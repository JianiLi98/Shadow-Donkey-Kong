import bagel.*;

/**
 * This class represents the Hammer entity that can be collected by
 * Mario and provide invincibility.
 */
public class Hammer extends Entity implements Collectable {

    private static final Image HAMMER_IMG = new Image("res/hammer.png");
    private boolean collected = false;


    public Hammer(double x, double y) {
        super(x, y, HAMMER_IMG);
    }

    /**
     * Only renders if hammer is still not collected.
     */
    @Override
    public void draw() {
        if(!this.collected) {
            super.draw();
        }
    }

    /**
     * Sets the hammer's collected status to true after collected by Mario.
     */
    @Override
    public void marioCollected() {
        this.collected = true;
    }

    /**
     * Returns true if the hammer has been collected by Mario.
     *
     * @return true if collected
     */
    @Override
    public boolean isCollected() {
        return this.collected;
    }
}
