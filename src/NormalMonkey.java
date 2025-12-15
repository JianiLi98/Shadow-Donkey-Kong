import bagel.*;
import java.util.ArrayList;

public class NormalMonkey extends Monkey {

    private static final Image NORMAL_MONKEY_RIGHT = new Image("res/normal_monkey_right.png");
    private static final Image NORMAL_MONKEY_LEFT = new Image("res/normal_monkey_left.png");

    public NormalMonkey(double x, double y, boolean facingRight, ArrayList<Integer> route) {
        super(x, y, facingRight, route, NORMAL_MONKEY_RIGHT, NORMAL_MONKEY_LEFT);
    }
}
