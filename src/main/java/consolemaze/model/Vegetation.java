package consolemaze.model;

/**
 * A plant that blocks movement until it is broken. The player breaks it just by walking
 * into it, monkeys break it at the cost of one move, traps treat it as a wall and ghosts
 * ignore it. Breaking is handled by the engine  {@link Monkey}, which replace it with a {@link Tile}.
 */

public class Vegetation extends GameObject {

    private static final Collision COLLISION = new Collision(false, true);

    public Vegetation(Position position) {

        super(position, 'ψ', COLLISION);

    }

}