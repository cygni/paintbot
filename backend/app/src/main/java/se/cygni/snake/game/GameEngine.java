package se.cygni.snake.game;

import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.cygni.game.WorldState;
import se.cygni.game.enums.Action;
import se.cygni.game.random.XORShiftRandom;
import se.cygni.game.transformation.*;
import se.cygni.game.worldobject.Bomb;
import se.cygni.game.worldobject.CharacterImpl;
import se.cygni.snake.api.GameMessage;
import se.cygni.snake.api.event.GameEndedEvent;
import se.cygni.snake.api.event.GameResultEvent;
import se.cygni.snake.api.event.GameStartingEvent;
import se.cygni.snake.api.event.MapUpdateEvent;
import se.cygni.snake.apiconversion.GameMessageConverter;
import se.cygni.snake.apiconversion.GameSettingsConverter;
import se.cygni.snake.event.InternalGameEvent;
import se.cygni.snake.player.IPlayer;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * GameEngine is responsible for:
 *
 * - Maintaining the world
 * - Handling the time ticker
 * - Executing player moves
 * - Executing the rules from GameFeatures
 */
public class GameEngine {
    private static Logger log = LoggerFactory
            .getLogger(GameEngine.class);

    private GameFeatures gameFeatures;
    private WorldState world;
    private long currentWorldTick = 0;
    private java.util.Map<String, Action> characterActions;
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private AtomicBoolean gameComplete = new AtomicBoolean(false);
    private final EventBus globalEventBus;
    private final WorldTransformer worldTransformer;
    private final PlayerManager playerManager;
    private final String gameId;

    private CountDownLatch countDownLatch;
    private Set<String> registeredActionsByPlayers = Collections.synchronizedSet(new HashSet<>());
    private XORShiftRandom random = new XORShiftRandom();
    private GameResult gameResult;


    public GameEngine(GameFeatures gameFeatures,
                      PlayerManager playerManager,
                      String gameId,
                      EventBus globalEventBus) {

        this.gameFeatures = gameFeatures;
        this.gameId = gameId;
        this.playerManager = playerManager;
        this.globalEventBus = globalEventBus;
        this.worldTransformer = new WorldTransformer(
                gameFeatures, playerManager, gameId, globalEventBus
        );
        this.gameResult = new GameResult();
    }

    public void reApplyGameFeatures(GameFeatures gameFeatures) {
        this.gameFeatures = gameFeatures;
    }

    public void startGame() {
        initGame();
        gameLoop();
    }

    public void abort() {
        isRunning.set(false);
    }

    private void initPlacePlayers() {
        // Place players
        List<CharacterImpl> characters = playerManager.toSet().stream().map(player -> new CharacterImpl(player.getName(), player.getPlayerId(), 0)).collect(Collectors.toList());
        AddWorldObjectsInCircle charactersInCircleFormation = new AddWorldObjectsInCircle(characters, 0.9d);
        world = charactersInCircleFormation.transform(world);
    }

    private void initPlaceBombs() {
        if (gameFeatures.isBombsEnabled()) {
            IntStream.range(0, gameFeatures.getStartBombs()).forEach(n -> {
                AddWorldObjectAtRandomPosition addFoodTransform = new AddWorldObjectAtRandomPosition(new Bomb());
                world = addFoodTransform.transform(world);
            });
        }
    }

    private void initPlaceObstacles() {
        if (gameFeatures.isObstaclesEnabled()) {
            IntStream.range(0, gameFeatures.getStartObstacles()).forEach(n -> {
                AddRandomObstacle obstacleTransform = new AddRandomObstacle();
                world = obstacleTransform.transform(world);
            });
        }
    }

    private void notifyAllPlayers(GameMessage message) {
        notifyPlayers(playerManager.toSet(), message);
    }

