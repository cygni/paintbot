package se.cygni.paintbot.game;

import org.junit.Test;
import se.cygni.game.Player;
import se.cygni.paintbot.player.IPlayer;
import se.cygni.paintbot.player.RemotePlayer;
import se.cygni.paintbot.tournament.TournamentLevel;
import se.cygni.paintbot.tournament.TournamentPlan;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TournamentPlanTest {

    @Test
    public void testGetLevelsSmall() throws Exception {
        GameFeatures gf = new GameFeatures();
        gf.setMaxNoofPlayers(10);

        Set<IPlayer> players = getPlayers(20);
        PlayerManager playerManager = new PlayerManager();
        playerManager.addAll(players);

        TournamentPlan tp = new TournamentPlan(gf, playerManager);

        List<TournamentLevel> levels = tp.getLevels();
        assertEquals(2, levels.size());
        assertEquals(20, levels.get(0).getExpectedNoofPlayers());
        assertEquals(10, levels.get(1).getExpectedNoofPlayers());
    }

    @Test
    public void testGetLevelsXLarge() throws Exception {
        GameFeatures gf = new GameFeatures();
        gf.setMaxNoofPlayers(10);

        Set<IPlayer> players = getPlayers(30);
        PlayerManager playerManager = new PlayerManager();
        playerManager.addAll(players);

        TournamentPlan tp = new TournamentPlan(gf, playerManager);

        List<TournamentLevel> levels = tp.getLevels();
        assertEquals(3, levels.size());
        assertEquals(30, levels.get(0).getExpectedNoofPlayers());
        assertEquals(20, levels.get(1).getExpectedNoofPlayers());
        assertEquals(10, levels.get(2).getExpectedNoofPlayers());
    }

    public static Set<IPlayer> getPlayers(int noof) {
        Set<IPlayer> players = new HashSet<>();
        for (int i = 0; i < noof; i++) {
            Player p = new Player("testplayer_" + i);
            p.setPlayerId(UUID.randomUUID().toString());
            players.add(new RemotePlayer(p, null));
        }
        return players;
    }
}