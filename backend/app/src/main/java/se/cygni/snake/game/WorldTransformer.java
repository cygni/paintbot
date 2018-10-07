package se.cygni.snake.game;

import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.cygni.game.Coordinate;
import se.cygni.game.WorldState;
import se.cygni.game.enums.Action;
import se.cygni.game.exception.TransformationException;
import se.cygni.game.transformation.PerformCharacterAction;
import se.cygni.game.worldobject.CharacterImpl;
import se.cygni.snake.api.event.CharacterStunnedEvent;
import se.cygni.snake.api.model.StunReason;
import se.cygni.snake.apiconversion.GameMessageConverter;
import se.cygni.snake.event.InternalGameEvent;
import se.cygni.snake.player.IPlayer;

import java.util.*;

public class WorldTransformer {

    private static final Logger log = LoggerFactory.getLogger(WorldTransformer.class);

    private final GameFeatures gameFeatures;
    private final PlayerManager playerManager;
    private final String gameId;
    private final EventBus globalEventBus;
    private ThreadLocal<WorldState> startingWorldState = new ThreadLocal<>();

    public WorldTransformer(GameFeatures gameFeatures, PlayerManager playerManager, String gameId, EventBus globalEventBus) {
        this.gameFeatures = gameFeatures;
        this.playerManager = playerManager;
        this.gameId = gameId;
        this.globalEventBus = globalEventBus;
    }

    public WorldState transform(
            Map<String, Action> actions,
            GameFeatures gameFeatures,
            WorldState ws,
            long worldTick) throws TransformationException {

        startingWorldState.set(ws);

        WorldState nextWorld = new WorldState(ws);


        for( Map.Entry<String, Action> e : actions.entrySet() ) {

            var pca = new PerformCharacterAction(
                    ws.getCharacterById(e.getKey()),
                    e.getValue());

            nextWorld = pca.transform(nextWorld);
        }


        Map<Integer, List<String>> playersPositions = new HashMap<>();

        for(int position : nextWorld.listPositionsWithContentOf(CharacterImpl.class)) {
            List<String> playersAtPosition = playersPositions.getOrDefault(position, new ArrayList<>());
            playersAtPosition.add(nextWorld.getCharacterAtPosition(position).getPlayerId());
            playersPositions.put(position, playersAtPosition);
        }

        // TODO Handle collision
        // TODO Handle stuns
        // TODO Handle explosions

        return nextWorld;

    }

    private void syncPoints(WorldState ws) {
        playerManager.toSet().forEach(player -> {
            if (player.isAlive())
                ws.getCharacterById(player.getPlayerId()).setPoints(player.getTotalPoints());
        });
    }

    private void notifyCharacterStunned(
            CharacterImpl head,
            StunReason stunReason,
            Coordinate coordinate,
            long worldTick) {

        IPlayer stunnedCharacter = playerManager.getPlayer(head.getPlayerId());
        log.info("Stun occurred by: {}. GameId: {}, Player: {}, with id: {}, stunned at: {}",
                stunReason,
                gameId,
                stunnedCharacter.getName(),
                stunnedCharacter.getPlayerId(),
                coordinate);

        stunnedCharacter.stunned(worldTick);

        CharacterStunnedEvent characterStunnedEvent = GameMessageConverter.onPlayerStunned(
                stunReason,
                gameFeatures.getNoOfTicksStunned(),
                head.getPlayerId(),
                coordinate.getX(), coordinate.getY(),
                gameId, worldTick);

        playerManager.toSet().stream().forEach(player -> {
            player.onCharacterStunned(characterStunnedEvent);
        });

        InternalGameEvent gevent = new InternalGameEvent(
                System.currentTimeMillis(),
                characterStunnedEvent);
        globalEventBus.post(gevent);
    }
}
