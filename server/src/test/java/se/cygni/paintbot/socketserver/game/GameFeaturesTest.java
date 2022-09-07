package se.cygni.paintbot.socketserver.game;

import org.junit.Assert;
import org.junit.Test;
import se.cygni.paintbot.socketserver.game.GameFeatures;

public class GameFeaturesTest {

    @Test
    public void testMaxNoofPlayersIsMax20() throws Exception {
        GameFeatures gf = new GameFeatures();
        gf.setMaxNoofPlayers(99);
        gf.applyValidation();

        Assert.assertEquals(20, gf.getMaxNoofPlayers());
    }

    @Test
    public void testMaxNoofPlayersIsMin2() throws Exception {
        GameFeatures gf = new GameFeatures();
        gf.setMaxNoofPlayers(-2);
        gf.applyValidation();

        Assert.assertEquals(2, gf.getMaxNoofPlayers());

        gf.setMaxNoofPlayers(0);
        gf.applyValidation();

        Assert.assertEquals(2, gf.getMaxNoofPlayers());
    }

    @Test
    public void testMinNoOfTicksStunnedIsMin0() throws Exception {
        GameFeatures gf = new GameFeatures();
        gf.setMinNoOfTicksStunned(-5);
        gf.applyValidation();

        Assert.assertEquals(0, gf.getMinNoOfTicksStunned());
    }

    @Test
    public void testMaxNoOfTicksStunnedIsGreaterThanOrEqualToMin() throws Exception {
        GameFeatures gf = new GameFeatures();
        gf.setMinNoOfTicksStunned(10);
        gf.setMaxNoOfTicksStunned(5);
        gf.applyValidation();

        Assert.assertEquals(10, gf.getMaxNoOfTicksStunned());
    }

    @Test
    public void testStartObstaclesIsPositive() throws Exception {
        GameFeatures gf = new GameFeatures();
        gf.setStartObstacles(-2);
        gf.applyValidation();

        Assert.assertEquals(0, gf.getStartObstacles());

        gf.setStartObstacles(0);
        gf.applyValidation();

        Assert.assertEquals(0, gf.getStartObstacles());

        gf.setStartObstacles(7);
        gf.applyValidation();

        Assert.assertEquals(7, gf.getStartObstacles());
    }

    @Test
    public void testStartFoodIsPositive() throws Exception {
        GameFeatures gf = new GameFeatures();
        gf.setStartPowerUps(-2);
        gf.applyValidation();

        Assert.assertEquals(0, gf.getStartPowerUps());

        gf.setStartPowerUps(0);
        gf.applyValidation();

        Assert.assertEquals(0, gf.getStartPowerUps());

        gf.setStartPowerUps(7);
        gf.applyValidation();

        Assert.assertEquals(7, gf.getStartPowerUps());
    }
}