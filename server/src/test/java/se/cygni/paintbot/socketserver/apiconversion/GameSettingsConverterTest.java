package se.cygni.paintbot.socketserver.apiconversion;

import org.junit.Test;
import se.cygni.paintbot.api.model.GameSettings;
import se.cygni.paintbot.socketserver.game.GameFeatures;
import se.cygni.paintbot.socketserver.mapper.GameSettingsMapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameSettingsConverterTest {

    @Test
    public void testToGameSettings() throws Exception {
        GameFeatures gameFeatures = new GameFeatures();
        gameFeatures.setTrainingGame(false);

        GameSettings gameSettings = GameSettingsMapper.INSTANCE.gameFeaturesToGameSettings(gameFeatures);

        assertEquals(false, gameSettings.isTrainingGame());
    }

    @Test
    public void testFromGameSettings() throws Exception {
        GameSettings gameSettings = new GameSettings();
        gameSettings.setTrainingGame(true);
        GameFeatures gameFeatures = GameSettingsMapper.INSTANCE.gameSettingsToGameFeatures(gameSettings);

        assertTrue(gameFeatures.isTrainingGame());
    }
}