package consolemaze.model;

/**
 * A trap block that sweeps back and forth along one axis of a room, usually in front of
 * a corridor entrance. Deals 3 damage on contact.
 *
 * <p>It moves between {@code min} and {@code max} (coordinates along its axis) and is
 * stopped by any solid obstacle (boulders, vegetation), which lets the player restrict
 * its movement. If it is blocked on both sides it simply stays in place.
 */

public class Trap extends Enemy {

    private static final int DAMAGE = 3;
    private static final int MOVE_INTERVAL = 2;

    private final Axis axis;
    private final int min;
    private final int max;
    private int dir = 1;

    public Trap(Position start, Axis axis, int min, int max) {

        super(start, '▦', DAMAGE, MOVE_INTERVAL);
        this.axis = axis;
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);

    }

    public Axis getAxis() {
        return axis;
    }

    @Override
    protected void step(Maze maze, Player player) {

        Position current = getPosition();
        int cur = coordinateOf(current);

        int next = cur + dir;
        if (!canMoveTo(maze, current, next)) {

            // Bounce: try the opposite direction; if that is blocked too, stay put
            dir = -dir;
            next = cur + dir;
            if (!canMoveTo(maze, current, next)) return;

        }

        moveTo(withCoordinate(current, next));

    }

    private boolean canMoveTo(Maze maze, Position current, int value) {

        if (value < min || value > max) return false;
        return maze.isWalkable(withCoordinate(current, value));

    }

    private int coordinateOf(Position pos) {
        return axis == Axis.HORIZONTAL ? pos.getX() : pos.getY();
    }

    private Position withCoordinate(Position pos, int value) {

        return axis == Axis.HORIZONTAL
                ? new Position(value, pos.getY())
                : new Position(pos.getX(), value);

    }

}