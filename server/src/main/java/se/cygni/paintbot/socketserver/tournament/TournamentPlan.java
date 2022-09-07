package se.cygni.paintbot.socketserver.tournament;

import se.cygni.paintbot.socketserver.game.GameFeatures;
import se.cygni.paintbot.socketserver.game.PlayerManager;
import se.cygni.paintbot.socketserver.player.IPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class TournamentPlan {

    private List<TournamentLevel> levels = new ArrayList<>();
    private final GameFeatures gameFeatures;
    private final PlayerManager playerManager;


    public TournamentPlan(
            GameFeatures gameFeatures,
            PlayerManager playerManager) {

        this.gameFeatures = gameFeatures;
        this.playerManager = playerManager;

        createPlan();
    }

    public TournamentLevel getLevelAt(int pos) {
        if (pos < 0 || pos >= levels.size()) {
            throw new RuntimeException("Not good, you tried to get a level out of bounds.");
        }

        return levels.get(pos);
    }

    public List<TournamentLevel> getLevels() {
        return levels;
    }

    private void createPlan() {

        int maxPlayersPerGame = gameFeatures.getMaxNoofPlayers();

        int noofLevels = TournamentUtil.getNoofLevels(playerManager.size(), maxPlayersPerGame);
        int[] gameDistribution = TournamentUtil.getGameDistribution(noofLevels);

        for (int index = 0; index < noofLevels; index++) {
            int noofPlayersInLevel = 0;
            if (index == 0) {
                noofPlayersInLevel = playerManager.size();
            } else {
                noofPlayersInLevel = gameDistribution[index] * maxPlayersPerGame;
            }

            TournamentLevel level = new TournamentLevel(index, gameDistribution[index], noofPlayersInLevel, maxPlayersPerGame);
            levels.add(level);
        }
    }

    public Set<IPlayer> getPlayers() {
        return playerManager.toSet();
    }
}
