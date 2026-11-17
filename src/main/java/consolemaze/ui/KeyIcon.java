package consolemaze.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;

final class KeyIcon {

    static final int WIDTH = 17;
    static final int HEIGHT = 9;

    private KeyIcon() {
    }

    static void draw(Graphics2D g, int x, int y, Color color) {
        Stroke oldStroke = g.getStroke();
        Object oldAntialiasing = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g.setStroke(new BasicStroke(1.8f));

        g.drawOval(x, y, 8, 8);                    // головка
        g.drawLine(x + 8, y + 4, x + 16, y + 4);   // стержень
        g.drawLine(x + 12, y + 4, x + 12, y + 7);  // первый зубец
        g.drawLine(x + 15, y + 4, x + 15, y + 7);  // второй зубец

        g.setStroke(oldStroke);
        if (oldAntialiasing != null) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialiasing);
        }
    }
}