package se.cygni.paintbot.socketserver.game;


//import com.google.common.eventbus.EventBus;
//import com.google.common.eventbus.Subscribe;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import se.cygni.game.Player;
import se.cygni.game.enums.Action;
import se.cygni.game.random.XORShiftRandom;
import se.cygni.paintbot.api.event.GameLinkEvent;
import se.cygni.paintbot.api.exception.InvalidPlayerName;
import se.cygni.paintbot.api.model.GameMode;
import se.cygni.paintbot.api.model.GameSettings;
import se.cygni.paintbot.api.request.ClientInfo;
import se.cygni.paintbot.api.request.RegisterMove;
import se.cygni.paintbot.api.request.RegisterPlayer;
import se.cygni.paintbot.api.request.StartGame;
import se.cygni.paintbot.api.response.PlayerRegistered;
import se.cygni.paintbot.api.util.MessageUtils;
import se.cygni.paintbot.socketserver.event.InternalGameEvent;
import se.cygni.paintbot.socketserver.mapper.ActionMapper;
import se.cygni.paintbot.socketserver.mapper.GameSettingsMapper;
import se.cygni.paintbot.socketserver.player.IPlayer;
import se.cygni.paintbot.socketserver.player.RemotePlayer;
import se.cygni.paintbot.socketserver.player.bot.*;

import java.time.Clock;
import java.util.UUID;

@Slf4j
@Getter
@Setter
public class Game {
    private final boolean trainingGame;
//    private final EventBus incomingEventBus;
//    private EventBus outgoingEventBus;
//    private final EventBus globalEventBus;
    private final String gameId;
    private PlayerManager playerManager;
    private GameFeatures gameFeatures;
    private final GameEngine gameEngine;
    private final String viewUrl;

    private XORShiftRandom botSelector = new XORShiftRandom();

    public Game(PlayerManager playerManager, GameFeatures gameFeatures, boolean trainingGame, String viewUrl) {
//        this.globalEventBus = globalEventBus;
        this.playerManager = playerManager;
        this.gameFeatures = gameFeatures;
        this.trainingGame = trainingGame;
        this.viewUrl = viewUrl;
        gameId = UUID.randomUUID().toString();
        gameEngine = new GameEngine(gameFeatures, playerManager, gameId);
//        incomingEventBus = new EventBus("game-" + gameId + "-incoming");
//        incomingEventBus.register(this);

//        outgoingEventBus = new EventBus("game-" + gameId + "-outgoing");
    }

//    public void setOutgoingEventBus(EventBus outgoingEventBus) {
//        this.outgoingEventBus = outgoingEventBus;
//    }

//    @Subscribe
    public void startGame(StartGame startGame) {
        if (trainingGame) {
            log.info("Starting game: {}", gameId);
            startGame();
        }
    }

//    @Subscribe
    public void registerPlayer(RegisterPlayer registerPlayer) {
        Player player = new Player(registerPlayer.getPlayerName());
        player.setPlayerId(registerPlayer.getReceivingPlayerId());

        if (playerManager.containsPlayerWithName(player.getName())) {
            InvalidPlayerName playerNameTaken = new InvalidPlayerName(InvalidPlayerName.PlayerNameInvalidReason.Taken);
            MessageUtils.copyCommonAttributes(registerPlayer, playerNameTaken);
//            outgoingEventBus.post(playerNameTaken);
            return;
        }

//        RemotePlayer remotePlayer = new RemotePlayer(player, outgoingEventBus);
        RemotePlayer remotePlayer = new RemotePlayer(player);
        addPlayer(remotePlayer);

        // If this is a training game changes to settings are allowed
        GameSettings requestedGameSettings = registerPlayer.getGameSettings();
        if (trainingGame && requestedGameSettings != null) {
            gameFeatures = GameSettingsMapper.INSTANCE.gameSettingsToGameFeatures(requestedGameSettings);
            gameEngine.reApplyGameFeatures(gameFeatures);
        }

        GameSettings gameSettings = GameSettingsMapper.INSTANCE.gameFeaturesToGameSettings(gameFeatures);
        PlayerRegistered playerRegistered = new PlayerRegistered(gameId, player.getName(), gameSettings, GameMode.TRAINING);
        MessageUtils.copyCommonAttributes(registerPlayer, playerRegistered);

//        outgoingEventBus.post(playerRegistered);
        sendGameLink(player);
        publishGameChanged();
    }

//    @Subscribe
    public void registerMove(RegisterMove registerMove) {
        long gameTick = registerMove.getGameTick();
        String playerId = registerMove.getReceivingPlayerId();
        Action action = ActionMapper.toDirection(registerMove.getDirection());
//
        if (!gameId.equals(registerMove.getGameId())) {
            log.warn("Player: {}, playerId: {}, tried to register move for wrong game. Aborting that move.",
                    playerManager.getPlayerName(playerId),
                    playerId);
            return;
        }

        gameEngine.registerAction(
                gameTick,
                playerId,
                action
        );
    }

//    @Subscribe
    public void clientInfo(ClientInfo clientInfo) {
        log.info("Client Info: {}", clientInfo);
//        globalEventBus.post(clientInfo);
    }

