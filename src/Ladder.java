import bagel.*;

/**
 * This class represents the ladder entity that vertically connects platforms.
 */
public class Ladder extends GravityApplyingEntity {

    private static final Image LADDER_IMG = new Image("res/ladder.png");
    private static final double LADDER_GRAVITY = 0.25;

    public Ladder(double x, double y) {
        super(x, y, LADDER_IMG, LADDER_GRAVITY);
    }
}
