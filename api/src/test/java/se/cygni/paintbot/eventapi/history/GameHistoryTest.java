package se.cygni.paintbot.eventapi.history;

import org.junit.Test;
import se.cygni.paintbot.api.GameMessage;
import se.cygni.paintbot.api.event.GameStartingEvent;
import se.cygni.paintbot.api.model.GameSettings;
import se.cygni.paintbot.eventapi.ApiMessageParser;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class GameHistoryTest {

    @Test
    public void testSerialization() throws Exception {

        String gameId = "game-has-id-3";
        LocalDateTime now = LocalDateTime.now();
        String[] players =  new String[] {"Emil", "Johannes", "Barkis"};

        List<GameMessage> gameMessages = new ArrayList<>();
        gameMessages.add(new GameStartingEvent(gameId, 3, 46, 34, new GameSettings()));

        GameHistory gh = new GameHistory(gameId, players, now, gameMessages);

        String msg = ApiMessageParser.encodeMessage(gh);
        System.out.println(msg);
        GameHistory ghReparsed = (GameHistory)ApiMessageParser.decodeMessage(msg);

        assertEquals(gameId, ghReparsed.getGameId());
        assertArrayEquals(players, ghReparsed.getPlayerNames());
        assertEquals(now.truncatedTo(ChronoUnit.MILLIS), ghReparsed.getGameDate().truncatedTo(ChronoUnit.MILLIS));

        GameStartingEvent gse = (GameStartingEvent)gh.getMessages().get(0);
        assertEquals(gameId, gse.getGameId());
        assertEquals(3, gse.getNoofPlayers());
        assertEquals(46, gse.getWidth());
        assertEquals(34, gse.getHeight());
    }
}