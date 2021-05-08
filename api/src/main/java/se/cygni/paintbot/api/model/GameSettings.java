package se.cygni.paintbot.api.model;

public class GameSettings {

    // Maximum no of players in this game
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

    // Minumum number of rounds a character is stunned
    private int minNoOfTicksStunned = 8;

    // Maximum number of rounds a character is stunned
    private int maxNoOfTicksStunned = 10;

    // The starting count for obstacles
    private int startObstacles = 30;

    // The starting count for power up
    private int startPowerUps = 0;

    private int gameDurationInSeconds = 180;

    private int explosionRange = 4;

    private boolean pointsPerTick = false;

    public int getMaxNoofPlayers() {
        return maxNoofPlayers;
    }

    public int getTimeInMsPerTick() {
        return timeInMsPerTick;
    }

    public boolean isObstaclesEnabled() {
        return obstaclesEnabled;
    }

    public boolean isPowerUpsEnabled() {
        return powerUpsEnabled;
    }

    public int getAddPowerUpLikelihood() {
        return addPowerUpLikelihood;
    }

    public int getRemovePowerUpLikelihood() {
        return removePowerUpLikelihood;
    }

    public boolean isTrainingGame() {
        return trainingGame;
    }

    public int getPointsPerTileOwned() {
        return pointsPerTileOwned;
    }

    public int getPointsPerCausedStun() {
        return pointsPerCausedStun;
    }

    public int getNoOfTicksInvulnerableAfterStun() {
        return noOfTicksInvulnerableAfterStun;
    }

    public int getMinNoOfTicksStunned() {
        return minNoOfTicksStunned;
    }

    public int getMaxNoOfTicksStunned() {
        return maxNoOfTicksStunned;
    }

    public int getStartObstacles() {
        return startObstacles;
    }

    public int getStartPowerUps() {
        return startPowerUps;
    }

    public int getGameDurationInSeconds() {
        return gameDurationInSeconds;
    }

    public int getExplosionRange() {
        return explosionRange;
    }

    public void setMaxNoofPlayers(int maxNoofPlayers) {
        this.maxNoofPlayers = maxNoofPlayers;
    }

    public void setTimeInMsPerTick(int timeInMsPerTick) {
        this.timeInMsPerTick = timeInMsPerTick;
    }

    public void setObstaclesEnabled(boolean obstaclesEnabled) {
        this.obstaclesEnabled = obstaclesEnabled;
    }

    public void setPowerUpsEnabled(boolean powerUpsEnabled) {
        this.powerUpsEnabled = powerUpsEnabled;
    }

    public void setAddPowerUpLikelihood(int addPowerUpLikelihood) {
        this.addPowerUpLikelihood = addPowerUpLikelihood;
    }

    public void setRemovePowerUpLikelihood(int removePowerUpLikelihood) {
        this.removePowerUpLikelihood = removePowerUpLikelihood;
    }

    public void setTrainingGame(boolean trainingGame) {
        this.trainingGame = trainingGame;
    }

    public void setPointsPerTileOwned(int pointsPerTileOwned) {
        this.pointsPerTileOwned = pointsPerTileOwned;
    }

    public void setPointsPerCausedStun(int pointsPerCausedStun) {
        this.pointsPerCausedStun = pointsPerCausedStun;
    }

    public void setNoOfTicksInvulnerableAfterStun(int noOfTicksInvulnerableAfterStun) {
        this.noOfTicksInvulnerableAfterStun = noOfTicksInvulnerableAfterStun;
    }

    public void setMinNoOfTicksStunned(int minNoOfTicksStunned) {
        this.minNoOfTicksStunned = minNoOfTicksStunned;
    }

    public void setMaxNoOfTicksStunned(int maxNoOfTicksStunned) {
        this.maxNoOfTicksStunned = maxNoOfTicksStunned;
    }

    public void setStartObstacles(int startObstacles) {
        this.startObstacles = startObstacles;
    }

    public void setStartPowerUps(int startPowerUps) {
        this.startPowerUps = startPowerUps;
    }

    public void setGameDurationInSeconds(int gameDurationInSeconds) {
        this.gameDurationInSeconds = gameDurationInSeconds;
    }

    public void setExplosionRange(int explosionRange) {
        this.explosionRange = explosionRange;
    }

    public boolean getPointsPerTick() {
        return pointsPerTick;
    }

    public void setPointsPerTick(boolean pointsPerTick) {
        this.pointsPerTick = pointsPerTick;
    }
}
