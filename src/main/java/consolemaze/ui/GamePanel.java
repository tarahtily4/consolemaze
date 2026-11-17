package consolemaze.ui;

import consolemaze.engine.MazeEngine;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * The single Swing panel of the game. It does no drawing itself: it only decides what to
 * show (HUD plus either the world or an end-of-game screen) and delegates to the renderers.
 */

public class GamePanel extends JPanel {

    private final HudRenderer hudRenderer = new HudRenderer();
    private final WorldRenderer worldRenderer = new WorldRenderer();
    private final OverlayRenderer overlayRenderer = new OverlayRenderer();

    private MazeEngine engine;

    public GamePanel(MazeEngine engine) {

        this.engine = engine;

        setBackground(Color.BLACK);
        setPreferredSize(calculatePreferredSize());
        setFocusable(true);

    }

    public void setEngine(MazeEngine engine) {

        this.engine = engine;
        repaint();

    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        hudRenderer.render(g, engine.getPlayer());

        if (engine.isGameOver()) {
            overlayRenderer.renderGameOver(g, getWidth(), getHeight());
        } else if (engine.isGameWon()) {
            overlayRenderer.renderVictory(g, engine.getPlayer(), getWidth(), getHeight());
        } else {
            worldRenderer.render(g, engine);
        }

    }

    private Dimension calculatePreferredSize() {

        int width = UiConfig.VIEWPORT_WIDTH * UiConfig.CHAR_WIDTH + 2 * UiConfig.MARGIN_X;
        int height = UiConfig.VIEWPORT_HEIGHT * UiConfig.CHAR_HEIGHT + UiConfig.HUD_OFFSET_Y + 20;
        return new Dimension(width, height);

    }
}