    public void startGame() {
        if (gameEngine.isGameRunning()) {
            return;
        }

        initBotPlayers();
        gameEngine.startGame();
    }

    public void addPlayer(IPlayer player) {
        playerManager.add(player);
        publishGameChanged();
    }

    private void sendGameLink(Player player) {
        GameLinkEvent gle = new GameLinkEvent(gameId, viewUrl + gameId);
        gle.setReceivingPlayerId(player.getPlayerId());
//        outgoingEventBus.post(gle);
    }


    public void playerLostConnection(String playerId) {
        try {
            IPlayer player = playerManager.getPlayer(playerId);
            player.stunned(gameEngine.getCurrentWorldTick());
            log.info("Player: {} , playerId: {} lost connection and was therefore killed.", player.getName(), playerId);
        } catch (Exception e) {
            log.warn("PlayerId: {} lost connection but I could not remove her (which is OK, she probably wasn't registered in the first place)", playerId);
        }
        if (playerManager.getLiveAndRemotePlayers().size() == 0) {
            abort();
        } else {
            publishGameChanged();
        }
    }

    public boolean isEnded() {
        return gameEngine.isGameComplete();
    }

    public GameResult getGameResult() {
        return gameEngine.getGameResult();
    }

    private void initBotPlayers() {
        if (!trainingGame)
            return;

        for (int i = 0; i < gameFeatures.getMaxNoofPlayers() - 1; i++) {
            BotPlayer bot;

            switch (Math.abs(botSelector.nextInt() % 5)) {
                case 0:
//                    bot = new RandomBot(UUID.randomUUID().toString(), incomingEventBus);
                    bot = new RandomBot(UUID.randomUUID().toString());
                    break;
                case 1:
//                    bot = new StraightBot(UUID.randomUUID().toString(), incomingEventBus);
                    bot = new StraightBot(UUID.randomUUID().toString());
                    break;
                case 2:
//                    bot = new PoweredBot(UUID.randomUUID().toString(), incomingEventBus);
                    bot = new PoweredBot(UUID.randomUUID().toString());
                    break;
                case 3:
//                    bot = new AggroBot(UUID.randomUUID().toString(), incomingEventBus);
                    bot = new AggroBot(UUID.randomUUID().toString());
                    break;
                default:
//                    bot = new StraightBot(UUID.randomUUID().toString(), incomingEventBus);
                    bot = new StraightBot(UUID.randomUUID().toString());
                    break;
            }

            addPlayer(bot);
        }
    }

    public void abort() {
        playerManager.clear();
        gameEngine.abort();

        InternalGameEvent gevent = new InternalGameEvent(this, Clock.systemUTC());
        gevent.onGameAborted(getGameId());
//        globalEventBus.post(gevent);
//        globalEventBus.post(gevent.getGameMessage());
    }

    public void publishGameChanged() {
        InternalGameEvent gevent = new InternalGameEvent(this, Clock.systemUTC());
        gevent.onGameChanged(getGameId());
//        globalEventBus.post(gevent);
    }
}
