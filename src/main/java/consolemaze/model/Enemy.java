package consolemaze.model;

/**
 * Base class for all hostile entities (ghost, monkey, trap).
 *
 * <p>Enemies are not stored in the {@link Maze} grid: the engine keeps them in a
 * separate list and the UI draws them on top of the map. Contact damage is applied by the
 * engine when an enemy shares a cell with the player.
 *
 * <p>Template method pattern: the engine calls {@link #tick}, which throttles the speed
 * and delegates the actual behaviour to the subclass's {@link #step}.
 */

public abstract class Enemy extends MovableEntity {

    private final int damage;
    private final int moveInterval; // the enemy acts once per N ticks (bigger = slower)
    private int tickCounter = 0;

    protected Enemy(Position position, char symbol, int damage, int moveInterval) {

        super(position, symbol, new Collision(true, true));
        this.damage = damage;
        this.moveInterval = Math.max(1, moveInterval);

    }

    public int getDamage() {

        return damage;

    }

    /** Called by the engine on every game tick; runs {@link #step} every moveInterval-th call. */
    public final void tick(Maze maze, Player player) {

        tickCounter++;
        if (tickCounter < moveInterval) return;
        tickCounter = 0;
        step(maze, player);

    }

    /** One unit of enemy behaviour. Implemented by each concrete enemy type. */
    protected abstract void step(Maze maze, Player player);

}