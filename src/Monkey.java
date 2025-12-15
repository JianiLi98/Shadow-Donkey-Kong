import bagel.Image;
import bagel.util.Rectangle;
import java.util.ArrayList;

/**
 * Represents a monkey entity in Level 2 that moves along platforms
 * following a predefined walking route.
 *
 * Monkeys are affected by gravity, patrols on platforms, and turn around at
 * the edge of their platform, screen boundaries, or after completing a route segment.
 */
public abstract class Monkey extends GravityApplyingEntity implements AutomatedMovable {

    private static final double MOVE_SPEED = 0.5;
    private static final double MONKEY_GRAVITY = 0.4;
    private final Image rightImage;
    private final Image leftImage;

    private ArrayList<Integer> route = new ArrayList<>();
    private boolean facingRight;
    private boolean isAppear = true;
    private double distanceTravelled = 0;
    private int currRouteIndex = 0;


    public Monkey(double x, double y, boolean facingRight, ArrayList<Integer> route,
                  Image rightImage, Image leftImage) {
        super(x, y, rightImage, MONKEY_GRAVITY);
        this.rightImage = rightImage;
        this.leftImage = leftImage;
        this.route = route;
        this.facingRight = facingRight;
    }


    /**
     * Draws the monkey using the appropriate image based on its current direction.
     * The monkey is not rendered if it has been destroyed.
     */
    @Override
    public void draw() {
        if (this.isAppear) {
            Image currentImage = this.facingRight ? rightImage : leftImage;
            currentImage.draw(getPosX(), getPosY());
        }
    }


    /**
     * Moves the monkey one step in the current direction
     * and updates the distance travelled.
     */
    @Override
    public void move() {
        double velocityX = this.facingRight ? MOVE_SPEED : -MOVE_SPEED;
        this.setPosX(this.getPosX() + velocityX);
        this.distanceTravelled += MOVE_SPEED;
    }


    /**
     * Updates the monkey by applying gravity, aligning to platforms,
     * handling movement logic, and drawing the entity.
     *
     * @param platforms List of platforms for collision and edge detection
     */
    @Override
    public void update(ArrayList<Platform> platforms){
        applyGravity();
        alignToPlatforms(platforms);

        moveOnPlatforms(platforms);
        draw();
    }


    /**
     * Handles directional logic while monkey moves across a platform.
     * Turns around after travelling the specified route segment or reaching the edge of platforms
     * or window edges.
     *
     * @param platforms List of platforms for edge detection
     */
    private void moveOnPlatforms(ArrayList<Platform> platforms) {
        move();

        // Start new patrol route if current is complete or reached platform edges or window edges
        if ((this.distanceTravelled >= route.get(currRouteIndex))
                || reachPlatformEdge(platforms) || reachWindowEdge()) {
            // Reset and update attributes for rendering
            this.distanceTravelled = 0;
            this.facingRight = !facingRight;
            this.currRouteIndex = (currRouteIndex + 1) % route.size();
        }
    }


    /**
     * Checks whether the monkey has reached the edge of the current platform.
     *
     * @param platforms List of platforms to check
     * @return True if the edge is reached, false otherwise
     */
    private boolean reachPlatformEdge(ArrayList<Platform> platforms) {
        Rectangle monkeyBounds = this.getBounds();

        for (Platform platform : platforms) {
            Rectangle platformBounds = platform.getBounds();

            // Found the platform Monkey is currently on
            if (monkeyBounds.bottom() == platformBounds.top()) {
                // True if Monkey's right/left edge reached the right/left edge of the platform
                if (facingRight && monkeyBounds.right() >= platformBounds.right()) {
                    return true;
                } else if (!facingRight && monkeyBounds.left() <= platformBounds.left()) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Checks if the monkey has reached the edge of the game screen window.
     * If so, its position is clamped and it turns around.
     *
     * @return True if the monkey reached the screen edge, false otherwise
     */
    private boolean reachWindowEdge() {
        double halfWidth = rightImage.getWidth()/2;

        // True if Monkey is moving beyond the left edge of the screen
        if (this.getPosX() < halfWidth) {
            this.setPosX(halfWidth);
            return true;
        }

        // True if Monkey is moving beyond the right edge of the screen
        double maxX = ShadowDonkeyKong.getScreenWidth() - halfWidth;
        if (this.getPosX() > maxX) {
            this.setPosX(maxX);
            return true;
        }

        return false;
    }


    /**
     * Returns whether the monkey is currently facing right.
     *
     * @return True if facing right, false if facing left
     */
    protected boolean isFacingRight() {
        return facingRight;
    }


    /**
     * Marks the monkey as destroyed, so it will no longer be rendered or updated.
     */
    public void destroy() {
        isAppear = false;
    }

    /**
     * Checks if the monkey is still active and visible.
     *
     * @return True if the monkey is alive, false if destroyed
     */
    public boolean isAppear() {
        return isAppear;
    }
}
