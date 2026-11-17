package consolemaze.engine;

import consolemaze.model.Enemy;
import consolemaze.model.Maze;
import consolemaze.model.Player;
import java.util.ArrayList;
import java.util.List;

/**
 * Plain data holder returned by the map generator: everything needed to start a game
 * (the map, the player and the enemies at their starting positions).
 */

public class LevelData {

    private final Maze maze;
    private final Player player;
    private final List<Enemy> enemies;

    public LevelData(Maze maze, Player player, List<Enemy> enemies) {

        this.maze = maze;
        this.player = player;
        this.enemies = new ArrayList<>(enemies);

    }

    public LevelData(Maze maze, Player player) {

        this(maze, player, new ArrayList<>());

    }

    public Maze getMaze() {

        return maze;

    }

    public Player getPlayer() {

        return player;

    }

    public List<Enemy> getEnemies() {

        return enemies;

    }

}