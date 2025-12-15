import bagel.*;
import java.util.Properties;

/**
 * Represents the screen displayed at the end of the game.
 * It shows whether the player won or lost, displays the final score,
 * and waits for the player to press SPACE to continue.
 */
public class GameOverScreen implements GameScreen {

    private final Properties GAME_PROPS;

    // Loads GameOver screen's texts
    private final String GAME_WON_TXT;
    private final String GAME_LOST_TXT;
    private final String CONTINUE_GAME_TXT;
    private final String SCORE_MESSAGE;

    // Fonts for displaying win/loss message and final score
    private final Font STATUS_FONT;
    private final Font SCORE_FONT;

    // Vertical position for the win/loss message
    private final double STATUS_Y;
    private final double MESSAGE_DIFF_Y_1 = 60.0;
    private final double MESSAGE_DIFF_Y_2 = 100.0;

    // The final score from this play through
    private int finalScore = 0;

    // Indicates whether the player won or lost
    private boolean gameWon;


    /**
     * Constructs the GameEndScreen, loading required resources such as images, fonts, and text.
     *
     * @param gameProps Properties file containing file paths and layout configurations.
     * @param messageProps  Properties file containing game messages and prompts.
     */
    public GameOverScreen(Properties gameProps, Properties messageProps) {
        this.GAME_PROPS = gameProps;

        // Dynamically load texts, fonts and positions from the properties files
        this.GAME_WON_TXT = messageProps.getProperty("gameEnd.won");
        this.GAME_LOST_TXT = messageProps.getProperty("gameEnd.lost");
        this.CONTINUE_GAME_TXT = messageProps.getProperty("gameEnd.continue");
        this.SCORE_MESSAGE = messageProps.getProperty("gameEnd.score");

        String font = gameProps.getProperty("font");
        this.STATUS_FONT = new Font(font, getInt("gameEnd.status.fontSize"));
        this.SCORE_FONT = new Font(font, getInt("gameEnd.scores.fontSize"));

        this.STATUS_Y = getDouble("gameEnd.status.y");
    }


    /**
     * Renders the game end screen, including the final score, win/loss message,
     * and a prompt for the player to continue. Also checks for user input to exit the screen.
     *
     * @param input The current user input.
     * @return {@code true} if the player presses SPACE to continue, {@code false} otherwise.
     */
    @Override
    public boolean update(Input input) {
        showScreenText();

        return input.wasPressed(Keys.SPACE);
    }


    /**
     * Renders the win/loss message, final score, and a prompt to continue
     * on the game over screen.
     */
    @Override
    public void showScreenText() {
        // Shows the status message
        String statusText = gameWon ? GAME_WON_TXT : GAME_LOST_TXT;
        STATUS_FONT.drawString(statusText, textCentreX(STATUS_FONT, statusText), STATUS_Y);

        // Shows the score
        String scoreText = SCORE_MESSAGE + " " + finalScore;
        SCORE_FONT.drawString(scoreText, textCentreX(SCORE_FONT, scoreText), STATUS_Y + MESSAGE_DIFF_Y_1);

        // Shows the continue prompt to go back to Home screen
        SCORE_FONT.drawString(CONTINUE_GAME_TXT, textCentreX(SCORE_FONT, CONTINUE_GAME_TXT),
                ShadowDonkeyKong.getScreenHeight() - MESSAGE_DIFF_Y_2);
    }


    /**
     * Sets whether the player won the game.
     *
     * @param isWon {@code true} if the player won, {@code false} if they lost.
     */
    public void setIsWon(boolean isWon) {
        this.gameWon = isWon;
    }


    /**
     * Sets the final score to be displayed on the game over screen.
     *
     * @param score The player's final score
     */
    public void setFinalScore(int score) {
        this.finalScore = score;
    }


    /**
     *
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
