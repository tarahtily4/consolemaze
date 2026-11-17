package consolemaze.model;

/**
 * Empty walkable floor. Also the "nothing here" filler that replaces an object
 * when it disappears (picked up, broken or pushed away).
 */

public class Tile extends GameObject {

    private static final Collision COLLISION = new Collision(true, false);

    public Tile(Position position) {

        super(position, '.', COLLISION);

    }

}