package se.cygni.paintbot.client;

import org.apache.commons.lang3.StringUtils;
import se.cygni.paintbot.api.event.MapUpdateEvent;
import se.cygni.paintbot.api.model.CharacterInfo;
import se.cygni.paintbot.api.model.Map;
import se.cygni.paintbot.api.model.MapCharacter;
import se.cygni.paintbot.api.model.MapObstacle;
import se.cygni.paintbot.api.model.MapPaintbotBody;
import se.cygni.paintbot.api.model.MapPowerUp;
import se.cygni.paintbot.api.model.TileContent;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

public class AnsiPrinter {
    private static final String EMPTY = "  ";
    private static final String FOOD = "██";
    private static final String OBSTACLE = "██";
    private static final String SNAKE_PART = "██";

    private static final String FG_DEFAULT = "[39m";
    private static final String FG_BLACK = "[30m";
    private static final String FG_RED = "[31m";
    private static final String FG_GREEN = "[32m";
    private static final String FG_YELLOW = "[33m";
    private static final String FG_BLUE = "[34m";
    private static final String FG_MAGENTA = "[35m";
    private static final String FG_CYAN = "[36m";
    private static final String FG_LIGHT_GRAY = "[37m";
    private static final String FG_DARK_GRAY = "[90m";
    private static final String FG_LIGHT_RED = "[91m";
    private static final String FG_LIGHT_GREEN = "[92m";
    private static final String FG_LIGHT_YELLOW = "[93m";
    private static final String FG_LIGHT_BLUE = "[94m";
    private static final String FG_LIGHT_MAGENTA = "[95m";
    private static final String FG_LIGHT_CYAN = "[96m";
    private static final String FG_WHITE = "[97m";

    private static final String BG_DEFAULT = "[49m";
    private static final String BG_LIGHT_GRAY = "[47m";

    private static final String SNAKE_HEAD_COLOR = "[33m"; // FG_YELLOW;
    private static final String FOOD_COLOR = "[32m"; // FG_GREEN;
    private static final String OBSTACLE_COLOR = "[30m"; // FG_BLACK;

    private final boolean active;
    private final boolean includeLegend;
    
    private HashMap<String, String> playerColorMap = new HashMap<>();

    public AnsiPrinter(final boolean includeLegend) {
        this.active = true;
        this.includeLegend = includeLegend;
    }

    public AnsiPrinter(final boolean active, final boolean includeLegend) {
        this.active = active;
        this.includeLegend = includeLegend;
    }

    public void printMap(MapUpdateEvent event) {
        if (active) {
            CompletableFuture cf = CompletableFuture.runAsync(() -> {

                if (event.getMap().getCharacterInfos().length > event.getMap().getHeight() - 5) {
                    System.out.println("Sorry, too many paintbots I can't render this.");
                    return;
                }

                if (event.getMap().getCharacterInfos().length > 11) {
                    System.out.println("Sorry, too many paintbots I can't render this.");
                    return;
                }

                if (event.getMap().getWidth() > 120) {
                    System.out.println("Sorry, the map is too wide, I can't render this.");
                    return;
                }

                if (event.getMap().getHeight() > 120) {
                    System.out.println("Sorry, the map is too high, I can't render this.");
                    return;
                }

                populatePaintbotColors(event);
                printMapActual(event);
            });
        }
    }

    private void printMapActual(MapUpdateEvent event) {

        Map map = event.getMap();

        int width = map.getWidth();
        int height = map.getHeight();

        StringBuilder sb = new StringBuilder();

        sb.append("Game id: ").append(event.getGameId())
                .append("\n")
                .append("Game tick: ").append(event.getGameTick())
                .append("\n\n");

        MapUtilityImpl mapUtil = new MapUtilityImpl(map, "notused");

        for (int y = 0; y < height; y++) {
            TileContent[] row = new TileContent[width];
            for (int x = 0; x < width; x++) {
                row[x] = mapUtil.getTileAt(new MapCoordinate(x, y));
            }
            printRow(row, event, sb);
            appendLegendForRow(y, map, sb);
        }

        System.out.println(sb);
    }

    private Queue<String> getAvailableColors() {
        Queue<String> availableColors = new ArrayDeque<>();
        availableColors.add(FG_LIGHT_YELLOW);
        availableColors.add(FG_LIGHT_BLUE);
        availableColors.add(FG_LIGHT_CYAN);
        availableColors.add(FG_LIGHT_RED);
        availableColors.add(FG_LIGHT_MAGENTA);
        availableColors.add(FG_BLUE);
        availableColors.add(FG_CYAN);
        availableColors.add(FG_MAGENTA);
        availableColors.add(FG_WHITE);
        availableColors.add(FG_RED);
        return availableColors;
    }

