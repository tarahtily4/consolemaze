package consolemaze.model;

/**
 * A solid wall: blocks movement and has no interaction.
 */

public class Wall extends GameObject {

    private static final Collision COLLISION = new Collision(false, false);

    public Wall(Position position) {

        super(position, '■', COLLISION);

    }

}