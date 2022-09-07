package se.cygni.paintbot.socketserver.wshandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.cygni.paintbot.socketserver.game.Game;
import se.cygni.paintbot.socketserver.game.GameManager;

@Slf4j
@Service
public class TrainingWebSocketHandler extends BaseGameSocketHandler {
    private final Game game;

    public TrainingWebSocketHandler(GameManager gameManager) {
        log.info("Started training web socket handler");
        game = gameManager.createTrainingGame();

//        setOutgoingEventBus(game.getOutgoingEventBus());
//        setIncomingEventBus(game.getIncomingEventBus());
    }

    @Override
    protected void playerLostConnection() {
        log.info("{} lost connection", getPlayerId());
        game.playerLostConnection(getPlayerId());
        game.abort();
    }
}
