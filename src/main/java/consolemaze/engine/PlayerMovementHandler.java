package consolemaze.engine;

import consolemaze.model.*;

/**
 * Pure movement: moves the player one step if the target cell is walkable.
 * Interactions (pickups, doors, boulders) are resolved earlier by {@link MazeEngine},
 * so by the time this runs the target cell is already in its final state.
 */

public class PlayerMovementHandler {

    private final Maze maze;

    public PlayerMovementHandler(Maze maze) {

        this.maze = maze;

    }

    public void move(Player player, int dx, int dy) {

        if (!player.isAlive()) return;

        Position currentPos = player.getPosition();
        Position targetPos = new Position(currentPos.getX() + dx, currentPos.getY() + dy);

        if (!maze.isWalkable(targetPos)) {

            return;

        }

        player.moveTo(targetPos);

    }

}