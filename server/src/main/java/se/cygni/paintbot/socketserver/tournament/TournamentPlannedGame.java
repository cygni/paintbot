package se.cygni.paintbot.socketserver.tournament;

import se.cygni.paintbot.socketserver.game.Game;
import se.cygni.paintbot.socketserver.game.GameResult;
import se.cygni.paintbot.socketserver.player.HistoricalPlayer;
import se.cygni.paintbot.socketserver.player.IPlayer;

import java.util.HashSet;
import java.util.Set;

public class TournamentPlannedGame {

    private int expectedNoofPlayers;
    private Set<IPlayer> players;
    private Set<HistoricalPlayer> historicalPlayers = new HashSet<>();
    private GameResult gameResult = new GameResult();
    private Game game;

    public int getExpectedNoofPlayers() {
        return expectedNoofPlayers;
    }

    public void setExpectedNoofPlayers(int expectedNoofPlayers) {
        this.expectedNoofPlayers = expectedNoofPlayers;
    }

    public Set<IPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(Set<IPlayer> players) {
        this.players = players;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public boolean isGamePlayed() {
        if (game == null) {
            return false;
        }

        return game.isEnded();
    }

    public HistoricalPlayer getHistoricalPlayer(String playerId) {
        return historicalPlayers.stream().filter(player -> player.getPlayerId().equals(playerId)).findFirst().get();
    }

    public void createHistoricalGameResult() {
        if (historicalPlayers.size() > 0) {
            return;
        }

        for (IPlayer player : game.getPlayerManager().toSet()) {
            HistoricalPlayer hPlayer = new HistoricalPlayer(player);
            gameResult.addResult(hPlayer);
            historicalPlayers.add(hPlayer);
        }

        // Set winner
        HistoricalPlayer winner = (HistoricalPlayer)gameResult.getWinner();
        winner.setWinner(true);
        winner.setMovedUpInTournament(true);
    }
}
