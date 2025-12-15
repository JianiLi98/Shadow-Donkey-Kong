import bagel.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Manages Level 2 GamePlay screen of the game.
 *
 * Controls the update and render of monkeys, bananas, blasters, bullets,
 * bullet count and Donkey Kong health, in addition to all level1 existing entities.
 */
public class GamePlayLevel2 extends GamePlayScreen {

    // Constant for loading correct data to initialise entities
    private static final int LEVEL = 2;

    // Constants for displaying texts and score tracking
    private static final int MONKEY_DESTROY_SCORE = 100;
    private static final String DONKEY_HEALTH_MESSAGE = "DONKEY HEALTH ";
    private static final String BULLET_MESSAGE = "BULLET ";

    // Load positions for the displayed texts of Donkey's health and Bullet count
    private final double DONKEY_HEALTH_X;
    private final double DONKEY_HEALTH_Y;
    private final double BULLET_DISPLAY_DIFF_Y = 30;

    // All game entities for Level2:
    private ArrayList<Platform> platforms = new ArrayList<>();
    private ArrayList<Ladder> ladders = new ArrayList<>();
    private ArrayList<Barrel> barrels = new ArrayList<>();
    private Hammer hammer;
    private Donkey donkey;
    private Mario mario;
    private ArrayList<Monkey> monkeys = new ArrayList<>();
    private ArrayList<Banana> bananas = new ArrayList<>();
    private ArrayList<Blaster> blasters = new ArrayList<>();
    private ArrayList<Bullet> bullets = new ArrayList<>();

    // Level2 game states tracking variables
    private int score = 0;
    private int currFrame = 0;
    private int timeRemaining;
    private boolean gameWon = false;
    private boolean gameOver = false;


    public GamePlayLevel2(Properties gameProps) {
        super(gameProps);

        // Load the required data from the file
        String[] donkeyHealthData = gameProps.getProperty("gamePlay.donkeyhealth.coords").split(",");
        this.DONKEY_HEALTH_X = Double.parseDouble(donkeyHealthData[0]);
        this.DONKEY_HEALTH_Y = Double.parseDouble(donkeyHealthData[1]);

        initialiseLevel2Entities(gameProps);
    }


    /**
     * Sets up and initializes all the game entities for Level 2.
     *
     * @param gameProps The properties file containing entity setup data
     */
    private void initialiseLevel2Entities(Properties gameProps) {
        this.mario = initialiseMario(LEVEL);
        this.donkey = initialiseDonkey(LEVEL);
        this.platforms = initialisePlatforms(LEVEL);
        this.ladders = initialiseLadders(LEVEL);
        this.barrels = initialiseBarrels(LEVEL);
        this.hammer = initialiseHammer(LEVEL);
        initialiseBlasters(gameProps);
        initialiseMonkeys(gameProps);
    }


    /**
     * Initialises and stores Monkey entities for Level 2.
     */
    private void initialiseMonkeys(Properties gameProps) {
        // Add in Normal Monkey entities
        int normalMonkeyCount = getInt("normalMonkey.level2.count");
        for (int i = 1; i <= normalMonkeyCount; i++) {
            String data = gameProps.getProperty("normalMonkey.level2." + i);
            monkeys.add(parseMonkey(data, false));
        }
        // Add in Intelligent Monkey entities
        int intelliMonkeyCount = getInt("intelligentMonkey.level2.count");
        for (int i = 1; i <= intelliMonkeyCount; i++) {
            String data = gameProps.getProperty("intelligentMonkey.level2." + i);
            monkeys.add(parseMonkey(data, true));
        }
    }


