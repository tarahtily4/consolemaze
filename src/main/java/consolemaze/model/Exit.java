package consolemaze.model;

/**
 * The level exit. Stepping onto it wins the game (the win itself is handled by the engine).
 */

public class Exit extends GameObject {

    private static final Collision COLLISION = new Collision(true, true);

    public Exit(Position position) {

        super(position, '▣', COLLISION);

    }

}