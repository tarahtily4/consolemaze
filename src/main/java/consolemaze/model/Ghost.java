package consolemaze.model;

import java.util.Random;

/**
 * Slow ghost that drifts toward the player through walls and any other obstacle.
 * It only respects the map bounds. Deals 1 damage on contact.
 */

public class Ghost extends Enemy {

    private static final int DAMAGE = 1;
    private static final int MOVE_INTERVAL = 4;

    private final Random random = new Random();

    public Ghost(Position position) {

        super(position, 'Ω', DAMAGE, MOVE_INTERVAL);

    }

    /**
     * Moves one cell along a single axis toward the player. In roughly 25% of the steps
     * it wanders randomly instead, so the chase does not feel perfectly precise.
     */

    @Override
    protected void step(Maze maze, Player player) {

        Position p = getPosition();
        Position t = player.getPosition();

        int dx = Integer.signum(t.getX() - p.getX());
        int dy = Integer.signum(t.getY() - p.getY());

        if (random.nextInt(4) == 0) {
            dx = random.nextInt(3) - 1;
            dy = random.nextInt(3) - 1;
        }

        // orthogonal steps only
        if (dx != 0 && dy != 0) {
            if (random.nextBoolean()) dx = 0; else dy = 0;
        }

        Position next = new Position(p.getX() + dx, p.getY() + dy);

        if (maze.isInside(next)) {
            moveTo(next);
        }

    }

}