package consolemaze.model;

/**
 * A collectible coin. Walkable trigger: picked up by walking over it.
 */

public class Coin extends GameObject {

    private static final Collision COLLISION = new Collision(true, true);

    private final int value;

    public Coin(Position position, int value) {

        super(position, '⦿', COLLISION);
        this.value = value;

    }

    public int getValue() {

        return value;

    }

}