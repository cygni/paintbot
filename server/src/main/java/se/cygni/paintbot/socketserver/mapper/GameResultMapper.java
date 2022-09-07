package se.cygni.paintbot.socketserver.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.cygni.paintbot.api.model.PlayerRank;
import se.cygni.paintbot.socketserver.game.GameResult;
import se.cygni.paintbot.socketserver.player.IPlayer;

import java.util.ArrayList;
import java.util.List;

public class GameResultMapper {

    private static Logger log = LoggerFactory
            .getLogger(GameResultMapper.class);

    public static List<PlayerRank> getPlayerRanks(GameResult gameResult) {
        List<IPlayer> players = gameResult.getSortedResult();

        List<PlayerRank> playerRanks = new ArrayList<>();
        int c = 1;
        for (IPlayer player : players) {
            PlayerRank pr = new PlayerRank(player.getName(), player.getPlayerId(), c, player.getTotalPoints(), player.isAlive());
            playerRanks.add(pr);
            c++;
        }

        return playerRanks;
    }
}
