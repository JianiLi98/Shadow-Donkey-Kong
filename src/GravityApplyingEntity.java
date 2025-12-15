import bagel.*;
import bagel.util.Rectangle;
import java.util.ArrayList;

/**
 * An abstract base class for all entities affected by gravity in the game.
 *
 * GravityApplyingEntity handles vertical motion by applying gravity
 * and simulates falling with a maximum fall speed. It also includes logic
 * to align the entity to platforms after falling.
 */
public abstract class GravityApplyingEntity extends Entity {

    // Entities' default maximum falling speed and various gravity values
    private static final double MAX_FALL_SPEED = 10.0;
    private final double GRAVITY;

    private double velocityY = 0.0;


    public GravityApplyingEntity(double x, double y, Image entityImage, double gravity) {
        super(x, y, entityImage);
        this.GRAVITY = gravity;
    }


    /**
     * Updates the entity by applying gravity, aligning to platform, and rendering.
     *
     * @param platforms list of platforms for collision checks.
     */
    public void update(ArrayList<Platform> platforms) {
        applyGravity();
        alignToPlatforms(platforms);
        super.draw();
    }


    /**
     * Applies gravity to an entity, clamping its vertical velocity
     * to the configured maxFallSpeed, then updates its Y position.
     */
    protected void applyGravity() {
        this.velocityY = Math.min(velocityY + GRAVITY, MAX_FALL_SPEED);
        this.setPosY(this.getPosY() + this.velocityY);
    }


    /**
     * Aligns the entity to the top surface of a platform when intersects.
     * @param platforms  array of all platforms.
     */
    protected void alignToPlatforms(ArrayList<Platform> platforms) {
        Rectangle entityBounds = this.getBounds();

        for (Platform platform : platforms) {
            Rectangle platformBounds = platform.getBounds();

            if (platformBounds.intersects(entityBounds)) {
                // update the centre Y position of entity to align to surface
                this.setPosY(platformBounds.top() - halfImageHeight(getEntityImage()));
                break;
            }
        }
    }


    /**
     * Retrieves the default maximum falling speed for gravity-applying entities.
     * @return the maximum falling speed as double.
     */
    protected double getMaxFallSpeed() {
        return MAX_FALL_SPEED;
    }
}
