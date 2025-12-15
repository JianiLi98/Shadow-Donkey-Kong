import bagel.*;
import java.util.ArrayList;

/**
 * Represents the intelligent monkey enemy that patrols and shoots bananas.
 *
 * Shoots a banana every 5 seconds and follows a route pattern defined in properties.
 * Can be destroyed by bullets or touched by Mario with a hammer.
 */
public class IntelligentMonkey extends Monkey {

    private static final Image INTELLI_MONKEY_RIGHT_IMG = new Image("res/intelli_monkey_right.png");
    private static final Image INTELLI_MONKEY_LEFT_IMG = new Image("res/intelli_monkey_left.png");
    private static final int FRAMES_PER_SECOND = 60;
    private final int SHOOTING_INTERVAL = 5;

    private int shootTimer = 0;


    public IntelligentMonkey(double x, double y, boolean facingRight, ArrayList<Integer> route) {
        super(x, y, facingRight, route, INTELLI_MONKEY_RIGHT_IMG, INTELLI_MONKEY_LEFT_IMG);
    }


    /**
     * Updates the monkey's state each frame by performing base movement logic
     * and incrementing the shooting timer.
     *
     * @param platforms The platforms used for gravity alignment and edge detection
     */
    @Override
    public void update(ArrayList<Platform> platforms) {
        super.update(platforms);

        shootTimer++;
    }


    /**
     * Called externally to check if it is time to shoot a banana.
     * If it is, resets timer and returns a new Banana; otherwise returns null.
     *
     * @return A new Banana if it's time to shoot, otherwise null
     */
    public Banana shouldShootBanana() {
        // Get the timer in seconds to check shooting interval
        int shootTimerSeconds = shootTimer / FRAMES_PER_SECOND;

        // Create a new banana if it is alive and has reached its shooting interval
        if (isAppear() && (shootTimerSeconds >= SHOOTING_INTERVAL)) {
            shootTimer = 0;
            return new Banana(this.getPosX(), this.getPosY(), isFacingRight());
        }
        return null;
    }
}
