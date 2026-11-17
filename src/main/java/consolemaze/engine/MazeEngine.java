package consolemaze.engine;

import consolemaze.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The game logic core, independent of any UI. It has two entry points:
 *
 *   @link #processPlayerMove: a player action (one step, possibly an interaction);
 * {@link #tick}: time-driven update of the enemies, called periodically by the UI.
 *
 * Interactions with objects are data-driven: a registry maps each trigger object class
 * to an {@link InteractionRule}, so adding a new interactive object means writing a handler
 * and registering one line, with no {@code instanceof} chains in the movement code.
 */

public class MazeEngine {

    private final Maze maze;
    private final Player player;
    private final List<Enemy> enemies;
    private final PlayerMovementHandler movementHandler;
    private final InteractionHandler interactionHandler;

    // Maps the exact class of a trigger object to its rule (lookup uses getClass(),
    // so subclasses would need their own registration)
    private final Map<Class<? extends GameObject>, InteractionRule> interactionRules = new HashMap<>();

    public MazeEngine (Maze maze, Player player, List<Enemy> enemies) {

        this.maze = maze;
        this.player = player;
        this.enemies = new ArrayList<>(enemies);
        this.interactionHandler = new InteractionHandler();
        this.movementHandler = new PlayerMovementHandler(maze);

        registerInteractionRules();

    }

    public MazeEngine (Maze maze, Player player) {

        this(maze, player, new ArrayList<>());

    }

    private void registerInteractionRules() {

        interactionRules.put(Exit.class, ctx -> {
            interactionHandler.handleExit((Exit) ctx.getTarget());
            return true;
        });

        interactionRules.put(Coin.class, ctx -> {
            interactionHandler.handleCoinPickup(player, (Coin) ctx.getTarget(), maze, ctx.getPosition());
            return true;
        });

        interactionRules.put(Key.class, ctx -> {
            interactionHandler.handleKeyPickup(player, (Key) ctx.getTarget(), maze, ctx.getPosition());
            return true;
        });

        interactionRules.put(HealthPotion.class, ctx -> {
            interactionHandler.handleHealthPickup(player, (HealthPotion) ctx.getTarget(), maze, ctx.getPosition());
            return true;
        });

        interactionRules.put(Vegetation.class, ctx -> {
            interactionHandler.handleVegetationBreak(maze, ctx.getPosition());
            return true;
        });

        interactionRules.put(Door.class, ctx ->
                interactionHandler.handleDoorUnlock(player, (Door) ctx.getTarget()));

        interactionRules.put(Boulder.class, ctx ->
                interactionHandler.handleBoulderPush(maze, enemies, (Boulder) ctx.getTarget(),
                        ctx.getPosition(), ctx.getDx(), ctx.getDy()));

    }

    /**
     * Handles one player step: resolve the interaction with the target cell (if it is a
     * trigger), then move, then check for contact with enemies.
     */
    public void processPlayerMove(int dx, int dy) {

        if (isGameOver() || isGameWon()) return;

        Position currentPos = player.getPosition();
        Position targetPos = new Position(currentPos.getX() + dx, currentPos.getY() + dy);

        GameObject targetObj = maze.getObjectAt(targetPos);

        if (targetObj != null && targetObj.getCollision().isTrigger()) {
            if (!interact(targetObj, targetPos, dx, dy)) return;
        }

        movementHandler.move(player, dx, dy);

        checkEnemyCollisions();
    }

    private boolean interact(GameObject target, Position position, int dx, int dy) {

        InteractionRule rule = interactionRules.get(target.getClass());
        if (rule == null) return true;

        return rule.apply(new InteractionContext(target, position, dx, dy));

    }

    public void tick() {

        if (isGameOver() || isGameWon()) return;

        player.tickInvulnerability();

        for (Enemy enemy : enemies) {
            enemy.tick(maze, player);
        }

        checkEnemyCollisions();
    }

    private void checkEnemyCollisions() {

        if (isGameOver() || isGameWon()) return;

        for (Enemy enemy : enemies) {
            if (enemy.getPosition().equals(player.getPosition())) {
                interactionHandler.handleEnemyHit(player, enemy);
            }
        }
    }

    public boolean isGameOver() {
        return !player.isAlive();
    }

    public boolean isGameWon() {
        return interactionHandler.isGameWon();
    }

    public Player getPlayer() {
        return player;
    }

    public Maze getMaze() {
        return maze;
    }

    public List<Enemy> getEnemies() {
        return Collections.unmodifiableList(enemies);
    }
}