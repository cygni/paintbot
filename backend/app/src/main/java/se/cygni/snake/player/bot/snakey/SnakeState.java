package se.cygni.snake.player.bot.snakey;

import se.cygni.snake.api.model.CharacterAction;
import se.cygni.snake.client.MapCoordinate;

import java.util.*;

/**
 * Created by trivo on 2017-03-29.
 */
public class SnakeState {
    private int mapHeight;
    private int mapWidth;

    private Snake self;
    private HashSet<Snake> foeSet;
    private HashSet<MapCoordinate> obstacleSet;
    private HashSet<MapCoordinate> visitedTiles;
    private boolean isKilledFoeState;


    //only for updateState
    public SnakeState(int height, int width, Snake self, ArrayList<Snake> foes, MapCoordinate[] obstacles){
        this(height, width, self, foes, new HashSet<>(), false);
        for(MapCoordinate obstacle : obstacles){
            obstacleSet.add(obstacle);
        }
    }

    // for future state
    public SnakeState(int height, int width, Snake self, ArrayList<Snake> foes, HashSet<MapCoordinate> obstacles, boolean killed){
        this.mapHeight = height;
        this.mapWidth = width;
        this.self = self;
        this.isKilledFoeState = killed;
        obstacleSet = new HashSet<>();
        foeSet = new HashSet<>();

        for(Iterator<Snake> snakeIt = foes.iterator(); snakeIt.hasNext(); ){
            Snake foe = snakeIt.next();
            foeSet.add(foe);
        }

        for(MapCoordinate obstacle : obstacles){
            obstacleSet.add(obstacle);
        }
    }


    public void updateSnakeState(MapCoordinate selfNewHead, HashSet<Snake> foes){
        self.updatePos(selfNewHead);
        foeSet = foes;
    }

    //Creates a new, possible state from a given state and a direction of movement
    public SnakeState createFutureState(CharacterAction dir){

        Snake newSelf = new Snake(self);
        newSelf.setDir(dir);
        newSelf.stepOnePos();
        boolean killed = false;

        //TODO: Predict behaviour of foes here - target face
        ArrayList<Snake> newFoes = new ArrayList<>();
        for(Snake foe : foeSet) {
            Snake newFoe = new Snake(foe);

            estimateNewHead(newFoe); //remove all related if this fucks up
            if(newFoe.getLength() != 0){
                newFoes.add(newFoe);
            } else {
                killed = true;
            }
        }

        return new SnakeState(mapHeight, mapWidth, newSelf, newFoes, obstacleSet, killed);

    }

    private void estimateNewHead(Snake snake){
        if(canSnakeMoveInDirection(snake, snake.getDir())){
            snake.stepOnePos();
        } else {
            CharacterAction possibleDir = estimateFoeDirection(snake);
            if(possibleDir == null){
                snake.kill();
            } else {
                snake.setDir(possibleDir);
                snake.stepOnePos();
            }
        }
    }

    public boolean getIsKilledFoeState(){
        return isKilledFoeState;
    }

    private CharacterAction estimateFoeDirection(Snake foe){
        ArrayList<CharacterAction> possibleDirections = new ArrayList<>();
        for(CharacterAction dir : CharacterAction.values()){
            if(canSnakeMoveInDirection(foe, dir)){
                possibleDirections.add(dir);
            }
        }
        int possibleDirectionsNbr = possibleDirections.size();

        if (possibleDirectionsNbr > 1){
            return findTargetDirection(foe);
        } else if (possibleDirectionsNbr == 1){
            return possibleDirections.get(0);
        }
        return null;
    }

    private CharacterAction findTargetDirection(Snake foe){
        MapCoordinate playerHead = self.getHead();
        MapCoordinate foeHead = foe.getHead();
        CharacterAction foeDir = foe.getDir();
        if(foeDir.equals(CharacterAction.DOWN) ||foeDir.equals(CharacterAction.UP)){
            if(playerHead.x <= foeHead.x){
                return CharacterAction.LEFT;
            } else {
                return CharacterAction.RIGHT;
            }
        } else {
            if(playerHead.y <= foeHead.y){
                return CharacterAction.UP;
            } else {
                return CharacterAction.DOWN;
            }
        }
    }

