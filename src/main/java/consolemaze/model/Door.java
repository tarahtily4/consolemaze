package consolemaze.model;

/**
 * A locked door that is opened by a {@link Key} of the same {@link KeyColor}.
 *
 * <p>The door is the only static object that changes state. Closed, it is not walkable but
 * triggers an unlock attempt when the player bumps into it. Once opened it becomes an
 * ordinary walkable cell and stays on the map.
 */

public class Door extends GameObject {

    private static final char CLOSED_SYMBOL = '☲';
    private static final char OPEN_SYMBOL = '▯';

    // Closed: cannot be entered, but bumping into it triggers the unlock attempt
    private static final Collision CLOSED_COLLISION = new Collision(false, true);
    // Open: a plain walkable cell, no interaction needed anymore
    private static final Collision OPEN_COLLISION = new Collision(true, false);

    private final KeyColor keyColor;
    private boolean open = false;

    public Door(Position position, KeyColor keyColor) {

        super(position, CLOSED_SYMBOL, CLOSED_COLLISION);
        this.keyColor = keyColor;

    }

    public KeyColor getKeyColor() {

        return keyColor;

    }

    public boolean isOpen() {

        return open;

    }

    /** Opens the door: switches its symbol and collision (one-way, a door never closes again). */
    public void open() {

        this.open = true;
        setSymbol(OPEN_SYMBOL);
        setCollision(OPEN_COLLISION);

    }

}