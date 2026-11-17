package consolemaze.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A monkey that patrols back and forth along a fixed path. Deals 2 damage on contact.
 *
 * The path is computed by the spawner (a walkable route between two rooms) and the
 * monkey simply walks it end to end, turning around at the ends or when something solid
 * blocks the way. Vegetation is the exception: the monkey breaks it, which costs one move.
 */

public class Monkey extends Enemy {

    private static final int DAMAGE = 2;
    private static final int MOVE_INTERVAL = 3;

    private final List<Position> path;
    private int index = 0; // current position on the path
    private int dir = 1;   // walking direction along the path: +1 forward, -1 backward

    public Monkey(List<Position> path) {

        super(firstOf(path), '♣', DAMAGE, MOVE_INTERVAL);
        this.path = new ArrayList<>(path);

    }

    /** Validates the path and returns its start. Needed because the super constructor call must come first. */
    private static Position firstOf(List<Position> path) {

        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Monkey path must not be empty");
        }
        return path.get(0);

    }

    public List<Position> getPath() {
        return new ArrayList<>(path);
    }

    @Override
    protected void step(Maze maze, Player player) {

        if (path.size() < 2) return;

        int next = index + dir;
        if (next < 0 || next >= path.size()) {
            dir = -dir;
            next = index + dir;
        }
        if (next < 0 || next >= path.size()) return;

        Position target = path.get(next);

        if (!maze.isWalkable(target)) {
            if (maze.getObjectAt(target) instanceof Vegetation) {
                maze.setObjectAt(target, new Tile(target));
            } else {
                dir = -dir;
            }
            return;
        }

        index = next;
        moveTo(target);

    }

}