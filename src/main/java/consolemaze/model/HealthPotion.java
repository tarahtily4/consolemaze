package consolemaze.model;

/**
 * A health pickup. It can also be picked up at full health, in which case the surplus
 * is simply lost (healing is clamped by {@code Player#heal}).
 */

public class HealthPotion extends GameObject {

    private static final Collision COLLISION = new Collision(true, true);

    private final int healAmount;

    public HealthPotion(Position position, int healAmount) {

        super(position, '♥', COLLISION);
        this.healAmount = healAmount;

    }

    public int getHealAmount() {

        return healAmount;

    }

}