    private void notifyPlayers(Set<IPlayer> players, GameMessage message) {
        players.forEach(player -> {
            try {
                player.onGameMessage((GameMessage) message.clone());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        InternalGameEvent gevent = new InternalGameEvent(
                System.currentTimeMillis(),
                message);
        globalEventBus.post(gevent);
    }

    private void initGame() {
        world = new WorldState(gameFeatures.getWidth(), gameFeatures.getHeight());

        initPlacePlayers();

        notifyAllPlayers(new GameStartingEvent(
                gameId,
                playerManager.size(),
                world.getWidth(), world.getHeight(),
                GameSettingsConverter.toGameSettings(gameFeatures)));

        initPlaceObstacles();
        initPlaceBombs();
    }



    private void gameLoop() {
        initCharacterActions();

        Runnable r = () -> {
            // Set internal state to running
            isRunning.set(true);

            // Loop till winner is decided
            while (isGameRunning()) {

                Set<IPlayer> livePlayers = playerManager.getLivePlayers();
                countDownLatch = new CountDownLatch(livePlayers.size());
                registeredActionsByPlayers.clear();

                DecrementStun decrementStun = new DecrementStun();
                world = decrementStun.transform(world);

                Set<IPlayer> players = playerManager.toSet();
                MapUpdateEvent mapUpdateEvent = GameMessageConverter
                        .onWorldUpdate(world, gameId, currentWorldTick, players);

                notifyPlayers(livePlayers, mapUpdateEvent);

                long tstart = System.currentTimeMillis();
                try {
                    countDownLatch.await(gameFeatures.getTimeInMsPerTick(), TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    log.error("Waiting for all players moves was interrupted!", e);
                }

                long timeSpent = System.currentTimeMillis() - tstart;
                log.info("All moves received, gameId: {}, tick: {}, time waiting: " + timeSpent + "ms", gameId, currentWorldTick);

                try {
                    world = worldTransformer.transform(characterActions, gameFeatures, world, currentWorldTick);
                } catch (Exception e) {
                    // This is really undefined, if this happens we have a bug
                    log.error("Bug found in WorldTransformer:", e);
                }

                currentWorldTick++;

                // Add random objects
                if (gameFeatures.isBombsEnabled()) {
                    randomBomb();
                }
            }

            // Set internal state to not running
            isRunning.set(false);

            // Create GameResult
            Set<IPlayer> allPlayers = playerManager.toSet();
            for (IPlayer player : allPlayers) {
                gameResult.addResult(player);
                log.debug("Adding player {} to gameResult", player.getName());
            }

            gameComplete.set(true);

            // Notify of GameResult
            GameResultEvent gameResultEvent = GameMessageConverter.onGameResult(gameId, gameResult);
            notifyPlayers(allPlayers, gameResultEvent);

            // Notify of GameEnded
            GameEndedEvent gameEndedEvent = GameMessageConverter.onGameEnded(
                    gameResult.getWinner().getPlayerId(),
                    gameResult.getWinner().getName(),
                    gameId,
                    currentWorldTick,
                    world,
                    allPlayers
            );
            notifyPlayers(allPlayers, gameEndedEvent);

            publishGameChanged();
        };

        Thread t = new Thread(r);
        t.start();
    }

    private void randomBomb() {
        if (shouldExecute(gameFeatures.getRemoveBombLikelihood())) {
            RemoveRandomWorldObject<Bomb> removeTransform =
                    new RemoveRandomWorldObject<>(Bomb.class);
            world = removeTransform.transform(world);
        }

        if (shouldExecute(gameFeatures.getAddBombLikelihood())) {
            AddWorldObjectAtRandomPosition addTransform =
                    new AddWorldObjectAtRandomPosition(new Bomb());
            world = addTransform.transform(world);
        }
    }

    /**
     *
     * @param likelihood
     * @return true if a random double * 100 is smaller than likelihood
     */
    private boolean shouldExecute(int likelihood) {
        return likelihood > random.nextDouble()*100;
    }

    private void initCharacterActions() {
        characterActions = new HashMap<>();
        playerManager.toSet().forEach(player -> characterActions.put(player.getPlayerId(), Action.STAY));
    }

    public boolean isGameRunning() {
        long elapsed = (currentWorldTick * gameFeatures.getTimeInMsPerTick()) / 1000;
        return (isRunning.get() && gameFeatures.getGameDurationInSeconds() > elapsed);
    }

    public int noofLiveCharactersInWorld() {
        return (int) playerManager.toSet().stream()
                .filter(player -> player.isAlive())
                .count();
    }

    public void registerAction(long gameTick, String playerId, Action action) {
        if (!isGameRunning()) {
            return;
        }

        // Move is for wrong gameTick
        if (gameTick != currentWorldTick) {
            log.warn("Player: {} with id {} sent move within wrong world tick. Current world tick: {}, player's world tick: {}",
                    playerManager.getPlayerName(playerId), playerId,
                    currentWorldTick, gameTick);
            return;
        }

        // Player has already registered a move
        if (registeredActionsByPlayers.contains(playerId)) {
            log.warn("Player: {} with id {} sent more than one move. Current world tick: {}, player's world tick: {}",
                    playerManager.getPlayerName(playerId), playerId,
                    currentWorldTick, gameTick);
            return;
        }

        registeredActionsByPlayers.add(playerId);
        characterActions.put(playerId, action);
        countDownLatch.countDown();
    }

    private Action getRandomDirection() {
        int max = Action.values().length-1;
        return Action.values()[random.nextInt(max)];
    }

    public boolean isGameComplete() {
        return gameComplete.get();
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public void publishGameChanged() {
        InternalGameEvent gevent = new InternalGameEvent(System.currentTimeMillis());
        gevent.onGameChanged(gameId);
        globalEventBus.post(gevent);
    }

    public long getCurrentWorldTick() {
        return currentWorldTick;
    }
}
