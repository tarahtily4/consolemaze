package consolemaze.model;

/**
 * Immutable 2D grid coordinate with value semantics ({@code equals}/{@code hashCode}),
 * so it can safely be used as a key in maps and sets.
 */

import java.util.Objects;

public class Position {

    private final int x;
    private final int y;

    public Position (int x, int y) {

        this.x = x;
        this.y = y;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
