package consolemaze.engine;

import consolemaze.model.*;
import java.util.List;

/**
 * The game rules for every kind of interaction (pickups, doors, boulders, enemy hits)
 * plus the win flag. Each method changes the passed player/maze directly; deciding
 * which method runs is the job of {@link MazeEngine}'s rule registry.
 */

public class InteractionHandler {

    private boolean gameWon = false;

    public void handleCoinPickup(Player player, Coin coin, Maze maze, Position position) {

        player.addCoin(coin);
        maze.setObjectAt(position, new Tile(position));

    }

    public void handleHealthPickup(Player player, HealthPotion potion, Maze maze, Position position) {

        player.heal(potion.getHealAmount());
        maze.setObjectAt(position, new Tile(position));

    }

    public void handleExit(Exit exit) {

        this.gameWon = true;

    }

    public boolean isGameWon() {

        return gameWon;

    }

    public void handleKeyPickup(Player player, Key key, Maze maze, Position position) {

        player.addKey(key);
        maze.setObjectAt(position, new Tile(position));

    }

    public boolean handleDoorUnlock(Player player, Door door) {

        if (door.isOpen()) return true;

        if (player.hasKey(door.getKeyColor())) {

            player.removeKey(door.getKeyColor());
            door.open();
            return true;

        }

        return false;

    }

    public void handleVegetationBreak(Maze maze, Position position) {

        maze.setObjectAt(position, new Tile(position));

    }

    public boolean handleBoulderPush(Maze maze, List<Enemy> enemies, Boulder boulder,
                                     Position boulderPos, int dx, int dy) {

        if (dx != 0 && dy != 0) return false;

        Position target = new Position(boulderPos.getX() + dx, boulderPos.getY() + dy);

        if (!boulder.canBePushedTo(maze, target)) return false;
        if (isOccupiedByEnemy(enemies, target)) return false;

        maze.setObjectAt(boulderPos, new Tile(boulderPos));
        maze.setObjectAt(target, boulder);
        return true;

    }

    public void handleEnemyHit(Player player, Enemy enemy) {

        player.takeDamage(enemy.getDamage());

    }

    private boolean isOccupiedByEnemy(List<Enemy> enemies, Position position) {

        for (Enemy enemy : enemies) {
            if (enemy.getPosition().equals(position)) return true;
        }
        return false;

    }

}