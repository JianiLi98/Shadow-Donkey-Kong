import bagel.*;
import java.util.ArrayList;

/**
 * This class represents the barrel entity that lands and aligns to platforms
 * and can be destroyed or jumped over.
 */
public class Barrel extends GravityApplyingEntity {

    private static final Image BARREL_IMG = new Image("res/barrel.png");
    private static final double BARREL_GRAVITY = 0.4;

    private boolean destroyed = false;
    private boolean jumped = false;


    public Barrel(double x, double y) {
        super(x, y, BARREL_IMG, BARREL_GRAVITY);
    }

    /**
     * Update barrel and render if not destroyed by Mario.
     *
     * @param platforms  array of all platforms
     */
    @Override
    public void update(ArrayList<Platform> platforms) {
        applyGravity();
        alignToPlatforms(platforms);

        if(!this.destroyed) {
            super.draw();
        }
    }

    /**
     * Returns whether the barrel is still active (not destroyed).
     *
     * @return True if the barrel is not destroyed
     */
    public boolean isAlive() {
        return !this.destroyed;
    }

    /**
     * Marks the barrel as destroyed so it no longer appears or interacts.
     */
    public void destroy() {
        this.destroyed = true;
    }

    /**
     * Returns whether Mario has jumped over this barrel.
     *
     * @return True if the barrel was flagged jumped over
     */
    public boolean isJumped() {
        return this.jumped;
    }

    /**
     * Marks that Mario has successfully jumped over this barrel.
     */
    public void markJumped() {
        this.jumped = true;
    }

    /**
     * Resets the jumped status for the barrel.
     */
    public void resetJumped() {
        this.jumped = false;
    }
}
