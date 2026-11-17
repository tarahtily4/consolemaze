package consolemaze.engine.generator;

import consolemaze.model.*;

import java.util.*;

/**
 * Places the enemies after doors, keys and coins are in. It spawns traps in front of room
 * entrances, monkeys on patrol routes between neighbouring rooms and ghosts at a safe
 * distance from the player. Traps and monkeys never use the start room.
 */
public class EnemySpawner {

    private static final int GHOST_MIN_DISTANCE = 15;  // minimum distance from the player start, in cells
    private static final int MAX_PATROL_LENGTH = 16;   // maximum length of a monkey's patrol route

    private final Random random = new Random();

    public List<Enemy> spawn(Maze maze, List<Room> rooms, Room startRoom, Room endRoom, GeneratorConfig config) {
        List<Enemy> enemies = new ArrayList<>();

        enemies.addAll(spawnTraps(maze, rooms, startRoom, endRoom, config.getTrapCount()));
        enemies.addAll(spawnMonkeys(maze, rooms, startRoom, config.getMonkeyCount()));
        enemies.addAll(spawnGhosts(rooms, startRoom.getCenter(), config.getGhostCount()));

        return enemies;

    }

    // ---------------------------------------------------------------- ghosts
    /** Ghosts ignore walls, so any room cell far enough from the player is fine. */

    private List<Enemy> spawnGhosts(List<Room> rooms, Position playerPos, int count) {

        List<Enemy> result = new ArrayList<>();
        int attempts = 0;

        while (result.size() < count && attempts < 200) {

            attempts++;

            Room room = rooms.get(random.nextInt(rooms.size()));
            List<Position> tiles = room.getTiles();
            Position pos = tiles.get(random.nextInt(tiles.size()));

            if (manhattan(pos, playerPos) < GHOST_MIN_DISTANCE) continue;

            result.add(new Ghost(pos));

        }

        return result;

    }

    // ---------------------------------------------------------------- monkeys

    /**
     * A monkey patrols the walkable route from the center of one room toward a neighbouring
     * room, cut to {@code MAX_PATROL_LENGTH} cells. Each room hosts at most one monkey, and
     * routes that touch the start room are skipped.
     */

    private List<Enemy> spawnMonkeys(Maze maze, List<Room> rooms, Room startRoom, int count) {

        List<Enemy> result = new ArrayList<>();
        Set<Position> startTiles = new HashSet<>(startRoom.getTiles());

        List<Room[]> pairs = new ArrayList<>();
        for (Room room : rooms) {

            if (room == startRoom) continue;
            for (Room neighbor : room.getConnectedRooms()) {

                if (neighbor == startRoom) continue;
                pairs.add(new Room[]{room, neighbor});
            }

        }
        Collections.shuffle(pairs, random);

        Set<Room> usedRooms = new HashSet<>();
        for (Room[] pair : pairs) {

            if (result.size() >= count) break;
            if (usedRooms.contains(pair[0])) continue;

            List<Position> path = findPath(maze, pair[0].getCenter(), pair[1].getCenter());
            if (path.size() < 2) continue;

            if (path.size() > MAX_PATROL_LENGTH) {
                path = new ArrayList<>(path.subList(0, MAX_PATROL_LENGTH));
            }
            if (touchesTiles(path, startTiles)) continue;

            result.add(new Monkey(path));
            usedRooms.add(pair[0]);

        }

        return result;

    }

    /** Breadth-first search over walkable cells. Returns an empty list if there is no route. */
    List<Position> findPath(Maze maze, Position from, Position to) {

        if (!maze.isWalkable(from) || !maze.isWalkable(to)) {
            return new ArrayList<>();
        }

        Queue<Position> queue = new ArrayDeque<>();
        Map<Position, Position> parent = new HashMap<>();

        queue.add(from);
        parent.put(from, null);

        while (!queue.isEmpty()) {

            Position current = queue.poll();
            if (current.equals(to)) break;

            for (Position next : GeneratorUtils.orthogonalNeighbors(current)) {

                if (parent.containsKey(next) || !maze.isWalkable(next)) continue;
                parent.put(next, current);
                queue.add(next);

            }

        }

        if (!parent.containsKey(to)) {
            return new ArrayList<>();
        }

        LinkedList<Position> path = new LinkedList<>();

        for (Position cur = to; cur != null; cur = parent.get(cur)) {
            path.addFirst(cur);
        }

        return new ArrayList<>(path);

    }

    // ---------------------------------------------------------------- traps
    /** One trap per room at most, never in the start or exit room. Rooms without a usable entrance are skipped. */

    private List<Enemy> spawnTraps(Maze maze, List<Room> rooms, Room startRoom, Room endRoom, int count) {

        List<Enemy> result = new ArrayList<>();

        List<Room> candidates = new ArrayList<>(rooms);
        candidates.remove(startRoom);
        candidates.remove(endRoom);
        Collections.shuffle(candidates, random);

        for (Room room : candidates) {

            if (result.size() >= count) break;

            Entrance entrance = findEntrance(maze, room);
            if (entrance == null) continue;

            result.add(createTrap(room, entrance));

        }

        return result;

    }

    private Trap createTrap(Room room, Entrance entrance) {

        Position inside = entrance.inside;

        if (entrance.corridor.getX() == inside.getX()) {

            int min = room.getPosition().getX();
            int max = min + room.getWidth() - 1;
            return new Trap(inside, Axis.HORIZONTAL, min, max);

        }

        int min = room.getPosition().getY();
        int max = min + room.getHeight() - 1;
        return new Trap(inside, Axis.VERTICAL, min, max);

    }

    private Entrance findEntrance(Maze maze, Room room) {

        Set<Position> roomTiles = new HashSet<>(room.getTiles());
        List<Entrance> found = new ArrayList<>();

        for (Position inside : room.getTiles()) {

            for (Position outside : GeneratorUtils.orthogonalNeighbors(inside)) {

                if (roomTiles.contains(outside) || !maze.isInside(outside)) continue;

                GameObject obj = maze.getObjectAt(outside);
                boolean passage = obj instanceof Tile || obj instanceof Door;

                if (passage && GeneratorUtils.isStrictNarrowCorridor(maze, outside)) {
                    found.add(new Entrance(outside, inside));
                }

            }

        }

        return found.isEmpty() ? null : found.get(random.nextInt(found.size()));

    }

    // ---------------------------------------------------------------- helpers

    private static int manhattan(Position a, Position b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    private static boolean touchesTiles(List<Position> path, Set<Position> tiles) {
        for (Position p : path) {
            if (tiles.contains(p)) return true;
        }
        return false;
    }

    private static final class Entrance {

        final Position corridor;
        final Position inside;

        Entrance(Position corridor, Position inside) {
            this.corridor = corridor;
            this.inside = inside;

        }

    }

}