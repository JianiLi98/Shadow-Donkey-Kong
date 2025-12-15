import bagel.*;

/**
 * This class represents the Blaster entity that can be collected by
 * Mario, allowing Mario to shoot and provide additional bullets.
 */
public class Blaster extends Entity implements Collectable {

    private static final Image BLASTER_IMG = new Image("res/blaster.png");
    private final int BULLET_COUNT = 5;
    private boolean collected;


    public Blaster(double x, double y) {
        super(x, y, BLASTER_IMG);
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
     * Sets the blaster's collected status to true after collected by Mario.
     */
    @Override
    public void marioCollected() {
        this.collected = true;
    }


    /**
     * Returns true if the blaster has been collected by Mario.
     *
     * @return true if collected
     */
    @Override
    public boolean isCollected() {
        return collected;
    }


    /**
     * Get the bullet count of a blaster when collecting it.
     *
     * @return the number of bullets.
     */
    public int getBulletCount() {
        return BULLET_COUNT;
    }
}
