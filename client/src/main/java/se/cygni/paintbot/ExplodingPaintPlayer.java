package se.cygni.paintbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.cygni.paintbot.api.event.CharacterStunnedEvent;
import se.cygni.paintbot.api.event.GameEndedEvent;
import se.cygni.paintbot.api.event.GameLinkEvent;
import se.cygni.paintbot.api.event.GameResultEvent;
import se.cygni.paintbot.api.event.GameStartingEvent;
import se.cygni.paintbot.api.event.MapUpdateEvent;
import se.cygni.paintbot.api.event.TournamentEndedEvent;
import se.cygni.paintbot.api.exception.InvalidPlayerName;
import se.cygni.paintbot.api.model.CharacterAction;
import se.cygni.paintbot.api.model.GameMode;
import se.cygni.paintbot.api.model.GameSettings;
import se.cygni.paintbot.api.model.PlayerPoints;
import se.cygni.paintbot.api.response.PlayerRegistered;
import se.cygni.paintbot.api.util.GameSettingsUtils;
import se.cygni.paintbot.client.BasePaintbotClient;
import se.cygni.paintbot.client.MapCoordinate;
import se.cygni.paintbot.client.MapUtilityImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ExplodingPaintPlayer extends BasePaintbotClient {

    private static Logger log = LoggerFactory
            .getLogger(ExplodingPaintPlayer.class);

    Random random = new Random();

    private String name = "#explos_" + random.nextInt(1000);
    private String protocol = "ws";
    private String host = "localhost";
    private int port = 8080;
//    private String protocol = "wss";
//    private String host = "server.paintbot.cygni.se";
//    private int port = 443;
    private GameMode gameMode = GameMode.TRAINING;

    CharacterAction lastDirection;

    public static void main(String[] args) {

        Runnable task = () -> {

            ExplodingPaintPlayer sp = new ExplodingPaintPlayer();
            sp.connect();

            // Keep this process alive as long as the
            // Paintbot is connected and playing.
            do {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (sp.isPlaying());

            log.info("Shutting down");
        };


        Thread thread = new Thread(task);
        thread.start();
    }

    public ExplodingPaintPlayer() {
        lastDirection = getRandomDirection();
    }

    @Override
    public void onMapUpdate(MapUpdateEvent mapUpdateEvent) {
        MapUtilityImpl mapUtil = new MapUtilityImpl(mapUpdateEvent.getMap(), getPlayerId());

        Arrays.stream(mapUpdateEvent.getMap().getCharacterInfos())
                .filter(ci -> ci.getName().equals(name))
                .findFirst().ifPresent(ci -> log
                .info("Is carrying power up {} at tick: {}", ci.isCarryingPowerUp(), mapUpdateEvent.getGameTick()));

        log.info("Is carrying power up 2 {} at tick: {}", mapUtil.getMyCharacterInfo().isCarryingPowerUp(), mapUpdateEvent.getGameTick());

        if(mapUpdateEvent.getGameTick() % 10 == 0) {
            registerMove(mapUpdateEvent.getGameTick(), CharacterAction.EXPLODE);
            return;
        }

        MapCoordinate closestPowerUp = null;
        int closestPowerUpDistance = Integer.MAX_VALUE;
        MapCoordinate myPosition = mapUtil.getMyCoordinate();

        for (MapCoordinate mc : mapUtil.getCoordinatesContainingPowerUps()) {
            if(closestPowerUp == null) {
                closestPowerUp = mc;
                closestPowerUpDistance = closestPowerUp.getManhattanDistanceTo(myPosition);
            } else {
                int distance = myPosition.getManhattanDistanceTo(mc);
                if(distance < closestPowerUpDistance) {
                    closestPowerUp = mc;
                    closestPowerUpDistance = distance;
                }
            }

        }

        if(closestPowerUp == null) {
            registerMove(mapUpdateEvent.getGameTick(), lastDirection);
            return;
        }

        List<CharacterAction> possibleActions = new ArrayList<>();
        if(closestPowerUp.x < myPosition.x) {
            possibleActions.add(CharacterAction.LEFT);
        } else if(closestPowerUp.x > myPosition.x) {
            possibleActions.add(CharacterAction.RIGHT);
        }

        if(closestPowerUp.y < myPosition.y) {
            possibleActions.add(CharacterAction.UP);
        } else if(closestPowerUp.y > myPosition.y) {
            possibleActions.add(CharacterAction.DOWN);
        }

        CharacterAction chosenDirection = lastDirection;
        List<CharacterAction> validActions = possibleActions.stream().filter(mapUtil::canIMoveInDirection)
                .collect(Collectors.toList());

            // Choose a random direction
        if (!validActions.isEmpty())
            chosenDirection = validActions.get(random.nextInt(validActions.size()));

        // Register action here!
        registerMove(mapUpdateEvent.getGameTick(), chosenDirection);
        lastDirection = chosenDirection;
    }

    private CharacterAction getRandomDirection() {
        return CharacterAction.values()[random.nextInt(4)];
    }

    @Override
    public void onInvalidPlayerName(InvalidPlayerName invalidPlayerName) {

    }

    @Override
    public void onGameResult(GameResultEvent gameResultEvent) {
        log.info("Got a Game result:");
        gameResultEvent.getPlayerRanks().forEach(playerRank -> {
            log.info(playerRank.toString());
        });
    }

    @Override
    public void onPaintbotDead(CharacterStunnedEvent characterStunnedEvent) {
        log.info("A paintbot {} died by {}",
                characterStunnedEvent.getPlayerId(),
                characterStunnedEvent.getStunReason() + " at tick: " + characterStunnedEvent.getGameTick());
    }

    @Override
    public void onTournamentEnded(TournamentEndedEvent tournamentEndedEvent) {
        log.info("Tournament has ended, winner playerId: {}", tournamentEndedEvent.getPlayerWinnerId());
        int c = 1;
        for (PlayerPoints pp : tournamentEndedEvent.getGameResult()) {
            log.info("{}. {} - {} points", c++, pp.getName(), pp.getPoints());
        }
    }

    @Override
    public void onGameEnded(GameEndedEvent gameEndedEvent) {
        log.info("{} GameEnded gameId: {}, at tick: {}, winner: {}",
                getName(),
                gameEndedEvent.getGameId(),
                gameEndedEvent.getGameTick(),
                gameEndedEvent.getPlayerWinnerId());
    }

    @Override
    public void onGameStarting(GameStartingEvent gameStartingEvent) {
        log.info("GameStartingEvent, gameId: {} ", gameStartingEvent.getGameId());
    }

    @Override
    public void onPlayerRegistered(PlayerRegistered playerRegistered) {
        log.info("PlayerRegistered: " + playerRegistered);

        // Disable this if you want to start the game manually from
        // the web GUI
        startGame();
    }

    @Override
    public void onGameLink(GameLinkEvent gameLinkEvent) {
        log.info("The game can be viewed at: {}", gameLinkEvent.getUrl());
    }

    @Override
    public void onSessionClosed() {
        log.info("Session closed");
    }

    @Override
    public void onConnected() {
        log.info("Connected as: {}, registering for {}...", getName(), gameMode);
        GameSettings gameSettings = GameSettingsUtils.trainingWorld();
        gameSettings.setStartObstacles(10);
        registerForGame(gameSettings);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String getServerHost() {
        return host;
    }

    @Override
    public int getServerPort() {
        return port;
    }

    @Override
    public GameMode getGameMode() {
        return gameMode;
    }
}