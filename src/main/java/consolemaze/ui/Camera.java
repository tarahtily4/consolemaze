package consolemaze.ui;

import consolemaze.model.Maze;
import consolemaze.model.Position;

final class Camera {

    private final int startX;
    private final int startY;
    private final int endX;
    private final int endY;

    private Camera(int startX, int startY, int endX, int endY) {

        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

    }

    static Camera centeredOn(Position target, Maze maze) {

        int startX = clamp(target.getX() - UiConfig.VISIBLE_COLUMNS / 2, 0, maze.getWidth() - UiConfig.VISIBLE_COLUMNS);
        int startY = clamp(target.getY() - UiConfig.VISIBLE_ROWS / 2, 0, maze.getHeight() - UiConfig.VISIBLE_ROWS);

        int endX = Math.min(startX + UiConfig.VISIBLE_COLUMNS, maze.getWidth());
        int endY = Math.min(startY + UiConfig.VISIBLE_ROWS, maze.getHeight());

        return new Camera(startX, startY, endX, endY);

    }

    int getStartX() { return startX; }
    int getStartY() { return startY; }
    int getEndX() { return endX; }
    int getEndY() { return endY; }

    boolean contains(Position pos) {

        return pos.getX() >= startX && pos.getX() < endX
                && pos.getY() >= startY && pos.getY() < endY;

    }

    int cellX(int worldX) {
        return (worldX - startX) * UiConfig.CHAR_WIDTH;
    }

    int cellY(int worldY) {
        return (worldY - startY) * UiConfig.CHAR_HEIGHT;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

}