package consolemaze.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The player: health, collected coins and keys.
 *
 * <p>After taking damage the player is briefly invulnerable (measured in game ticks),
 * so a single enemy standing on the player cannot drain all health at once.
 * The player is not stored in the {@link Maze} grid.
 */

public class Player extends MovableEntity {

    private static final int INVULNERABILITY_TICKS = 8;

    private int health;
    private final int maxHealth = 10;
    private final List<Coin> inventory;
    private final List<Key> keys = new ArrayList<>();
    private int invulnerableTicks = 0;

    public Player(Position position) {

        super(position, '웃', new Collision(true, true));

        this.health = maxHealth;
        this.inventory = new ArrayList<>();

    }

    public int getHealth() {

        return health;

    }

    public int getMaxHealth() {

        return maxHealth;

    }

    public void takeDamage(int amount) {

        if (amount <= 0 || invulnerableTicks > 0) return;

        this.health -= amount;
        if (this.health < 0) {

            this.health = 0;

        }

        this.invulnerableTicks = INVULNERABILITY_TICKS;

    }

    public void tickInvulnerability() {

        if (invulnerableTicks > 0) {

            invulnerableTicks--;

        }

    }

    public boolean isInvulnerable() {

        return invulnerableTicks > 0;

    }

    public void heal (int amount) {

        this.health += amount;
        if (this.health > maxHealth) {

            this.health = maxHealth;

        }

    }

    public boolean isAlive() {
        return health > 0;
    }

    public List<Coin> getInventory() {
        return  inventory;
    }

    public void addCoin(Coin coin) {

        if (coin != null) {

            this.inventory.add(coin);

        }

    }

    public List<Key> getKeys() { return keys; }
    public void addKey(Key key) { if (key != null) keys.add(key); }
    public boolean hasKey(KeyColor color) {
        return keys.stream().anyMatch(k -> k.getKeyColor() == color);
    }
    public void removeKey(KeyColor color) {
        keys.removeIf(k -> k.getKeyColor() == color);
    }

}