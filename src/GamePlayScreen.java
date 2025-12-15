import bagel.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Abstract superclass for gameplay screens in Shadow Donkey Kong.
 *
 * Provides common initialization logic, UI rendering support,
 * property access utilities, and shared constants for both levels.
 */
public abstract class GamePlayScreen implements GameScreen {

    private final Properties GAME_PROPS;

    // Constants for frame and score tracking
    private final int MAX_FRAMES;
    private static final int FRAMES_PER_SECOND = 60;
    private static final int BARREL_DESTROY_SCORE = 100;
    private static final int BARREL_CROSS_SCORE = 30;

    // Constants for GamePlay screen's texts
    private static final String SCORE_MESSAGE = "SCORE ";
    private static final String TIME_MESSAGE = "TIME LEFT ";

    // Fonts for displaying GamePlay texts
    private final Font GAME_PLAY_FONT;

    // Load positions for the displayed texts
    private final double SCORE_X;
    private final double SCORE_Y;
    private final double TIME_DISPLAY_DIFF_Y = 30;


    /**
     * Constructs the gameplay screen, loading resources and initializing game objects.
     *
     * @param gameProps  Properties file containing game settings.
     */
    public GamePlayScreen(Properties gameProps) {
        this.GAME_PROPS = gameProps;

        // Load the required data from the file
        this.MAX_FRAMES = getInt("gamePlay.maxFrames");
        this.GAME_PLAY_FONT = new Font(gameProps.getProperty("font"), getInt("gamePlay.score.fontSize"));
        this.SCORE_X = getDouble("gamePlay.score.x");
        this.SCORE_Y = getDouble("gamePlay.score.y");
    }


    /**
     * Updates the game state and entities for a given frame.
     *
     * @param input The current input from the keyboard
     * @return true if the level has ended, false otherwise
     */
    @Override
    public abstract boolean update(Input input);


    /**
     * Displays the score and time remaining text for the gameplay screen.
     */
    @Override
    public abstract void showScreenText();


    /**
     * Returns the player's current score.
     *
     * @return The player's score.
     */
    public abstract int getScore();


    /**
     * Returns the remaining time left in the level, in seconds.
     *
     * @return The number of seconds remaining before the game ends.
     */
    public abstract int getTimeRemaining();


    /**
     * Indicates whether the game was won.
     *
     * @return true if the player completed the level, false otherwise
     */
    public abstract boolean isGameWon();


    /**
     * Here provides a series of shared logic for initialising all game entities for
     * both game levels, reading from app.properties file.
     *
     * Creates and returns the Mario entity for the specified level.
     *
     * @param level The level number (1 or 2)
     * @return Mario instance positioned as specified in properties
     */
    public Mario initialiseMario(int level) {
        String[] marioPos = GAME_PROPS.getProperty("mario.level" + level).split(",");
        double marioX = Double.parseDouble(marioPos[0]);
        double marioY = Double.parseDouble(marioPos[1]);
        return new Mario(marioX, marioY);
    }


    /**
     * Creates and returns the Donkey Kong entity for the specified level.
     *
     * @param level The level number (1 or 2)
     * @return Donkey instance positioned as specified in properties
     */
    public Donkey initialiseDonkey(int level) {
        String[] donkeyPos = GAME_PROPS.getProperty("donkey.level" + level).split(",");
        double donkeyX = Double.parseDouble(donkeyPos[0]);
        double donkeyY = Double.parseDouble(donkeyPos[1]);
        return new Donkey(donkeyX, donkeyY);
    }


    /**
     * Creates and returns all platforms for the given level.
     *
     * @param level The level number (1 or 2)
     * @return A list of Platform instances
     */
    public ArrayList<Platform> initialisePlatforms(int level) {
        ArrayList<Platform> platforms = new ArrayList<>();
        String[] allPlatformPos = GAME_PROPS.getProperty("platforms.level" + level).split(";");
        for (String pos : allPlatformPos) {
            String[] platformPos = pos.split(",");
            double platformX = Double.parseDouble(platformPos[0]);
            double platformY = Double.parseDouble(platformPos[1]);
            platforms.add(new Platform(platformX, platformY));
        }
        return platforms;
    }


