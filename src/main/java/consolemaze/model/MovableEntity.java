package consolemaze.model;

/**
 * Base class for entities that change position at runtime: the {@link Player} and all
 * {@link Enemy} types. They live outside the {@link Maze} grid, so moving them only
 * means updating their own position.
 */

public abstract class MovableEntity extends GameObject {

    public MovableEntity(Position position, char symbol, Collision collision) {

        super(position, symbol, collision);

    }

    public void moveTo(Position newPosition) {

        setPosition(newPosition);

    }

}