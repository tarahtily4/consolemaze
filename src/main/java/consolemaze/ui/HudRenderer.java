package consolemaze.ui;

import consolemaze.model.Key;
import consolemaze.model.Player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

final class HudRenderer {

    private static final int LABEL_GAP = 8;
    private static final int ICON_GAP = 6;

    void render(Graphics g, Player player) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(UiConfig.FONT);
        g2.setColor(Color.WHITE);

        String stats = "HP: " + player.getHealth() + "/" + player.getMaxHealth()
                + " | Coins: " + player.getInventory().size()
                + " | Keys:";
        g2.drawString(stats, UiConfig.MARGIN_X, UiConfig.HUD_TEXT_Y);

        int keysStartX = UiConfig.MARGIN_X + g2.getFontMetrics().stringWidth(stats) + LABEL_GAP;
        drawKeys(g2, player, keysStartX);
    }

    private void drawKeys(Graphics2D g, Player player, int startX) {
        if (player.getKeys().isEmpty()) {
            g.drawString("None", startX, UiConfig.HUD_TEXT_Y);
            return;
        }

        int iconY = UiConfig.HUD_TEXT_Y - KeyIcon.HEIGHT;
        int x = startX;

        for (Key key : player.getKeys()) {
            KeyIcon.draw(g, x, iconY, ColorPalette.forKeyColor(key.getKeyColor()));
            x += KeyIcon.WIDTH + ICON_GAP;
        }
    }
}