    /**
     * Help create an Intelligent or Normal Monkey entity based on data input
     * read from app.properties file.
     *
     * @param data one piece of data for initialising a Monkey.
     * @param intelligent flag if the Monkey is intelligent or not.
     * @return an initialised Monkey entity.
     */
    private Monkey parseMonkey(String data, boolean intelligent) {
        // Get the initial position and facing direction
        String[] allData = data.split(";");
        String[] monkeyPos = allData[0].split(",");
        double monkeyX = Double.parseDouble(monkeyPos[0]);
        double monkeyY = Double.parseDouble(monkeyPos[1]);
        boolean faceRight = allData[1].equals("right");

        // Build the ArrayList<Integer> route for patrolling movement
        String[] allRouteData = allData[2].split(",");
        ArrayList<Integer> route = new ArrayList<>();
        for (String routeData : allRouteData) {
            route.add(Integer.parseInt(routeData));
        }

        if (intelligent) {
            return new IntelligentMonkey(monkeyX, monkeyY, faceRight, route);
        } else {
            return new NormalMonkey(monkeyX, monkeyY, faceRight, route);
        }
    }


    /**
     * Initialises and stores Blaster entities for Level 2.
     */
    private void initialiseBlasters(Properties gameProps) {
        int blasterCount = getInt("blaster.level2.count");
        for (int i = 1; i <= blasterCount; i ++) {
            String[] blasterPos = gameProps.getProperty("blaster.level2." + i).split(",");
            double blasterX = Double.parseDouble(blasterPos[0]);
            double blasterY = Double.parseDouble(blasterPos[1]);
            this.blasters.add(new Blaster(blasterX, blasterY));
        }
    }


    /**
     * Called from main for delegation, then updates to the appropriate screens.
     * The main gameplay updating logic for Level 2 to calculate time remaining, check game win/lose
     * state, update entities, and render texts.
     * @param input keyboard input for update logic
     */
    @Override
    public boolean update(Input input) {
        trackTime();

        // If game still on, render static entities
        drawPlatforms();
        drawHammer();
        drawBlasters();

        // Update moving entities
        updateLadders();
        updateMario(input);
        updateDonkey();
        updateBarrels();
        updateMonkeys();
        updateBullets();

        // Then render texts for Level 1 Screen
        showScreenText();

        // Requires transitioning is game has ended (lost/won)
        return gameOver || gameWon;
    }


    /**
     * Draws the Level 2 screen texts,
     * Including score, time remaining, donkey health, and bullet count.
     */
    @Override
    public void showScreenText() {
        Font gamePlayFont = getGamePlayFont();

        // Shows current score
        gamePlayFont.drawString(getScoreMessage() + score, getScoreX(), getScoreY());

        // Shows current time remaining
        gamePlayFont.drawString(getTimeMessage() + timeRemaining, getScoreX(),
                getScoreY() + getTimeDisplayDiffY());

        // Shows Donkey's health
        gamePlayFont.drawString(DONKEY_HEALTH_MESSAGE + donkey.getHealth(), DONKEY_HEALTH_X, DONKEY_HEALTH_Y);

        // Shows number of bullets left
        gamePlayFont.drawString(BULLET_MESSAGE + mario.getBulletCount(),
                DONKEY_HEALTH_X, DONKEY_HEALTH_Y + BULLET_DISPLAY_DIFF_Y);
    }


    /**
     * Gets the score accumulated in Level 2.
     *
     * @return the level score.
     */
    @Override
    public int getScore() {
        return this.score;
    }


    /**
     * Gets the current time remaining in Level 2.
     *
     * @return the time remaining.
     */
    @Override
    public int getTimeRemaining() {
        return this.timeRemaining;
    }


    /**
     * Returns true if the player has won Level 2.
     *
     * @return true if Level 2 is won.
     */
    @Override
    public boolean isGameWon() {
        return this.gameWon;
    }


    /**
     * Tracks the remaining time based on frame count and sets game over
     * if time runs out.
     */
    private void trackTime() {
        // Keep track of current frame and calculate the remaining time
        this.currFrame ++;
        this.timeRemaining = (getMaxFrames() - currFrame) / getFramesPerSecond();

        // Check if game state should be updated to game over state
        if (timeRemaining <= 0) {
            this.gameWon = false;
            this.gameOver = true;
        }
    }


    /**
     * Updates Ladders' states, applying gravity if not aligned to platforms
     */
    private void updateLadders() {
        for (Ladder ladder : ladders) {
            ladder.update(platforms);
        }
    }


