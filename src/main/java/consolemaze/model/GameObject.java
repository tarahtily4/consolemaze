package consolemaze.model;

/**
 * Root of the model hierarchy: anything that has a position, a display symbol
 * and a {@link Collision}.
 *
 * Position invariant: for objects stored in a {@link Maze}, {@code getPosition()}
 * always equals the coordinates of the cell that holds the object. {@code Maze.setObjectAt}
 * keeps it in sync, which is why {@code setPosition} is package-private.
 * Subclasses can change their look and behaviour at runtime (e.g. an opened door)
 * through the protected setters.
 */

public abstract class GameObject {

    private Position position;
    private char symbol;
    private Collision collision;

    public GameObject(Position position, char symbol, Collision collision) {

        this.position = position;
        this.symbol = symbol;
        this.collision = collision;

    }

    //getters

    public Position getPosition() {

        return position;

    }

    public int getX() {

        return position.getX();

    }

    public int getY() {

        return position.getY();

    }

    public char getSymbol() {

        return symbol;

    }

    public Collision getCollision() {

        return collision;

    }

    //state changes for subclasses

    protected void setSymbol(char symbol) {

        this.symbol = symbol;

    }

    protected void setCollision(Collision collision) {

        this.collision = collision;

    }


    void setPosition(Position position) {

        this.position = position;

    }

}