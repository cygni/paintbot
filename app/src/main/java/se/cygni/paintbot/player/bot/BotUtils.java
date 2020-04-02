package se.cygni.paintbot.player.bot;

import se.cygni.paintbot.client.MapCoordinate;
import se.cygni.paintbot.client.MapUtilityImpl;

final class BotUtils {

    private BotUtils() {}

    static MapCoordinate findClosestPowerUp(MapUtilityImpl mapUtil) {
        MapCoordinate closestPowerUp = null;

        int closestPowerUpDistance = Integer.MAX_VALUE;
        MapCoordinate myPosition = mapUtil.getMyPosition();

        for (MapCoordinate mc : mapUtil.listCoordinatesContainingPowerUps()) {
            if (closestPowerUp == null) {
                closestPowerUp = mc;
                closestPowerUpDistance = closestPowerUp.getManhattanDistanceTo(myPosition);
            } else {
                int distance = myPosition.getManhattanDistanceTo(mc);
                if (distance < closestPowerUpDistance) {
                    closestPowerUp = mc;
                    closestPowerUpDistance = distance;
                }
            }

        }

        return closestPowerUp;
    }

}