    /**
     * Updates Mario’s state, including interaction with platforms, hammers, blasters
     * and ladders. Also handles shooting and adding into the Bullet's ArrayList.
     *
     * @param input The current keyboard input
     */
    private void updateMario(Input input) {
        mario.checkHammers(hammer);
        mario.checkBlasters(blasters);

        // Add newly created Bullet to bullets if Mario (player) shoots
        if (mario.shootBullet(input) != null) {
            Bullet bullet = mario.shootBullet(input);
            bullets.add(bullet);
            mario.bulletSpent();
        }

        mario.updateLevel2(input, platforms, ladders);
    }


    /**
     * Updates Donkey’s position and checks if Mario collides with Donkey.
     * Ends the game if a collision occurs.
     */
    private void updateDonkey() {
        donkey.update(platforms);

        // Game ends and update game status to won when Donkey's health reaches 0
        if (donkey.getHealth() <= 0) {
            this.gameWon = true;
            this.gameOver = true;
        }
        // Handle Donkey's collision with Mario
        if (mario.marioCollidesWith(donkey)) {
            this.gameWon = mario.hasHammer();
            this.gameOver = true;
        }
    }


    /**
     * Updates each barrel's state and handles jumping or destroying barrels
     * for scoring and collisions for loss condition.
     */
    private void updateBarrels() {
        for (Barrel barrel : barrels) {
            // Handle being jumped over by Mario and update score
            if (mario.jumpedOverBarrel(barrel) && barrel.isAlive()) {
                this.score += getBarrelCrossScore();
            }
            // Handle collision with Mario and update score
            if (mario.marioCollidesWith(barrel) && barrel.isAlive()) {
                if (mario.hasHammer()) {
                    barrel.destroy();
                    this.score += getBarrelDestroyScore();
                } else {
                    this.gameOver = true;
                    this.gameWon = false;
                }
            }
            barrel.update(platforms);
        }
    }


    /**
     * Updates all monkey entities in the game.
     *
     * Handles movement, banana shooting by intelligent monkeys,
     * and collision detection with Mario.
     */
    private void updateMonkeys() {
        for (Monkey monkey : monkeys) {
            // Handles banana shooting by Intelligent Monkey
            if (monkey instanceof IntelligentMonkey intelliMonkey) {
                Banana b = intelliMonkey.shouldShootBanana();
                if (b != null) {
                    bananas.add(b);
                }
            }
            // Handles collision detection with Mario
            if (monkey.isAppear() && mario.marioCollidesWith(monkey)) {
                if (mario.hasHammer()) {
                    monkey.destroy();
                    this.score += MONKEY_DESTROY_SCORE;
                } else {
                    this.gameWon = false;
                    this.gameOver = true;
                }
            }
            monkey.update(platforms);
        }
        // Handles update of bananas within its shooter's update logic
        updateBananas();
    }


    /**
     * Updates all banana projectiles currently on screen.
     */
    private void updateBananas() {
        for (Banana banana : bananas) {
            // Handles movement and collision detection with Mario
            if (banana.isAppear() && mario.marioCollidesWith(banana)) {
                this.gameOver = true;
                this.gameWon = false;
            }
            banana.update();
        }
    }



    /**
     * Updates all bullet projectiles currently on screen.
     */
    private void updateBullets() {
        for (Bullet bullet : bullets) {
            // Handles collision detection with monkeys and Donkey
            bullet.collideWithDonkey(donkey);
            if (bullet.collideWithMonkeys(monkeys)) {
                this.score += MONKEY_DESTROY_SCORE;
            }
            bullet.update(platforms);
        }
    }


    /**
     * Draws all non-moving platform entities.
     */
    private void drawPlatforms() {
        for (Platform platform : platforms) {
            platform.draw();
        }
    }


    /**
     * Draws non-moving hammer entity.
     * (Stated in README's assumption that each level has only one hammer)
     */
    private void drawHammer() {
        hammer.draw();
    }


    /**
     * Draws all non-moving blaster entities.
     */
    private void drawBlasters() {
        for (Blaster blaster : blasters) {
            blaster.draw();
        }
    }
}
