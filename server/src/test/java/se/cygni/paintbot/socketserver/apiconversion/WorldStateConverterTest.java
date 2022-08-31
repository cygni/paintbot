package se.cygni.paintbot.socketserver.apiconversion;

import org.junit.Ignore;
import org.junit.Test;
import se.cygni.game.WorldState;
import se.cygni.game.testutil.PaintbotTestUtil;
import se.cygni.game.worldobject.Empty;
import se.cygni.game.worldobject.Obstacle;
import se.cygni.game.worldobject.PowerUp;
import se.cygni.game.worldobject.WorldObject;
import se.cygni.paintbot.api.GameMessageParser;
import se.cygni.paintbot.api.event.MapUpdateEvent;
import se.cygni.paintbot.api.model.Map;
import se.cygni.paintbot.api.model.MapEmpty;
import se.cygni.paintbot.api.model.MapObstacle;
import se.cygni.paintbot.api.model.MapPowerUp;
import se.cygni.paintbot.socketserver.mapper.WorldStateConverter;

import java.util.HashSet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class WorldStateConverterTest {

    @Test
    public void testConvertWorldStateWithObstacle() throws Exception {
        testConversionWithType(Obstacle.class);
    }

    @Test
    public void testConvertWorldStateWithFood() throws Exception {
        testConversionWithType(PowerUp.class);
    }

    @Test
    @Ignore
    public void testPrintCoordinatePosition() {
        int size = 15;
        int counter = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                System.out.printf("%03d ", counter++);
            }
            System.out.println("\n");
        }
    }

    private <T extends WorldObject> void testConversionWithType(Class<T> clazz) throws Exception {
        WorldState ws = new WorldState(3, 4);

        WorldObject worldObject = PaintbotTestUtil.createWorldObject(clazz);

        // Obstacle at 1,1
        ws = PaintbotTestUtil.replaceWorldObjectAt(ws, worldObject, 4);

        WorldStateConverter converter = new WorldStateConverter();
        Map map = converter.convertWorldState(ws, 1, new HashSet<>());

        MapUpdateEvent mue = new MapUpdateEvent(0, "id", map);

        // Make sure serialisation works
        String mapUpdateStr = GameMessageParser.encodeMessage(mue);

        // Make sure deserialisation works
        MapUpdateEvent mueReparsed = (MapUpdateEvent) GameMessageParser.decodeMessage(mapUpdateStr);
        Map reparsedMap = mueReparsed.getMap();

        // Assert values
        assertEquals(3, reparsedMap.getWidth());
        assertEquals(4, reparsedMap.getHeight());

        Class mapType = getCorrespondingMapType(worldObject);

        if (clazz == Obstacle.class) {
            assertArrayEquals(new int[]{4}, reparsedMap.getObstaclePositions());
        } else {
            assertArrayEquals(new int[]{4}, reparsedMap.getPowerUpPositions());

        }

        // No paintbotinfo
        assertEquals(0, map.getCharacterInfos().length);
    }

    private Class getCorrespondingMapType(WorldObject obj) {
        if (obj instanceof Obstacle)
            return MapObstacle.class;

        if (obj instanceof PowerUp)
            return MapPowerUp.class;

        if (obj instanceof Empty)
            return MapEmpty.class;

        throw new IllegalArgumentException(obj.getClass() + " is not a known type");
    }

}