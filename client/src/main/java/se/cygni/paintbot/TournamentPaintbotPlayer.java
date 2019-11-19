package se.cygni.paintbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.cygni.paintbot.api.event.*;
import se.cygni.paintbot.api.exception.InvalidPlayerName;
import se.cygni.paintbot.api.model.CharacterAction;
import se.cygni.paintbot.api.model.GameMode;
import se.cygni.paintbot.api.model.GameSettings;
import se.cygni.paintbot.api.model.PlayerPoints;
import se.cygni.paintbot.api.response.PlayerRegistered;
import se.cygni.paintbot.api.util.GameSettingsUtils;
import se.cygni.paintbot.client.AnsiPrinter;
import se.cygni.paintbot.client.BasePaintbotClient;
import se.cygni.paintbot.client.MapUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TournamentPaintbotPlayer extends BasePaintbotClient {

    private static Logger log = LoggerFactory
            .getLogger(TournamentPaintbotPlayer.class);

    Random random = new Random();

    private AnsiPrinter ansiPrinter;
    private String name = "#emil_" + random.nextInt(1000);
    private String host = "localhost";
    private int port = 8080;
//    private String host = "paintbot.cygni.se";
//    private int port = 80;
    private GameMode gameMode = GameMode.TOURNAMENT;

    CharacterAction lastDirection;

    public static void main(String[] args) {

        Runnable task = () -> {

            TournamentPaintbotPlayer sp = new TournamentPaintbotPlayer();
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

    public TournamentPaintbotPlayer() {
        ansiPrinter = new AnsiPrinter(true);
        lastDirection = getRandomDirection();
    }

    @Override
    public void onMapUpdate(MapUpdateEvent mapUpdateEvent) {
//        ansiPrinter.printMap(mapUpdateEvent);

        // MapUtil contains lot's of useful methods for querying the map!
        MapUtil mapUtil = new MapUtil(mapUpdateEvent.getMap(), getPlayerId());


        CharacterAction chosenDirection = lastDirection;
        List<CharacterAction> directions = new ArrayList<>();


        if (!mapUtil.canIMoveInDirection(lastDirection)) {
            // Let's see in which directions I can move
            if (mapUtil.canIMoveInDirection(CharacterAction.LEFT))
                directions.add(CharacterAction.LEFT);
            if (mapUtil.canIMoveInDirection(CharacterAction.RIGHT))
                directions.add(CharacterAction.RIGHT);
            if (mapUtil.canIMoveInDirection(CharacterAction.UP))
                directions.add(CharacterAction.UP);
            if (mapUtil.canIMoveInDirection(CharacterAction.DOWN))
                directions.add(CharacterAction.DOWN);

            // Choose a random direction
            if (!directions.isEmpty())
                chosenDirection = directions.get(random.nextInt(directions.size()));
        }

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