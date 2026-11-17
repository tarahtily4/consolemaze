package consolemaze.model;

/**
 * A collectible key. It opens the {@link Door} of the same {@link KeyColor} and is
 * consumed on use. Picked up by walking over it.
 */

public class Key extends GameObject {

    private static final Collision COLLISION = new Collision(true, true);

    private final KeyColor keyColor;

    public Key(Position position, KeyColor keyColor) {

        super(position, '⚿', COLLISION);
        this.keyColor = keyColor;

    }

    public KeyColor getKeyColor() {

        return keyColor;

    }

}