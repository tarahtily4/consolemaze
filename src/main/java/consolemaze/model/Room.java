package consolemaze.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A rectangular room produced by the map generator. Besides its geometry it keeps
 * a graph of connected neighbour rooms, which the spawners use for path-based logic
 * (e.g. choosing where doors go).
 */

public class Room {

    private final Position position;
    private final int width;
    private final int height;
    private final List<Position> tiles;
    private final List<Room> connectedRooms = new ArrayList<>();

    public Room(Position position, int width, int height) {

        this.position = position;
        this.width = width;
        this.height = height;
        this.tiles = new ArrayList<>();

        for (int dx = 0; dx < width; dx++) {
            for (int dy = 0; dy < height; dy++) {
                tiles.add(new Position(position.getX() + dx, position.getY() + dy));
            }
        }

    }

    public Position getPosition() {
        return position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Position getCenter() {

        return new Position(position.getX() + width / 2, position.getY() + height / 2);

    }

    public List<Position> getTiles() {
        return Collections.unmodifiableList(tiles);
    }

    /**
     * Axis-aligned overlap test that treats every room as if it were {@code margin}
     * cells larger on the left/top. Used to keep generated rooms apart.
     */

    public boolean intersects(Room other, int margin) {

        int thisX = this.position.getX();
        int thisY = this.position.getY();

        int otherX = other.getPosition().getX();
        int otherY = other.getPosition().getY();

        return thisX - margin < otherX + other.width &&
                thisX + this.width + margin > otherX &&
                thisY - margin < otherY + other.height &&
                thisY + this.height + margin > otherY;

    }

    public void connect(Room other) {

        if (!connectedRooms.contains(other)) {
            connectedRooms.add(other);
        }
        if (!other.connectedRooms.contains(this)) {
            other.connectedRooms.add(this);
        }

    }

    public List<Room> getConnectedRooms() {
        return Collections.unmodifiableList(connectedRooms);
    }

    public boolean contains(Position pos) {

        return pos.getX() >= position.getX() && pos.getX() < position.getX() + width
                && pos.getY() >= position.getY() && pos.getY() < position.getY() + height;

    }

}