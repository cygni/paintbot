package se.cygni.snake.player.bot.snakey;

import se.cygni.snake.api.model.CharacterAction;

import java.util.*;

/**
 * Created by trivo on 2017-03-31.
 */
public class BonusHandler {



    private Map<CharacterAction, BonusTracker> bonusMap;

    public BonusHandler(){
        bonusMap = new EnumMap<>(CharacterAction.class);
    }

    public BonusTracker addBonusTracker(CharacterAction dir){
        BonusTracker bt = new BonusTracker();
        bonusMap.put(dir, bt);
        return bt;
    }

    public CharacterAction getBestBonus(){
        Collection<CharacterAction> directions = bonusMap.keySet();

        int maxValue = Integer.MIN_VALUE;
        CharacterAction bestBonus = CharacterAction.DOWN;

        for(CharacterAction dir : directions){
            BonusTracker bt = bonusMap.get(dir);
            int bonusValue = bt.getFoodOnPath() - bt.getNearCollisions();
            if(bonusValue > maxValue){
                maxValue = bonusValue;
                bestBonus = dir;
            }
        }
        return bestBonus;
    }

    public int getBonus(CharacterAction dir){
        BonusTracker bt = bonusMap.get(dir);
        return bt.getFoodOnPath()  + bt.getKillBonus() + bt.getMiddleBonus() + bt.getFreeHeadSpaces();
    }




}
