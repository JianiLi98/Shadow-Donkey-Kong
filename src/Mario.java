import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

/**
 * The Mario(Player) class that supports movements controlled by the keyboard input,
 * and detects interactions with other entities to control the game states.
 *
 * Mario lands on platforms. Mario can move horizontally, vertically (by ladders), and jump.
 * Mario interacts with ladders, hammer, Donkey Kong, and barrels.
 */
public class Mario extends GravityApplyingEntity {

    // constants for movements:
    private static final double MOVE_SPEED = 3.5;
    private static final double INIT_JUMP_VELOCITY = -5.0;
    private static final double CLIMB_SPEED = 2.0;
    private static final double GRAVITY = 0.2;
    private static final double MAX_JUMP_HEIGHT = 62.5;

    // images for Mario's different states
    private static final Image MARIO_RIGHT_IMG = new Image("res/mario_right.png");
    private final Image MARIO_LEFT_IMG = new Image("res/mario_left.png");
    private final Image MARIO_HAMMER_RIGHT = new Image("res/mario_hammer_right.png");
    private final Image MARIO_HAMMER_LEFT = new Image("res/mario_hammer_left.png");
    private static final Image MARIO_BLASTER_RIGHT = new Image("res/mario_blaster_right.png");
    private static final Image MARIO_BLASTER_LEFT = new Image("res/mario_blaster_left.png");

    // state tracking for Mario
    private double velocityY = 0.0;
    private double jumpStartY;
    private boolean onPlatform = false;
    private boolean onLadder = false;
    private boolean canClimbDown = false;
    private boolean hitPlatform = false;
    private boolean isClimbing = false;
    private boolean hasHammer = false;
    private boolean isJumping = false;
    private boolean facingRight = true;
    private Image currImage = MARIO_RIGHT_IMG;
    private boolean hasBlaster = false;
    private int bulletCount = 0;

    // previous‐frame position for jump‑over detection for barrels
    private Point marioPrevPos;


    public Mario(double x, double y) {
        super(x, y, MARIO_RIGHT_IMG, GRAVITY);
    }


    /**
     * Ensures to render correct, updated image since Mario has dynamic images.
     */
    @Override
    public void draw() {
        updateImage();
        this.currImage.draw(this.getPosX(), this.getPosY());
    }


    /**
     * Only let Mario fall if not on any platform and not climbing ladders.
     */
    @Override
    protected void applyGravity() {
        if (!onPlatform && !isClimbing) {
            velocityY = Math.min(velocityY + GRAVITY, this.getMaxFallSpeed());
            this.setPosY(this.getPosY() + velocityY);
        }
    }


    /**
     * Check if Mario is on a platform or if is climbing a ladder and then
     * implement the alignment logic to the platforms.
     *
     * @param platforms  array of all platforms
     */
    @Override
    protected void alignToPlatforms(ArrayList<Platform> platforms) {
        // Find if Mario is on any one of the platforms
        Platform platform = onThisPlatform(platforms);

        // Only align Mario to the platform if he isn’t currently climbing.
        if (platform != null && !isClimbing) {
            this.setPosY(platform.getBounds().top() - halfImageHeight(currImage));

            // Reset vertical state now that Mario is landed
            this.onPlatform = true;
            this.isJumping = false;
            this.velocityY = 0;
        }
    }


    /**
     * Ensures to get correct bounding rectangles since Mario has dynamic images.
     *
     * @return A Rectangle representing Mario’s current bounds
     */
    @Override
    public Rectangle getBounds() {
        return this.currImage.getBoundingBoxAt(this.getPos());
    }


    /**
     * Applies gravity and platform alignment for Mario (no input logic).
     *
     * @param platforms List of platforms to align against
     */
    @Override
    public void update(ArrayList<Platform> platforms) {
        applyGravity();
        alignToPlatforms(platforms);
    }


    /**
     * For Level 1, advances and updates the state of Mario(Player) based on keyboard input, moving Mario
     * left/right, climbing up/down, and initiating jumps.
     *
     * @param input    current keyboard input
     * @param platforms list of platforms for collision checks
     * @param ladders   list of ladders for climbing logic
     */
    public void updateLevel1(Input input, ArrayList<Platform> platforms, ArrayList<Ladder> ladders) {
        getPreviousPos();

        update(platforms);

        // apply movements and physics
        moveHorizontal(input);
        moveVertical(input, ladders);
        allowJumping(input);

        draw();
    }


