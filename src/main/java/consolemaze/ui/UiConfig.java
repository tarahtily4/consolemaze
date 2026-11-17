package consolemaze.ui;

import java.awt.Font;

final class UiConfig {

    private UiConfig() {
    }

    static final int CHAR_WIDTH = 9;
    static final int CHAR_HEIGHT = 16;
    static final int MARGIN_X = 15;

    static final int VIEWPORT_WIDTH = 60;
    static final int VIEWPORT_HEIGHT = 30;
    static final int FIELD_WIDTH_PX = VIEWPORT_WIDTH * CHAR_WIDTH;
    static final int FIELD_HEIGHT_PX = VIEWPORT_HEIGHT * CHAR_HEIGHT;

    static final double WORLD_ZOOM = 1.5;
    static final int VISIBLE_COLUMNS = (int) (VIEWPORT_WIDTH / WORLD_ZOOM);
    static final int VISIBLE_ROWS = (int) (VIEWPORT_HEIGHT / WORLD_ZOOM);

    static final int HUD_TEXT_Y = 25;
    static final int HUD_OFFSET_Y = 45;
    static final int WORLD_ORIGIN_X = MARGIN_X;
    static final int WORLD_ORIGIN_Y = HUD_OFFSET_Y - 13; // верх первой строки поля

    static final int GAME_TICK_MS = 120;
    static final int BLINK_PERIOD_MS = 150;

    static final Font FONT = new Font("Monospaced", Font.BOLD, 14);
    static final Font TITLE_FONT = new Font("Monospaced", Font.BOLD, 36);
}