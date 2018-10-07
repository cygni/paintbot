package se.cygni.snake.game;

public class GameFeatures {

    private int width = 46;

    private int height = 34;

    // Maximum noof players in this game
    private int maxNoofPlayers = 10;

    // The time clients have to respond with a new move
    private int timeInMsPerTick = 250;

    // Randomly place obstacles
    private boolean obstaclesEnabled = true;

    // Randomly place food
    private boolean bombsEnabled = true;

    // Likelihood (in percent) that a new food will be
    // added to the world
    private int addBombLikelihood = 15;

    // Likelihood (in percent) that a
    // food will be removed from the world
    private int removeBombLikelihood = 5;


    // Indicates that this is a training game,
    // Bots will be added to fill up remaining players.
    private boolean trainingGame = true;

    // Points given per length unit the Snake has
    private int pointsPerTileOwned = 1;


    // Points given per caused death (i.e. another
    // snake collides with yours)
    private int pointsPerCausedStun = 5;


    // Number of rounds a character is protected after stun
    private int noOfTicksInvulnerableAfterStun = 3;

    // Number of rounds a character is stunned

    private int noOfTicksStunned = 3;
    // The starting count for obstacles

    private int startObstacles = 5;
    // The starting count for bomb

    private int startBombs = 0;

    private int gameDurationInSeconds = 20; //TODO Dont forget to set this back to something reasonable

    /**
     * Enforces limits on some values
     */
    public void applyValidation() {
        maxNoofPlayers = Math.min(20, maxNoofPlayers);
        maxNoofPlayers = Math.max(2, maxNoofPlayers);

        // spontaneousGrowthEveryNWorldTick = spontaneousGrowthEveryNWorldTick < 2 ? 2 : spontaneousGrowthEveryNWorldTick;
        startObstacles = Math.max(0, startObstacles);
        startBombs = Math.max(0, startBombs);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getGameDurationInSeconds() {
        return gameDurationInSeconds;
    }

    public int getNoOfTicksStunned() {
        return noOfTicksStunned;
    }

    public void setNoOfTicksStunned(int noOfTicksStunned) {
        this.noOfTicksStunned = noOfTicksStunned;
    }

    public int getMaxNoofPlayers() {
        return maxNoofPlayers;
    }

    public void setMaxNoofPlayers(int maxNoofPlayers) {
        this.maxNoofPlayers = maxNoofPlayers;
    }

    public int getTimeInMsPerTick() {
        return timeInMsPerTick;
    }

    public void setTimeInMsPerTick(int timeInMsPerTick) {
        this.timeInMsPerTick = timeInMsPerTick;
    }

    public boolean isObstaclesEnabled() {
        return obstaclesEnabled;
    }

    public void setObstaclesEnabled(boolean obstaclesEnabled) {
        this.obstaclesEnabled = obstaclesEnabled;
    }

    public boolean isBombsEnabled() {
        return bombsEnabled;
    }

    public void setBombsEnabled(boolean bombsEnabled) {
        this.bombsEnabled = bombsEnabled;
    }

    public int getAddBombLikelihood() {
        return addBombLikelihood;
    }

    public void setAddBombLikelihood(int addBombLikelihood) {
        this.addBombLikelihood = addBombLikelihood;
    }

    public int getRemoveBombLikelihood() {
        return removeBombLikelihood;
    }

    public void setRemoveBombLikelihood(int removeBombLikelihood) {
        this.removeBombLikelihood = removeBombLikelihood;
    }

    public boolean isTrainingGame() {
        return trainingGame;
    }

    public void setTrainingGame(boolean trainingGame) {
        this.trainingGame = trainingGame;
    }

    public int getPointsPerTileOwned() {
        return pointsPerTileOwned;
    }

    public void setPointsPerTileOwned(int pointsPerTileOwned) {
        this.pointsPerTileOwned = pointsPerTileOwned;
    }

    public int getPointsPerCausedStun() {
        return pointsPerCausedStun;
    }

    public void setPointsPerCausedStun(int pointsPerCausedStun) {
        this.pointsPerCausedStun = pointsPerCausedStun;
    }

    public int getNoOfTicksInvulnerableAfterStun() {
        return noOfTicksInvulnerableAfterStun;
    }

    public void setNoOfTicksInvulnerableAfterStun(int noOfTicksInvulnerableAfterStun) {
        this.noOfTicksInvulnerableAfterStun = noOfTicksInvulnerableAfterStun;
    }

    public int getStartObstacles() {
        return startObstacles;
    }

    public void setStartObstacles(int startObstacles) {
        this.startObstacles = startObstacles;
    }

    public int getStartBombs() {
        return startBombs;
    }

    public void setStartBombs(int startBombs) {
        this.startBombs = startBombs;
    }
}
