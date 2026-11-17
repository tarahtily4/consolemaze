package consolemaze.model;
import java.util.HashMap;
import java.util.Map;

/**
 * The game map: a fixed-size grid holding at most one {@link GameObject} per cell.
 *
 * The grid stores only static content (walls, floor, doors, items, props).
 * Dynamic entities (the player and enemies) are kept by the engine and are not part of it.
 * Out-of-bounds access is safe: reads return {@code null}, writes are ignored.
 */

public class Maze {

    public static final int MIN_SIZE = 40;
    public static final int MAX_SIZE = 180;

    private final int width;
    private final int height;
    private final GameObject[][] grid;
    private final Map<MovableEntity, GameObject> objectsBeneath = new HashMap<>();

    public Maze (int width, int height) {

        if (!isValidSize(width) || !isValidSize(height)) {
            throw new IllegalArgumentException(
                    "Maze size must be between " + MIN_SIZE + " and " + MAX_SIZE
                            + ", got " + width + "x" + height);
        }

        this.width = width;
        this.height = height;

        this.grid = new GameObject[this.width][this.height];

    }

    /** The single place that defines the valid size range; use it to sanitize sizes before construction. */
    public static int clampSize(int size) {

        return Math.max(MIN_SIZE, Math.min(size, MAX_SIZE));

    }

    private static boolean isValidSize(int size) {

        return size >= MIN_SIZE && size <= MAX_SIZE;

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isInside(Position position) {

        return position.getX() >= 0 && position.getX() < width &&
                position.getY() >= 0 && position.getY() < height;

    }

    public GameObject getObjectAt(Position position) {

        if (!isInside(position)) return null;
        return grid[position.getX()][position.getY()];

    }

    /**
     * Stores the object in the cell and syncs the object's position with the cell
     * coordinates (see the position invariant in {@link GameObject}).
     * Passing {@code null} clears the cell.
     */

    public void setObjectAt(Position position, GameObject obj) {

        if (!isInside(position)) return;

        grid[position.getX()][position.getY()] = obj;

        if (obj != null) {
            obj.setPosition(position);
        }

    }


    public boolean isWalkable(Position position) {

        GameObject obj = getObjectAt(position);
        return obj != null && obj.getCollision().isWalkable();

    }

    public void moveEntity(MovableEntity entity, Position target) {

        if (!isInside(target)) return;

        Position from = entity.getPosition();
        if (from.equals(target)) return;

        GameObject beneathTarget = getObjectAt(target);
        GameObject restored = objectsBeneath.remove(entity);

        setObjectAt(from, restored != null ? restored : new Tile(from));

        if (beneathTarget != null && beneathTarget != entity) {
            objectsBeneath.put(entity, beneathTarget);
        }

        setObjectAt(target, entity);

    }

}