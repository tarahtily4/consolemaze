package consolemaze.engine.generator;

import consolemaze.engine.LevelData;
import consolemaze.model.*;

import java.util.*;

/**
 * Populates a freshly carved map: the player, the exit, doors with their keys and coins,
 * then delegates enemies and props to {@link EnemySpawner} and {@link PropSpawner}.
 *
 * <p>The order of the steps matters (see {@link #populateLevel}).
 *
 * <p>Known limitation: keys are placed in random rooms, and the generator does not verify
 * that every key is reachable before its own door, so a level is not guaranteed to be solvable.
 */

public class EntitySpawner {
    private static final KeyColor[] KEY_COLORS = KeyColor.values();

    private final Random random = new Random();
    private final EnemySpawner enemySpawner = new EnemySpawner();
    private final PropSpawner propSpawner = new PropSpawner();

    public LevelData populateLevel(Maze maze, List<Room> rooms, GeneratorConfig config) {

        if (rooms.size() < 3) {
            throw new IllegalStateException("Too few rooms to generate keys and doors!");
        }

        Room startRoom = rooms.get(0);
        Room endRoom = findFarthestRoom(startRoom, rooms);

        Position playerPos = startRoom.getCenter();
        Player player = new Player(playerPos);

        Position exitPos = endRoom.getCenter();
        maze.setObjectAt(exitPos, new Exit(exitPos));

        spawnProgressiveDoorsAndKeys(maze, rooms, startRoom, endRoom, config.getKeyCount());

        spawnCoins(maze, rooms, config.getCoinCount(), playerPos);

        List<Enemy> enemies = enemySpawner.spawn(maze, rooms, startRoom, endRoom, config);

        propSpawner.spawn(maze, rooms, startRoom, playerPos, enemies, config);

        return new LevelData(maze, player, enemies);

    }

    private void spawnProgressiveDoorsAndKeys(Maze maze, List<Room> rooms, Room startRoom, Room endRoom, int totalDoors) {

        int doorsToSpawn = Math.min(totalDoors, KEY_COLORS.length);

        List<Room> pathRooms = new ArrayList<>(findRoomPath(startRoom, endRoom));
        List<Room> otherRooms = new ArrayList<>(rooms);
        otherRooms.removeAll(pathRooms);
        Collections.shuffle(pathRooms, random);
        Collections.shuffle(otherRooms, random);

        List<Room> ordered = new ArrayList<>(pathRooms);
        ordered.addAll(otherRooms);
        ordered.remove(startRoom);
        ordered.remove(endRoom);

        int placed = 0;

        for (int pass = 0; pass < 2 && placed < doorsToSpawn; pass++) {

            for (Room room : ordered) {

                if (placed >= doorsToSpawn) break;

                Position doorPos = GeneratorUtils.findBottleneckForRoom(maze, room);
                if (doorPos == null) continue;

                KeyColor color = KEY_COLORS[placed];

                maze.setObjectAt(doorPos, new Door(doorPos, color));
                spawnKeyInSafeRoom(maze, rooms, startRoom, room, color);
                placed++;

            }

        }

        if (placed < doorsToSpawn) {
            System.err.println("Placed only " + placed + " of " + doorsToSpawn + " doors");
        }

    }

    private void spawnKeyInSafeRoom(Maze maze, List<Room> rooms, Room startRoom, Room excludedRoom, KeyColor color) {

        List<Room> candidates = new ArrayList<>(rooms);
        candidates.remove(startRoom);
        candidates.remove(excludedRoom);

        if (candidates.isEmpty()) candidates = rooms;

        Room room = candidates.get(random.nextInt(candidates.size()));
        List<Position> tiles = room.getTiles();

        int safety = 0;
        while (safety < 50) {

            safety++;
            Position pos = tiles.get(random.nextInt(tiles.size()));

            if (maze.getObjectAt(pos) instanceof Tile) {

                maze.setObjectAt(pos, new Key(pos, color));
                break;

            }

        }

    }

    private List<Room> findRoomPath(Room start, Room target) {

        Queue<Room> queue = new LinkedList<>();
        Map<Room, Room> parentMap = new HashMap<>();
        Set<Room> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Room current = queue.poll();

            if (current == target) break;

            for (Room neighbor : current.getConnectedRooms()) {

                if (!visited.contains(neighbor)) {

                    visited.add(neighbor);
                    parentMap.put(neighbor, current);
                    queue.add(neighbor);

                }

            }

        }

        List<Room> path = new ArrayList<>();
        Room curr = target;

        while (curr != null) {

            path.add(0, curr);
            curr = parentMap.get(curr);

        }
        return path;

    }

    private void spawnCoins(Maze maze, List<Room> rooms, int count, Position playerPos) {

        int spawned = 0;
        int safety = 0;
        while (spawned < count && safety < 300) {

            safety++;
            Room room = rooms.get(random.nextInt(rooms.size()));
            List<Position> tiles = room.getTiles();
            Position pos = tiles.get(random.nextInt(tiles.size()));

            if (maze.getObjectAt(pos) instanceof Tile && !pos.equals(playerPos)) {

                maze.setObjectAt(pos, new Coin(pos, 1));
                spawned++;

            }

        }

    }

    private Room findFarthestRoom(Room start, List<Room> rooms) {

        Room farthest = start;
        double maxDist = -1;
        for (Room room : rooms) {

            double dist = Math.pow(start.getCenter().getX() - room.getCenter().getX(), 2) +
                    Math.pow(start.getCenter().getY() - room.getCenter().getY(), 2);

            if (dist > maxDist) {

                maxDist = dist;
                farthest = room;

            }

        }
        return farthest;

    }
}