    /**
     * Creates and returns all ladders for the given level.
     *
     * @param level The level number (1 or 2)
     * @return A list of Ladder instances
     */
    public ArrayList<Ladder> initialiseLadders(int level) {
        ArrayList<Ladder> ladders = new ArrayList<>();
        int ladderCount = getInt("ladder.level" + level + ".count");
        for (int i = 1; i <= ladderCount; i++) {
            String[] ladderPos = GAME_PROPS.getProperty("ladder.level" + level + "." + i).split(",");
            double ladderX = Double.parseDouble(ladderPos[0]);
            double ladderY = Double.parseDouble(ladderPos[1]);
            ladders.add(new Ladder(ladderX, ladderY));
        }
        return ladders;
    }


    /**
     * Creates and returns all barrels for the given level.
     *
     * @param level The level number (1 or 2)
     * @return A list of Barrel instances
     */
    public ArrayList<Barrel> initialiseBarrels(int level) {
        ArrayList<Barrel> barrels = new ArrayList<>();
        int barrelCount = getInt("barrel.level"  + level + ".count");
        for (int i = 1; i <= barrelCount; i++) {
            String[] barrelPos = GAME_PROPS.getProperty("barrel.level" + level + "." + i).split(",");
            double barrelX = Double.parseDouble(barrelPos[0]);
            double barrelY = Double.parseDouble(barrelPos[1]);
            barrels.add(new Barrel(barrelX, barrelY));
        }
        return barrels;
    }


    /**
     * Initializes the Hammer object for a given level.
     * (Stated in README's assumption that each level has only one hammer)
     *
     * @param level The level number (1 or 2)
     * @return A Hammer instance or null if not defined
     */
    public Hammer initialiseHammer(int level) {
        String key = "hammer.level" + level + ".1";
        if (GAME_PROPS.containsKey(key)) {
            String[] hammerPos = GAME_PROPS.getProperty(key).split(",");
            double hammerX = Double.parseDouble(hammerPos[0]);
            double hammerY = Double.parseDouble(hammerPos[1]);
            return new Hammer(hammerX, hammerY);
        }
        return null;
    }


    /**
     *
     * Helper method to retrieves an integer value from the game properties file.
     *
     * @param infoName The key of the property
     * @return The integer value associated with the key
     */
    public int getInt(String infoName) {
        return Integer.parseInt(GAME_PROPS.getProperty(infoName));
    }

    /**
     * Helper method to retrieves a double value from the game properties file.
     *
     * @param infoName The key of the property
     * @return The double value associated with the key
     */
    public double getDouble(String infoName) {
        return Double.parseDouble(GAME_PROPS.getProperty(infoName));
    }

    /**
     * Gets the common font for GamePlay screen
     * @return The font used for displaying score and time.
     */
    public Font getGamePlayFont() {
        return GAME_PLAY_FONT;
    }

    /**
     * Gets the maximum number of frames allowed in a level.
     * @return Maximum frame count.
     */
    public int getMaxFrames() {
        return MAX_FRAMES;
    }

    /**
     * Gets the number of frames rendered per second.
     * @return Frame rate used to calculate time.
     */
    public int getFramesPerSecond() {
        return FRAMES_PER_SECOND;
    }

    /**
     * Gets the X-coordinate for score display.
     * @return X-position of score text.
     */
    public double getScoreX() {
        return SCORE_X;
    }

    /**
     * Gets the Y-coordinate for score display.
     * @return Y-position of score text.
     */
    public double getScoreY() {
        return SCORE_Y;
    }

    /**
     * Gets the vertical offset between score and time text.
     * @return Difference in Y-position between score and time.
     */
    public double getTimeDisplayDiffY() {
        return TIME_DISPLAY_DIFF_Y;
    }

    /**
     * Gets the prefix string used for score display.
     * @return "SCORE " message label.
     */
    public String getScoreMessage() {
        return SCORE_MESSAGE;
    }

    /**
     * Gets the prefix string used for time display.
     * @return "TIME LEFT " message label.
     */
    public String getTimeMessage() {
        return TIME_MESSAGE;
    }

    /**
     * Gets the score awarded when jumping over a barrel.
     * @return Score value for jumping over a barrel.
     */
    public int getBarrelCrossScore() {
        return BARREL_CROSS_SCORE;
    }

    /**
     * Gets the score awarded when destroying a barrel.
     * @return Score value for destroying a barrel.
     */
    public int getBarrelDestroyScore() {
        return BARREL_DESTROY_SCORE;
    }
}
