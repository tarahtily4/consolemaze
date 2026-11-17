package consolemaze.ui;

import consolemaze.model.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/** Экраны конца игры. */
final class OverlayRenderer {

    private static final String RESTART_PROMPT = "[R] Restart Level   |   [ESC] Exit Game";

    void renderGameOver(Graphics g, int panelWidth, int panelHeight) {
        int centerY = panelHeight / 2;

        drawCentered(g, "GAME OVER", UiConfig.TITLE_FONT, Color.RED, panelWidth, centerY);
        drawCentered(g, RESTART_PROMPT, UiConfig.FONT, Color.WHITE, panelWidth, centerY + 40);
    }

    void renderVictory(Graphics g, Player player, int panelWidth, int panelHeight) {
        int titleY = panelHeight / 2 - 40;
        String stats = "Coins collected: " + player.getInventory().size();

        drawCentered(g, "VICTORY!", UiConfig.TITLE_FONT, Color.GREEN, panelWidth, titleY);
        drawCentered(g, stats, UiConfig.FONT, Color.WHITE, panelWidth, titleY + 40);
        drawCentered(g, RESTART_PROMPT, UiConfig.FONT, Color.WHITE, panelWidth, titleY + 80);
    }

    private void drawCentered(Graphics g, String text, Font font, Color color, int panelWidth, int baselineY) {
        g.setFont(font);
        g.setColor(color);

        int x = (panelWidth - g.getFontMetrics().stringWidth(text)) / 2;
        g.drawString(text, x, baselineY);
    }
}