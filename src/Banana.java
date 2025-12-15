import bagel.*;

/**
 * Represents a banana thrown by an intelligent monkey.
 *
 * Bananas move horizontally in the direction the monkey is facing.
 * They disappear after travelling a fixed distance or when collided.
 */
public class Banana extends Entity implements AutomatedMovable {

    private static final Image BANANA_IMG = new Image("res/banana.png");
    private static final double MOVE_SPEED = 1.8;
    private final double MAX_DISTANCE_PIXEL = 300;

    private final boolean shootingRight;
    private double distanceTravelled = 0;
    private boolean isAppear = true;


    public Banana(double x, double y, boolean shootingRight) {
        super(x, y, BANANA_IMG);
        this.shootingRight = shootingRight;
    }

    /**
     * Moves the banana horizontally in its firing direction,
     * and updates the total distance travelled.
     */
    @Override
    public void move() {
        double velocityX = this.shootingRight ? MOVE_SPEED : -MOVE_SPEED;
        this.setPosX(this.getPosX() + velocityX);
        this.distanceTravelled += MOVE_SPEED;
    }

    /**
     * Updates the banana each frame: moves and draws it if within range,
     * otherwise destroys it when the travel limit is exceeded.
     */
    public void update() {
        if (this.isAppear && this.distanceTravelled < MAX_DISTANCE_PIXEL) {
            move();
            super.draw();
        } else {
            disappear();
        }
    }

    /**
     * Checks if the banana is still active and visible in the game.
     *
     * @return True if the banana is active, false if destroyed
     */
    public boolean isAppear() {
        return isAppear;
    }


    /**
     * Marks the banana as disappeared so it is no longer updated or drawn.
     */
    public void disappear() {
        isAppear = false;
    }
}