    /**
     * For Level 2, advances and updates the state of Mario(Player) based on keyboard input, moving Mario
     * left/right, climbing up/down, and initiating jumps.
     *
     * @param input    current keyboard input
     * @param platforms list of platforms for collision checks
     * @param ladders   list of ladders for climbing logic
     */
    public void updateLevel2(Input input, ArrayList<Platform> platforms, ArrayList<Ladder> ladders) {
        getPreviousPos();

        update(platforms);

        // apply movements and physics
        moveHorizontal(input);
        moveVertical(input, ladders);
        allowJumping(input);

        draw();
    }


    /**
     * Checks for collision between Mario and another entity.
     *
     * @param entity The entity to check collision with
     * @return true if Mario intersects with the entity
     */
    public boolean marioCollidesWith(Entity entity) {
        return this.getBounds().intersects(entity.getBounds());
    }


    /**
     * Checks if Mario collects a hammer and updates state accordingly.
     *
     * @param hammer The hammer to check collision with
     */
    public void checkHammers(Hammer hammer) {
        if (!hammer.isCollected() && marioCollidesWith(hammer)) {
            this.hasHammer = true;
            this.hasBlaster = false;
            bulletCount = 0;
            hammer.marioCollected();
        }
    }


    /**
     * Checks and collects available blasters, accumulating bullet count.
     *
     * @param blasters List of blasters to check for collision
     */
    public void checkBlasters(ArrayList<Blaster> blasters) {
        for (Blaster blaster : blasters) {
            if (!blaster.isCollected() && marioCollidesWith(blaster)) {
                this.hasBlaster = true;
                this.hasHammer = false;
                blaster.marioCollected();
                this.bulletCount += blaster.getBulletCount();
            }
        }
    }


    /**
     * Fires a bullet in the current direction if Mario has a blaster and ammo.
     *
     * @param input The keyboard input
     * @return A new Bullet if a shot is fired, or null otherwise
     */
    public Bullet shootBullet(Input input) {
        // If user pressed "S" as keyboard input, mario has blaster and bullet remaining
        if (input.wasPressed(Keys.S) && hasBlaster && bulletCount > 0) {
            // Creates and returns a new bullet
            return new Bullet(this.getPosX(), this.getPosY(), this.isFacingRight());
        }
        return null;
    }


    /**
     * Decrements bullet count. Removes blaster when bullets run out.
     */
    public void bulletSpent() {
        bulletCount --;
        if (bulletCount == 0) {
            hasBlaster = false;
        }
    }


    /**
     * Checks whether Mario has jumped over a given barrel.
     *
     * @param barrel The barrel to test jump-over logic
     * @return true if Mario successfully jumped over the barrel
     */
    public boolean jumpedOverBarrel(Barrel barrel) {
        Rectangle barrelBounds = barrel.getBounds();
        double prevMarioBottom = marioPrevPos.y + halfImageHeight(currImage);

        // Check when Mario is vertically moving above barrel
        boolean verticalJumpedOver = (prevMarioBottom >= barrelBounds.top()) &&
                (this.getBounds().bottom() < barrelBounds.top());

        // Flag the barrel if satisfying a vertical jumped-over
        if (!barrel.isJumped() && this.isJumping && verticalJumpedOver) {
            barrel.markJumped();
        }

        // Reward points if sure that Mario is actually jumping over
        if (barrel.isJumped() && this.isJumping && this.getPosY() < barrelBounds.top()) {
            // When horizontal overlapping is also satisfied
            if (this.getPosX() > barrelBounds.left() && this.getPosX() < barrelBounds.right()) {
                // Reset flag to allow multiple scoring for one barrel
                barrel.resetJumped();
                return true;
            }
        }
        return false;
    }


    /**
     * Handle horizontal movement based on keyboard input.
     *
     * @param input current keyboard input
     */
    private void moveHorizontal(Input input) {
        if(input.isDown(Keys.LEFT)) {
            this.setPosX(this.getPosX() - MOVE_SPEED);
            facingRight = false;
            isClimbing = false;
        }
        if(input.isDown(Keys.RIGHT)) {
            this.setPosX(this.getPosX() + MOVE_SPEED);
            facingRight = true;
            isClimbing = false;
        }
        limitToScreenEdges();
    }


