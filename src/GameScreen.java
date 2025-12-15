import bagel.*;

/**
 * This interface defines the contract for all major game screens, including
 * the home screen, gameplay levels, and game over screen.
 * Each screen share common capabilities of updating its internal state based on
 * player input and rendering its visual elements to the screen.
 */
public interface GameScreen {

    /**
     * Updates this screen. Returns true if the screen is ready to transition.
     *
     * @param input keyboard input for update logic
     * @return true when need to switch to another screen, false otherwise.
     */
    boolean update(Input input);

    /**
     * Renders the current screen.
     */
    void showScreenText();
}
