import bagel.*;

/**
 * This class represents the enemy Donkey Kong.
 *
 * A stationary enemy that is affected by gravity. It can be defeated by Mario either
 * using a hammer (instant defeat) or by shooting it with bullets to reduce its health.
 */
public class Donkey extends GravityApplyingEntity {

    private static final Image DONKEY_IMG = new Image("res/donkey_kong.png");
    private static final double DONKEY_GRAVITY = 0.4;

    private int health = 5;


    public Donkey(double x, double y) {
        super(x, y, DONKEY_IMG, DONKEY_GRAVITY);
    }

    /**
     * Reduces the Donkey’s health by the specified damage amount.
     *
     * @param damage The amount of damage to subtract from Donkey's health
     */
    public void reduceHealth(int damage) {
        this.health -= damage;
    }

    /**
     * Returns Donkey’s current health value.
     *
     * @return The current health of Donkey as int
     */
    public int getHealth() {
        return this.health;
    }
}
