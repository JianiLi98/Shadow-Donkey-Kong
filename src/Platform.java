import bagel.*;

/**
 * This class represents stationary platform entity.
 */
public class Platform extends Entity {

    private static final Image PLATFORM_IMG = new Image("res/platform.png");

    public Platform(double x, double y) {
        super(x, y, PLATFORM_IMG);
    }
}