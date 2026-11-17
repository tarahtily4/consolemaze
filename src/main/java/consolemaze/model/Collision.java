package consolemaze.model;

/**
 * Immutable description of how an object reacts to the player's movement.
 * Instances can be safely shared between objects.
 *
 *   walkable: the player may stand on the cell.
 *   trigger: entering (or bumping into) the cell starts an interaction
 *       handled by the engine (pickup, unlock, push...), even if the cell is not
 *       walkable, like a closed door.
 */

public class Collision {

    private final boolean isWalkable;
    private final boolean isTrigger;

    public Collision (boolean isWalkable, boolean isTrigger) {

        this.isWalkable = isWalkable;
        this.isTrigger = isTrigger;

    }

    public boolean isWalkable() {

        return isWalkable;

    }

    public boolean isTrigger() {

        return isTrigger;

    }

}