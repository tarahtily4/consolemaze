package consolemaze.engine.generator;

import consolemaze.engine.LevelData;
import consolemaze.model.*;

import java.util.*;

/**
 * Level generator based on random rooms and corridors.
 *
 * Despite the name it is not a true binary space partitioning algorithm: it scatters
 * non-overlapping rooms at random, links them into a graph and digs L-shaped one-tile-wide
 * corridors between connected rooms. (The class name is kept for historical reasons.)
 *
 * <p>Pipeline: fill the map with walls, place rooms, connect them, carve rooms and corridors,
 * then hand the finished map to {@link EntitySpawner} to populate it.
 */

public class BspMazeGenerator implements MapGenerator {

    private final Random random = new Random();
    private final EntitySpawner entitySpawner = new EntitySpawner();

    @Override
    public LevelData generate(int width, int height) {

        GeneratorConfig config = new GeneratorConfig(width, height);
        Maze maze = new Maze(config.getWidth(), config.getHeight());

        fillWithWalls(maze, config);
        List<Room> rooms = generateRooms(config);

        connectRooms(rooms);

        for (Room room : rooms) {
            carveRoom(maze, room);
        }
        carveCorridors(maze, rooms);

        return entitySpawner.populateLevel(maze, rooms, config);

    }

    private void fillWithWalls(Maze maze, GeneratorConfig config) {

        for (int x = 0; x < config.getWidth(); x++) {

            for (int y = 0; y < config.getHeight(); y++) {

                Position pos = new Position(x, y);
                maze.setObjectAt(pos, new Wall(pos));

            }

        }

    }

    /**
     * Tries to place up to {@code maxRooms} rooms at random positions (100 attempts in total),
     * rejecting any that come too close to an existing room. Rooms keep one cell away from the map border.
     * If nothing fits, two fixed rooms in opposite corners are used as a fallback.
     */

    private List<Room> generateRooms(GeneratorConfig config) {

        List<Room> rooms = new ArrayList<>();
        int attempts = 100;

        for (int i = 0; i < attempts && rooms.size() < config.getMaxRooms(); i++) {

            int rWidth = random.nextInt(config.getMaxRoomSize() - config.getMinRoomSize() + 1) + config.getMinRoomSize();
            int rHeight = random.nextInt(config.getMaxRoomSize() - config.getMinRoomSize() + 1) + config.getMinRoomSize();

            int rx = random.nextInt(config.getWidth() - rWidth - 2) + 1;
            int ry = random.nextInt(config.getHeight() - rHeight - 2) + 1;

            Room newRoom = new Room(new Position(rx, ry), rWidth, rHeight);

            boolean overlap = false;

            for (Room existing : rooms) {

                if (newRoom.intersects(existing, config.getRoomMargin())) {

                    overlap = true;
                    break;

                }

            }

            if (!overlap) {
                rooms.add(newRoom);
            }
        }

        if (rooms.isEmpty()) {

            rooms.add(new Room(new Position(2, 2), 10, 8));
            rooms.add(new Room(new Position(config.getWidth() - 12, config.getHeight() - 10), 10, 8));

        }

        return rooms;

    }

    /**
     * Builds the room graph. First every room is linked to the next one in the list, which
     * guarantees that the whole level is connected. Then each room also gets a link to its
     * nearest not-yet-linked room, if that one is close enough (squared distance below 600),
     * which adds loops and alternative routes.
     */

    private void connectRooms(List<Room> rooms) {

        if (rooms.isEmpty()) return;

        for (int i = 0; i < rooms.size() - 1; i++) {
            rooms.get(i).connect(rooms.get(i + 1));
        }

        for (Room current : rooms) {
            Room closest = null;
            double minDistance = Double.MAX_VALUE;

            for (Room other : rooms) {

                if (current == other || current.getConnectedRooms().contains(other)) {
                    continue;
                }

                double dist = Math.pow(current.getCenter().getX() - other.getCenter().getX(), 2) +
                        Math.pow(current.getCenter().getY() - other.getCenter().getY(), 2);

                if (dist < minDistance) {
                    minDistance = dist;
                    closest = other;
                }

            }

            if (closest != null && minDistance < 600.0) {
                current.connect(closest);
            }

        }

    }

    private void carveRoom(Maze maze, Room room) {

        for (Position pos : room.getTiles()) {
            maze.setObjectAt(pos, new Tile(pos));
        }

    }

    private void carveCorridors(Maze maze, List<Room> rooms) {

        for (Room room : rooms) {
            for (Room neighbor : room.getConnectedRooms()) {
                carveLShapedPath(maze, room.getCenter(), neighbor.getCenter());
            }
        }

    }

    private void carveLShapedPath(Maze maze, Position from, Position to) {

        int x = from.getX();
        int y = from.getY();
        int targetX = to.getX();
        int targetY = to.getY();

        boolean moveXFirst = random.nextBoolean();

        if (moveXFirst) {
            while (x != targetX) {
                carveTileSafe(maze, new Position(x, y));
                x += (targetX > x) ? 1 : -1;
            }
            while (y != targetY) {
                carveTileSafe(maze, new Position(x, y));
                y += (targetY > y) ? 1 : -1;
            }
        } else {
            while (y != targetY) {
                carveTileSafe(maze, new Position(x, y));
                y += (targetY > y) ? 1 : -1;
            }
            while (x != targetX) {
                carveTileSafe(maze, new Position(x, y));
                x += (targetX > x) ? 1 : -1;
            }

        }

    }

    private void carveTileSafe(Maze maze, Position pos) {

        if (maze.isInside(pos)) {
            maze.setObjectAt(pos, new Tile(pos));
        }

    }

}