    /**
     * Handle vertical movement by ladders based on keyboard input.
     *
     * @param input current keyboard input
     * @param ladders all ladders
     */
    private void moveVertical(Input input, ArrayList<Ladder> ladders) {

        trackLadderCollision(ladders);

        // if involved in collision (is climbing), either inside of ladder or trying to climb down
        if (this.onLadder || (this.canClimbDown && input.isDown(Keys.DOWN))) {
            this.isJumping = false;
            this.isClimbing = true;

            if (input.isDown(Keys.UP)) {
                this.setPosY(this.getPosY() - CLIMB_SPEED);
            }
            if (this.canClimbDown && input.isDown(Keys.DOWN)) {
                // Allow to intersect if on top of a ladder
                if (!onLadder) {
                    this.setPosY(this.getPosY() + CLIMB_SPEED);
                    // or allow climbing down until hitting a platform
                } else if (!hitPlatform) {
                    this.setPosY(this.getPosY() + CLIMB_SPEED);
                }
            }
        } else {
            this.isClimbing = false;
        }
    }


    /**
     * Helper for moveVertical to update the collision status with ladders.
     */
    private void trackLadderCollision(ArrayList<Ladder> ladders) {
        // reset all ladder collision tracking status
        this.onLadder = false;
        this.canClimbDown = false;
        this.hitPlatform = false;

        Rectangle marioBounds = this.getBounds();
        Point MarioBottomCentre = new Point(this.getPosX(), marioBounds.bottom());

        for (Ladder ladder : ladders) {
            Rectangle ladderBounds = ladder.getBounds();
            // Track when mario is inside/climbing a ladder
            if (marioBounds.intersects(ladder.getBounds()) && isCenterLadder(ladder)) {
                this.onLadder = true;
                this.hitPlatform = marioBounds.bottom() == ladderBounds.bottom();
            }
            // Track when mario is on top of a ladder
            if (ladderBounds.intersects(MarioBottomCentre)) {
                this.canClimbDown = true;
            }
        }
    }


    private boolean isCenterLadder(Ladder ladder) {
        double centerX = this.getPosX();
        return (centerX >= ladder.getBounds().left()) && (centerX <= ladder.getBounds().right());
    }


    /**
     * Handle jumping based on keyboard input.
     */
    private void allowJumping(Input input) {
        if (input.wasPressed(Keys.SPACE)) {
            if (!isJumping && onPlatform) {
                isJumping = true;
                jumpStartY = this.getPosY();
                velocityY = INIT_JUMP_VELOCITY;
                onPlatform = false;
            }
        }

        // Handle jumping if exceeds the maximum jumping height
        if (isJumping && ((jumpStartY - this.getPosY())) >= MAX_JUMP_HEIGHT) {
            isJumping = false;
            velocityY = 0;
        }
    }


    /**
     * Checks and returns a platform if Mario is on top of any one in the array.
     */
    private Platform onThisPlatform(ArrayList<Platform> platforms) {
        Point marioBottomCentre = new Point(this.getPosX(), this.getBounds().bottom());

        for(Platform platform : platforms) {
            Rectangle platformBounds = platform.getBounds();
            if (platformBounds.intersects(marioBottomCentre) && this.getPosY() < platform.getPosY()) {
                this.onPlatform = true;
                return platform;
            } else {
                this.onPlatform = false;
            }
        }
        return null;
    }


    /**
     * Limits Mario to the screen edges.
     */
    private void limitToScreenEdges() {
        double marioWidth = this.currImage.getWidth();
        double maxX = Math.max(marioWidth/2.0, Math.min(this.getPosX(),
                (ShadowDonkeyKong.getScreenWidth() - marioWidth/2.0)));
        this.setPosX(maxX);
    }


    /**
     * Gets the previous position of Mario to help handling barrel jumping-over detection.
     */
    private void getPreviousPos() {
        this.marioPrevPos = new Point(this.getPosX(), this.getPosY());
    }


    /**
     * Update Mario's image based on the current state of collection.
     */
    private void updateImage() {
        if (hasHammer) {
            this.currImage = (facingRight) ? MARIO_HAMMER_RIGHT : MARIO_HAMMER_LEFT;
        } else if (hasBlaster) {
            this.currImage = (facingRight) ? MARIO_BLASTER_RIGHT : MARIO_BLASTER_LEFT;
        } else {
            this.currImage = (facingRight) ? MARIO_RIGHT_IMG : MARIO_LEFT_IMG;
        }
    }


    /**
     * Checks if Mario is currently holding a hammer.
     *
     * @return true if Mario has a hammer
     */
    public boolean hasHammer() {
        return hasHammer;
    }


    /**
     * Gets the number of bullets Mario currently holds.
     *
     * @return The current bullet count
     */
    public int getBulletCount() {
        return bulletCount;
    }


    /**
     * Checks whether Mario is currently facing right.
     *
     * @return true if facing right, false if facing left
     */
    public boolean isFacingRight() {
        return facingRight;
    }
}
