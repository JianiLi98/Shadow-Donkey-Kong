import bagel.*;
import java.util.Properties;

/**
 * A class representing the home screen of the game.
 * Displays the game title and prompt message for starting level 1 or level 2.
 */
public class HomeScreen implements GameScreen {

    private final Properties GAME_PROPS;

    // Loads Home's texts, fonts, and position
    private final String TITLE;
    private final String PROMPT;

    // Fonts for displaying title and prompt messages
    private final Font TITLE_FONT;
    private final Font PROMPT_FONT;

    // Vertical position for the messages
    private final double TITLE_Y;
    private final double PROMPT_Y;

    // To indicate user's selected level to play
    private GameState targetLevel;


    /**
     * Constructs the HomeScreen, loading images, fonts, and text properties.
     *
     * @param gameProps Properties file containing image paths and font details.
     * @param messageProps  Properties file containing title and prompt text.
     */
    public HomeScreen(Properties gameProps, Properties messageProps) {
        this.GAME_PROPS = gameProps;

        // Dynamically load texts, fonts and positions from the properties files
        TITLE = messageProps.getProperty("home.title");
        PROMPT = messageProps.getProperty("home.prompt");

        String font = gameProps.getProperty("font");
        TITLE_FONT = new Font(font, getInt("home.title.fontSize"));
        PROMPT_FONT = new Font(font, getInt("home.prompt.fontSize"));

        TITLE_Y = getDouble("home.title.y");
        PROMPT_Y = getDouble("home.prompt.y");
    }


    /**
     * Handles entering game and renders the Home screen with the title, prompt and background.
     * Waits for the player to press ENTER or 2 as keyboard input to proceed.
     *
     * @param input keyboard input for update logic
     * @return true if the user has selected to start any level, false otherwise.
     */
    @Override
    public boolean update(Input input) {
        showScreenText();

        if (input.wasPressed(Keys.ENTER)) {
            targetLevel = GameState.GAME_PLAY_LEVEL1;
            return true;
        } else if (input.wasPressed(Keys.NUM_2)) {
            targetLevel = GameState.GAME_PLAY_LEVEL2;
            return true;
        }
        return false;
    }


    /**
     * Renders the home screen including title and prompt message.
     */
    @Override
    public void showScreenText() {
        TITLE_FONT.drawString(TITLE, textCentreX(TITLE_FONT, TITLE), TITLE_Y);
        PROMPT_FONT.drawString(PROMPT, textCentreX(PROMPT_FONT, PROMPT), PROMPT_Y);
    }


    /**
     * Check which level is chosen to play.
     * @return an int representing the chosen level.
     */
    public GameState getTargetLevel() {
        return targetLevel;
    }


    /**
     * Helper method to retrieves an integer value from the game properties file.
     *
     * @param infoName The key of the property
     * @return The integer value associated with the key
     */
    private int getInt(String infoName) {
        return Integer.parseInt(GAME_PROPS.getProperty(infoName));
    }


    /**
     * Helper method to retrieves a double value from the game properties file.
     *
     * @param infoName The key of the property
     * @return The double value associated with the key
     */
    private double getDouble(String infoName) {
        return Double.parseDouble(GAME_PROPS.getProperty(infoName));
    }


    /**
     * Calculates the x-coordinate required to center a given text string horizontally on screen.
     *
     * @param font The font used to measure the text width
     * @param text The text to be centered
     * @return The x-coordinate to center the text on screen
     */
    private double textCentreX(Font font, String text) {
        return (ShadowDonkeyKong.getScreenWidth() - font.getWidth(text)) / 2.0;
    }
}
