package consolemaze.ui;

import consolemaze.engine.MazeEngine;
import consolemaze.model.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

/**
 * Draws the visible part of the map: cells first, then enemies, then the player (always on top).
 * The whole field is drawn in a zoomed coordinate system; the HUD is not affected by the zoom.
 */
final class WorldRenderer {

    private int ascent;
    void render(Graphics g, MazeEngine engine) {
        Maze maze = engine.getMaze();
        Player player = engine.getPlayer();
        Camera camera = Camera.centeredOn(player.getPosition(), maze);

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            configureWorldGraphics(g2);
            ascent = g2.getFontMetrics().getAscent();

            drawTiles(g2, maze, camera);
            drawEnemies(g2, engine.getEnemies(), camera);
            drawPlayer(g2, player, camera);
        } finally {
            g2.dispose();
        }
    }

    private void configureWorldGraphics(Graphics2D g2) {

        g2.setClip(UiConfig.WORLD_ORIGIN_X, UiConfig.WORLD_ORIGIN_Y,
                UiConfig.FIELD_WIDTH_PX, UiConfig.FIELD_HEIGHT_PX);
        g2.translate(UiConfig.WORLD_ORIGIN_X, UiConfig.WORLD_ORIGIN_Y);
        g2.scale(UiConfig.WORLD_ZOOM, UiConfig.WORLD_ZOOM);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setFont(UiConfig.FONT);

    }

    private void drawTiles(Graphics2D g, Maze maze, Camera camera) {

        for (int y = camera.getStartY(); y < camera.getEndY(); y++) {

            for (int x = camera.getStartX(); x < camera.getEndX(); x++) {

                Position pos = new Position(x, y);
                GameObject obj = maze.getObjectAt(pos);
                if (obj == null) continue;

                drawGlyph(g, camera, pos, obj.getSymbol(), ColorPalette.forObject(obj));

            }
        }

    }

    private void drawEnemies(Graphics2D g, List<Enemy> enemies, Camera camera) {

        for (Enemy enemy : enemies) {
            Position pos = enemy.getPosition();
            if (camera.contains(pos)) {
                drawSprite(g, camera, pos, enemy.getSymbol(), ColorPalette.forObject(enemy));
            }

        }

    }

    private void drawPlayer(Graphics2D g, Player player, Camera camera) {

        Color color = isHitFlashFrame(player) ? ColorPalette.PLAYER_HIT : ColorPalette.PLAYER;
        drawSprite(g, camera, player.getPosition(), player.getSymbol(), color);

    }

    private boolean isHitFlashFrame(Player player) {

        return player.isInvulnerable()
                && (System.currentTimeMillis() / UiConfig.BLINK_PERIOD_MS) % 2 == 0;

    }

    private void drawGlyph(Graphics2D g, Camera camera, Position pos, char symbol, Color color) {

        g.setColor(color);
        g.drawString(String.valueOf(symbol),
                camera.cellX(pos.getX()),
                camera.cellY(pos.getY()) + ascent);

    }

    private void drawSprite(Graphics2D g, Camera camera, Position pos, char symbol, Color color) {

        g.setColor(Color.BLACK);
        g.fillRect(camera.cellX(pos.getX()), camera.cellY(pos.getY()),
                UiConfig.CHAR_WIDTH, UiConfig.CHAR_HEIGHT);

        drawGlyph(g, camera, pos, symbol, color);

    }

}