    private void populatePaintbotColors(MapUpdateEvent event) {
        Queue<String> availableColors = getAvailableColors();

        for (CharacterInfo characterInfo : event.getMap().getCharacterInfos()) {
            if (!playerColorMap.containsKey(characterInfo.getId())) {

                if (characterInfo.getId().equals(event.getReceivingPlayerId())) {
                    playerColorMap.put(characterInfo.getId(), FG_LIGHT_GREEN);
                } else {
                    playerColorMap.put(characterInfo.getId(), availableColors.remove());
                }
            }
        }
    }

    private void appendLegendForRow(int row, Map map, StringBuilder sb) {
        String indent = "    ";
        int offset = getOffsetForLegend(map);
        int noofStdItems = 4;

        int noofPlayers = map.getCharacterInfos().length;

        if (!includeLegend || row < offset || row - offset > noofPlayers + noofStdItems) {
            sb.append("\n");
            return;
        }

        if (row == offset) {
            sb.append("   LEGEND:\n");
            return;
        }

        int stdOffset = offset + noofPlayers;

        if (row > stdOffset) {
            int k = row - offset - noofPlayers - 1;
            switch (k) {
                case 1:
                    sb.append(indent)
                            .append((char) 27).append(SNAKE_HEAD_COLOR).append(SNAKE_PART)
                            .append((char) 27).append(FG_DEFAULT)
                            .append(" ").append("Paintbot head")
                            .append("\n");
                    return;
                case 2:
                    sb.append(indent)
                            .append((char) 27).append(FOOD_COLOR).append(FOOD)
                            .append((char) 27).append(FG_DEFAULT)
                            .append(" ").append("Food")
                            .append("\n");
                    return;
                case 3:
                    sb.append(indent)
                            .append((char) 27).append(OBSTACLE_COLOR).append(OBSTACLE)
                            .append((char) 27).append(FG_DEFAULT)
                            .append(" ").append("Obstacle")
                            .append("\n");
                    return;
            }
            sb.append("\n");
            return;
        }

        CharacterInfo si = map.getCharacterInfos()[row - offset - 1];
        sb.append(indent)
                .append((char) 27).append(getPlayerColour(si.getId())).append(SNAKE_PART)
                .append((char) 27).append(FG_DEFAULT)
                .append(" ").append(si.getName())
                .append(", p:").append(si.getPoints()).append(")")
                .append(" id: ").append(si.getId())
                .append("\n");

    }

    private int getOffsetForLegend(Map map) {
        return 2;
    }

    private String getPlayerColour(String id) {
        return playerColorMap.get(id);
    }

    private void printRow(TileContent[] row, MapUpdateEvent event, StringBuilder sb) {
        String bg_color = BG_LIGHT_GRAY;
        String bg_default = BG_DEFAULT;

        for (TileContent tc : row) {
            if (tc instanceof MapPaintbotBody)
                append(
                        getPlayerColour(((MapPaintbotBody) tc)
                                .getPlayerId()),
                        bg_color,
                        SNAKE_PART,
                        FG_DEFAULT,
                        bg_default, sb);

            else if (tc instanceof MapCharacter) {
                    append(
                            getPlayerColour(((MapCharacter) tc)
                                    .getPlayerId()),
                            bg_color,
                            SNAKE_PART,
                            FG_DEFAULT,
                            bg_default, sb);
            } else if (tc instanceof MapPowerUp)
                append(
                        FOOD_COLOR,
                        bg_color,
                        FOOD,
                        FG_DEFAULT,
                        bg_default, sb);
            else if (tc instanceof MapObstacle)
                append(
                        OBSTACLE_COLOR,
                        bg_color,
                        OBSTACLE,
                        FG_DEFAULT,
                        bg_default, sb);
            else
                append(
                        BG_LIGHT_GRAY,
                        bg_color,
                        EMPTY,
                        FG_DEFAULT,
                        bg_default, sb);
        }
    }


    private void append(
            String fgcolor,
            String bgcolor,
            String text,
            String resetFgColor,
            String resetBgColor,
            StringBuilder sb) {

        if (StringUtils.isNotEmpty(fgcolor))
            sb.append((char) 27).append(fgcolor);

        if (StringUtils.isNotEmpty(bgcolor))
            sb.append((char) 27).append(bgcolor);

        sb.append(text);

        if (StringUtils.isNotEmpty(resetFgColor))
            sb.append((char) 27).append(resetFgColor);

        if (StringUtils.isNotEmpty(resetBgColor))
            sb.append((char) 27).append(resetBgColor);
    }
}