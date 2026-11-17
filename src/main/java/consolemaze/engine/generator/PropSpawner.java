package consolemaze.engine.generator;

import consolemaze.model.*;

import java.util.*;

/** Расставляет хилки, камни и растительность. Вызывается последним, когда враги уже есть. */
public class PropSpawner {

    private static final int CLUSTER_MIN_SIZE = 4;
    private static final int CLUSTER_MAX_SIZE = 8;
    private static final int MAX_ATTEMPTS = 300;

    private final Random random = new Random();

    public void spawn(Maze maze, List<Room> rooms, Room startRoom, Position playerStart,
                      List<Enemy> enemies, GeneratorConfig config) {

        Set<Position> blocked = new HashSet<>();
        blocked.add(playerStart);
        for (Enemy enemy : enemies) {
            blocked.add(enemy.getPosition());
        }

        spawnHealthPotions(maze, rooms, startRoom, blocked, config.getHealCount(), config.getHealAmount());
        spawnBoulders(maze, rooms, blocked, collectMonkeyPaths(enemies), config.getBoulderCount());
        spawnVegetation(maze, rooms, blocked, config.getVegetationClusters());
    }

    // ---------------------------------------------------------------- HealthPotions

    private void spawnHealthPotions(Maze maze, List<Room> rooms, Room startRoom,
                                    Set<Position> blocked, int count, int healAmount) {
        List<Room> candidates = new ArrayList<>(rooms);
        candidates.remove(startRoom);
        if (candidates.isEmpty()) return;

        int placed = 0;
        int attempts = 0;
        while (placed < count && attempts < MAX_ATTEMPTS) {
            attempts++;

            Room room = candidates.get(random.nextInt(candidates.size()));
            Position pos = randomTile(room);
            if (!isFree(maze, pos, blocked)) continue;

            maze.setObjectAt(pos, new HealthPotion(pos, healAmount));
            placed++;
        }
    }

    // ---------------------------------------------------------------- Boulders

    private void spawnBoulders(Maze maze, List<Room> rooms, Set<Position> blocked,
                               Set<Position> monkeyPaths, int count) {
        int placed = 0;
        int attempts = 0;
        while (placed < count && attempts < MAX_ATTEMPTS) {
            attempts++;

            Room room = rooms.get(random.nextInt(rooms.size()));
            if (room.getWidth() < 3 || room.getHeight() < 3) continue;

            int x = room.getPosition().getX() + 1 + random.nextInt(room.getWidth() - 2);
            int y = room.getPosition().getY() + 1 + random.nextInt(room.getHeight() - 2);
            Position pos = new Position(x, y);

            if (!isFree(maze, pos, blocked) || monkeyPaths.contains(pos)) continue;

            maze.setObjectAt(pos, new Boulder(pos, room));
            placed++;
        }
    }

    private Set<Position> collectMonkeyPaths(List<Enemy> enemies) {
        Set<Position> cells = new HashSet<>();
        for (Enemy enemy : enemies) {
            if (enemy instanceof Monkey) {
                cells.addAll(((Monkey) enemy).getPath());
            }
        }
        return cells;
    }

    // ---------------------------------------------------------------- Vegetation

    private void spawnVegetation(Maze maze, List<Room> rooms, Set<Position> blocked, int clusters) {
        for (int i = 0; i < clusters; i++) {
            Room room = rooms.get(random.nextInt(rooms.size()));
            int size = CLUSTER_MIN_SIZE + random.nextInt(CLUSTER_MAX_SIZE - CLUSTER_MIN_SIZE + 1);
            growCluster(maze, room, randomTile(room), size, blocked);
        }
    }

    private void growCluster(Maze maze, Room room, Position seed, int size, Set<Position> blocked) {
        List<Position> frontier = new ArrayList<>();
        frontier.add(seed);

        int placed = 0;
        int guard = 0;
        while (placed < size && !frontier.isEmpty() && guard < 100) {
            guard++;

            Position pos = frontier.remove(random.nextInt(frontier.size()));
            if (!room.contains(pos) || !isFree(maze, pos, blocked)) continue;

            maze.setObjectAt(pos, new Vegetation(pos));
            placed++;
            frontier.addAll(GeneratorUtils.orthogonalNeighbors(pos));
        }
    }

    // ---------------------------------------------------------------- helpers

    private boolean isFree(Maze maze, Position pos, Set<Position> blocked) {
        return maze.getObjectAt(pos) instanceof Tile && !blocked.contains(pos);
    }

    private Position randomTile(Room room) {
        List<Position> tiles = room.getTiles();
        return tiles.get(random.nextInt(tiles.size()));
    }
}