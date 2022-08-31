package se.cygni.paintbot.socketserver.game;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.cygni.paintbot.api.model.GameSettings;
import se.cygni.paintbot.api.request.RegisterPlayer;
import se.cygni.paintbot.socketserver.game.*;

public class GameEngineTest {
    AutoCloseable openMocks;
    @Mock
    private PlayerManager playerManager;
    @InjectMocks
    private GameManager gameManager;
    @InjectMocks
    private GameEngine gameEngine;
    private Game game;

    @Before
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        GameFeatures gameFeatures = new GameFeatures();
        gameFeatures.setTimeInMsPerTick(1000);
        gameFeatures.setMaxNoofPlayers(25);
        gameFeatures.setTrainingGame(true);
        game = gameManager.createGame(gameFeatures);
        gameEngine = game.getGameEngine();
    }

    @After
    public void tearDown() throws Exception {
        openMocks.close();
    }


    @Test
    public void testGame() {
        RegisterPlayer emil = new RegisterPlayer("emil", new GameSettings());
        emil.setReceivingPlayerId("id-" + emil.getPlayerName());
        game.registerPlayer(emil);
        RegisterPlayer lisa = new RegisterPlayer("lisa", new GameSettings());
        lisa.setReceivingPlayerId("id-" + lisa.getPlayerName());
        game.registerPlayer(lisa);
        game.startGame();
    }

    @Test
    @Ignore
    public void testSimpleGame() {
        game.startGame();

        do {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
        } while (game.getGameEngine().isGameRunning());
    }
}
