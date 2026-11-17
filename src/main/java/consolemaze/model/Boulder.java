package consolemaze.model;

/**
 * A pushable stone. It blocks the player, monkeys and traps (ghosts pass through it).
 *
 * Collision is (not walkable, trigger): the player cannot step onto it, but bumping
 * into it triggers a push, which is resolved by the engine. A boulder never leaves the
 * room it was spawned in, so it cannot end up inside a corridor.
 */

public class Boulder extends GameObject {

    // Solid, but bumping into it triggers the push interaction
    private static final Collision COLLISION = new Collision(false, true);

    // The room this boulder is bound to
    private final Room home;

    public Boulder(Position position, Room home) {

        super(position, '●', COLLISION);
        this.home = home;

    }

    public Room getHome() {

        return home;

    }


    /**
     * Static push rules: the target cell must lie inside the home room and contain empty
     * floor. Enemy occupancy is checked separately by the engine, because enemies are not
     * stored in the maze grid.
     */

    public boolean canBePushedTo(Maze maze, Position target) {

        return home.contains(target)
                && maze.getObjectAt(target) instanceof Tile;

    }

}