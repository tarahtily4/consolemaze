package consolemaze.ui;

import consolemaze.model.*;

import java.awt.Color;

final class ColorPalette {

    static final Color PLAYER = Color.YELLOW;
    static final Color PLAYER_HIT = Color.RED;

    private static final Color GHOST = new Color(190, 150, 255);
    private static final Color MONKEY = new Color(205, 133, 63);
    private static final Color TRAP = new Color(255, 60, 60);
    private static final Color FLOOR = new Color(40, 40, 40);
    private static final Color BLUE_KEY = new Color(80, 130, 255);

    private static final Color HEALTH = new Color(255, 105, 180);
    private static final Color BOULDER = new Color(150, 120, 90);
    private static final Color VEGETATION = new Color(60, 180, 75);



    static Color forObject(GameObject obj) {
        if (obj instanceof Player) return PLAYER;
        if (obj instanceof Ghost) return GHOST;
        if (obj instanceof Monkey) return MONKEY;
        if (obj instanceof Trap) return TRAP;
        if (obj instanceof Coin) return Color.ORANGE;
        if (obj instanceof HealthPotion) return HEALTH;
        if (obj instanceof Boulder) return BOULDER;
        if (obj instanceof Vegetation) return VEGETATION;
        if (obj instanceof Exit) return Color.GREEN;
        if (obj instanceof Key) return forKeyColor(((Key) obj).getKeyColor());
        if (obj instanceof Door) return doorColor((Door) obj);
        if (obj instanceof Wall) return Color.GRAY;
        if (obj instanceof Tile) return FLOOR;
        return Color.WHITE;
    }

    static Color forKeyColor(KeyColor color) {
        switch (color) {
            case RED:    return Color.RED;
            case BLUE:   return BLUE_KEY;
            case GREEN:  return Color.GREEN;
            case YELLOW: return Color.YELLOW;
            case PURPLE: return Color.MAGENTA;
            case ORANGE: return Color.ORANGE;
            case CYAN:   return Color.CYAN;
            default:     return Color.WHITE;
        }
    }

    private static Color doorColor(Door door) {
        Color base = forKeyColor(door.getKeyColor());
        return door.isOpen() ? base.darker() : base;
    }
}