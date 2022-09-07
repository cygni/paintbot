package se.cygni.paintbot.socketserver.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameFeatures {

    private int width = 46;

    private int height = 34;

    // Maximum noof players in this game
    private int maxNoofPlayers = 6;

    // The time clients have to respond with a new move
    private int timeInMsPerTick = 250;

    // Randomly place obstacles
    private boolean obstaclesEnabled = true;

    // Randomly place power ups
    private boolean powerUpsEnabled = true;

    // Likelihood (in percent) that a new power up will be
    // added to the world
    private int addPowerUpLikelihood = 15;

    // Likelihood (in percent) that a
    // power up will be removed from the world
    private int removePowerUpLikelihood = 5;

    // Indicates that this is a training game,
    // Bots will be added to fill up remaining players.
    private boolean trainingGame = true;

    // Points given per length unit the Paintbot has
    private int pointsPerTileOwned = 1;

    // Points given per caused death (i.e. another
    // paintbot collides with yours)
    private int pointsPerCausedStun = 5;

    // Number of rounds a character is protected after stun
    private int noOfTicksInvulnerableAfterStun = 3;

    // Minimum number of rounds a character is stunned
    private int minNoOfTicksStunned = 8;

    // Maximum number of rounds a character is stunned
    private int maxNoOfTicksStunned = 10;

    // The starting count for obstacles
    private int startObstacles = 30;

    // The starting count for power ups
    private int startPowerUps = 0;

    private int gameDurationInSeconds = 180;

    private int explosionRange = 4;

    private boolean pointsPerTick = false;

    /**
     * Enforces limits on some values
     */
    public void applyValidation() {
        maxNoofPlayers = Math.min(20, maxNoofPlayers);
        maxNoofPlayers = Math.max(2, maxNoofPlayers);

        startObstacles = Math.max(0, startObstacles);
        startPowerUps = Math.max(0, startPowerUps);

        minNoOfTicksStunned = Math.max(0, minNoOfTicksStunned);
        maxNoOfTicksStunned = Math.max(minNoOfTicksStunned, maxNoOfTicksStunned);
    }

    /**
     * Randomizes a number of ticks to be stunned
     * @return A random value between min and max number of ticks stunned
     */
    public int getRandomNoOfTicksStunned() {
        int diff = maxNoOfTicksStunned - minNoOfTicksStunned;
        int randomness = (int)(Math.random() * (1 + diff));
        return minNoOfTicksStunned + randomness;
    }
}
