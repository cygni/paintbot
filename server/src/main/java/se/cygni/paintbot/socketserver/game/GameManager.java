package se.cygni.paintbot.socketserver.game;

//import com.google.common.eventbus.EventBus;
//import com.google.common.eventbus.Subscribe;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.cygni.paintbot.api.event.GameAbortedEvent;
import se.cygni.paintbot.api.event.GameEndedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GameManager {
//    private final EventBus globalEventBus;
    private final PlayerManager playerManager;
    private GameEngine gameEngine;
    private Map<String, Game> activeGames = new ConcurrentHashMap<>(new HashMap<>());
    @Value("${paintbot.view.url}")
    private String viewUrl;

    public GameManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    //    @Autowired
//    public GameManager(EventBus globalEventBus) {
//        this.globalEventBus = globalEventBus;
//        globalEventBus.register(this);
//    }

    public Game createTrainingGame() {
        GameFeatures gameFeatures = new GameFeatures();
        gameFeatures.setTrainingGame(true);
//        Game game = new Game(gameFeatures, globalEventBus, true, viewUrl);
        Game game = new Game(playerManager, gameFeatures, true, viewUrl);

        registerGame(game);
        return game;
    }

    public Game createGame(GameFeatures gameFeatures) {
//        Game game = new Game(gameFeatures, globalEventBus, false, viewUrl);
        Game game = new Game(playerManager, gameFeatures, false, viewUrl);
        registerGame(game);
        return game;
    }

    public List<Game> listAllGames() {
        return activeGames
                .keySet()
                .stream()
                .map(id -> {
                    return getGame(id);
                })
                .collect(Collectors.toList());
    }

    public List<Game> listActiveGames() {
        return activeGames
                .keySet()
                .stream()
                .filter(id -> {
                    return getGame(id).getPlayerManager().getLiveAndRemotePlayers().size() > 0;
                }).map(id -> {
                    return getGame(id);
                }).collect(Collectors.toList());
    }

    public String[] listGameIds() {

        return activeGames
                .keySet()
                .stream()
                .filter(id -> {
                    return getGame(id).getPlayerManager().getLiveAndRemotePlayers().size() > 0;
                })
                .toArray(size -> new String[size]);
    }

    public Game getGame(String gameId) {
        return activeGames.get(gameId);
    }

    private void registerGame(Game game) {
        activeGames.put(game.getGameId(), game);

        log.info("Registered new game, posting to GlobalEventBus...");
//        globalEventBus.post(new InternalGameEvent(
//                System.currentTimeMillis(),
//                new GameCreatedEvent(game.getGameId())));
    }

    //    @Subscribe
    public void onGameEndedEvent(GameEndedEvent gameEndedEvent) {
        activeGames.remove(gameEndedEvent.getGameId());
    }

    //    @Subscribe
    public void onGameAbortedEvent(GameAbortedEvent gameAbortedEvent) {
        activeGames.remove(gameAbortedEvent.getGameId());
    }
}
