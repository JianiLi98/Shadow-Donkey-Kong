import bagel.*;
import bagel.util.Rectangle;
import java.util.ArrayList;

/**
 * Represents a bullet fired by Mario when holding a blaster.
 *
 * Bullets travel horizontally in the direction Mario is facing, handle damage
 * to Donkey or Monkeys upon collision, and disappear after travelling a
 * maximum distance or upon hitting a platform or window boundary.
 */
public class Bullet extends Entity implements AutomatedMovable {

    private static final Image BULLET_RIGHT_IMG = new Image("res/bullet_right.png");
    private static final Image BULLET_LEFT_IMG = new Image("res/bullet_left.png");

    // Constants for Bullet entity:
    private static final double MOVE_SPEED = 3.8;
    private static final double MAX_DISTANCE_PIXEL = 300;
    private static final int DAMAGE_TO_DONKEY = 1;
    private final boolean shootingRight;

    private double distanceTravelled = 0;
    private boolean isAppear = true;


    public Bullet(double x, double y, boolean shootingRight) {
        super(x, y, BULLET_LEFT_IMG);
        this.shootingRight = shootingRight;
    }


    /**
     * Draws the bullet image depending on its facing direction.
     */
    @Override
    public void draw() {
        Image currentImage = this.shootingRight ? BULLET_RIGHT_IMG : BULLET_LEFT_IMG;
        currentImage.draw(this.getPosX(), this.getPosY());
    }


    /**
     * Moves the bullet in its firing direction, updating its horizontal position
     * and the total distance it has travelled.
     */
    @Override
    public void move() {
        double velocityX = this.shootingRight ? MOVE_SPEED : -MOVE_SPEED;
        this.setPosX(this.getPosX() + velocityX);
        this.distanceTravelled += MOVE_SPEED;
    }


    /**
     * Updates the bullet's state: moves and renders if within range, or removes
     * it if expired. Also checks for collisions with platforms or screen bounds.
     *
     * @param platforms The platforms to check for collisions
     */
    public void update(ArrayList<Platform> platforms) {
        // Move and render bullet if still alive and within moving range
        if (this.isAppear && this.distanceTravelled < MAX_DISTANCE_PIXEL) {
            move();
            draw();
        } else {
            disappear();
        }
        checkPlatformCollisions(platforms);
        reachWindowEdge();
    }


    /**
     * Checks for and handles a collision with Donkey.
     * Reduces Donkey's health if a collision occurs.
     *
     * @param donkey The Donkey to check against
     */
    public void collideWithDonkey(Donkey donkey) {
        Rectangle bulletBounds = this.getBounds();

        if (this.isAppear && bulletBounds.intersects(donkey.getBounds())) {
            donkey.reduceHealth(DAMAGE_TO_DONKEY);
            disappear();
        }
    }


    /**
     * Checks for and handles collisions with any Monkey.
     * Destroys both the bullet and the monkey on collision.
     *
     * @param monkeys The list of monkeys to check
     * @return True if a monkey was hit, false otherwise
     */
    public boolean collideWithMonkeys(ArrayList<Monkey> monkeys) {
        Rectangle bulletBounds = this.getBounds();

        for (Monkey monkey : monkeys) {
            Rectangle monkeyBounds = monkey.getBounds();
            if (this.isAppear && bulletBounds.intersects(monkeyBounds)) {
                monkey.destroy();
                disappear();
                return true;
            }
        }
        return false;
    }


    /**
     * Checks whether the bullet intersects with any platform and destroys it on contact.
     *
     * @param platforms The list of platforms to check
     */
    private void checkPlatformCollisions(ArrayList<Platform> platforms) {
        Rectangle bulletBounds = this.getBounds();

        for (Platform platform : platforms) {
            Rectangle platformBounds = platform.getBounds();
            if (this.isAppear && bulletBounds.intersects(platformBounds)) {
                disappear();
            }
        }
    }


    /**
     * Destroys the bullet if it goes beyond the horizontal bounds of the game screen.
     */
    private void reachWindowEdge() {
        double halfWidth = BULLET_LEFT_IMG.getWidth()/2;

        if (this.getPosX() < halfWidth) {
            disappear();
        }

        double maxX = ShadowDonkeyKong.getScreenWidth() - halfWidth;
        if (this.getPosX() > maxX) {
            disappear();
        }
    }


    /**
     * Marks the bullet as spent so it is no longer updated or drawn.
     */
    private void disappear() {
        isAppear = false;
    }
}
