package se.cygni.snake.api.event;

import org.junit.Test;
import se.cygni.snake.api.GameMessageParser;
import se.cygni.snake.api.model.StunReason;

import static org.junit.Assert.*;

public class CharacterStunnedEventTest {

    @Test
    public void testSerializationSnakeDeadEvent() throws Exception {
        CharacterStunnedEvent sde = new CharacterStunnedEvent(StunReason.CollisionWithWall, "playerId", 5, 10, "6666", 99, durationInTicks);
        TestUtil.populateBaseData(sde, "rPlayerId");

        String serialized = GameMessageParser.encodeMessage(sde);

        CharacterStunnedEvent parsedSde = (CharacterStunnedEvent)GameMessageParser.decodeMessage(serialized);

        assertEquals(StunReason.CollisionWithWall, parsedSde.getStunReason());
        assertEquals("playerId", parsedSde.getPlayerId());
        assertEquals(5, parsedSde.getX());
        assertEquals(10, parsedSde.getY());
        assertEquals("6666", parsedSde.getGameId());
        assertEquals(99, parsedSde.getGameTick());
        assertEquals("rPlayerId", parsedSde.getReceivingPlayerId());
    }
}