    public boolean canIMoveInDirection(CharacterAction dir){
        return canSnakeMoveInDirection(self, dir);
    }

    private boolean isMoveOutOfBounds(Snake snake, CharacterAction dir){
        MapCoordinate head = snake.getHead();
        switch (dir){
            case LEFT:
                return (head.x - 1) < 0;
            case RIGHT:
                return (head.x + 2) > mapWidth;
            case DOWN:
                return (head.y + 2) > mapHeight;
            case UP:
                return (head.y - 1) < 0;
            default:
                return false;
        }
    }


    private boolean canSnakeMoveInDirection(Snake snake, CharacterAction dir){
        MapCoordinate snakeHead = snake.getHead();

        if(isMoveOutOfBounds(snake, dir)){
            return false;
        }

        HashSet<MapCoordinate>totalObstacleSet = getTotalSet();
        switch(dir){
            case LEFT:
                return !totalObstacleSet.contains(snakeHead.translateBy(-1, 0));
            case RIGHT:
                return !totalObstacleSet.contains(snakeHead.translateBy(1, 0));
            case DOWN:
                return !totalObstacleSet.contains(snakeHead.translateBy(0, 1));
            case UP:
                return !totalObstacleSet.contains(snakeHead.translateBy(0, -1));
            default:
                return false;
        }
    }

    //TODO: Generalize this for any snake?
    public int getOpenSpacesinDir(CharacterAction dir){
        visitedTiles = new HashSet<>();
        SnakeState futureState = createFutureState(dir);
        HashSet<MapCoordinate> obstacles = getTotalSet();
        HashSet<MapCoordinate> visitedTiles = new HashSet<>();
        visitedTiles.add(futureState.getSelf().getHead());
        return getOpenSpacesRec(futureState, futureState.getSelf().getHead(), obstacles);

    }


    private int getOpenSpacesRec(SnakeState state, MapCoordinate tile, HashSet<MapCoordinate> obstacleSet){
        MapCoordinate leftTile = tile.translateBy(-1, 0);
        MapCoordinate rightTile = tile.translateBy(1, 0);
        MapCoordinate upTile = tile.translateBy(0, -1);
        MapCoordinate downTile = tile.translateBy(0, 1);

        if(!isCoordinateOutOfBounds(leftTile) && !obstacleSet.contains(leftTile) && visitedTiles.add(leftTile)){
            getOpenSpacesRec(state, leftTile, obstacleSet);
        }
        if(!isCoordinateOutOfBounds(rightTile) && !obstacleSet.contains(rightTile) && visitedTiles.add(rightTile)){
            getOpenSpacesRec(state, rightTile, obstacleSet);
        }
        if(!isCoordinateOutOfBounds(upTile) && !obstacleSet.contains(upTile) && visitedTiles.add(upTile)){
            getOpenSpacesRec(state, upTile, obstacleSet);
        }
        if(!isCoordinateOutOfBounds(downTile) && !obstacleSet.contains(downTile) && visitedTiles.add(downTile)){
            getOpenSpacesRec(state, downTile, obstacleSet);
        }
        return visitedTiles.size();
    }


    public boolean isCoordinateOutOfBounds(MapCoordinate coordinate) {
        return coordinate.x < 0 || coordinate.x >= mapWidth || coordinate.y < 0 || coordinate.y >= mapHeight;
    }



    public HashSet<MapCoordinate> getTotalSet(){
        HashSet<MapCoordinate> totalSet = new HashSet<>();
        totalSet.addAll(obstacleSet);
        totalSet.addAll(self.getBodySet());

        Iterator<Snake> foeIT = foeSet.iterator();
        while(foeIT.hasNext()){
            Snake foe = foeIT.next();
            totalSet.addAll(foe.getBodySet());
        }

        return totalSet;
    }

    public int getMapHeight(){
        return mapHeight;
    }

    public int getMapWidth(){
        return mapWidth;
    }

    public Snake getSelf(){
        return self;
    }

    public Collection<Snake> getFoes() {
        return foeSet;
    }

}
