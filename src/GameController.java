import bagel.*;
import java.util.Properties;

/**
 * Orchestrates the main game loop and screen state management for the Shadow Donkey Kong game.
 *
 * This class controls transitions between HomeScreen, Level1, Level2, and GameOver screens.
 * Tracks global state (score, remaining time, win/lose), delegating per‐frame input processing,
 * initialising, entity updates, and rendering to current screen.
 */
public class GameController {

    // Properties resources:
    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;

    // The current game screen and game state for transition handling:
    private GameScreen currentScreen;
    private GameState currentState;

    private int totalScore = 0;


    public GameController(Properties gameProps, Properties messageProps) {
        this.GAME_PROPS = gameProps;
        this.MESSAGE_PROPS = messageProps;

        this.currentState  = GameState.HOME;
        this.currentScreen = new HomeScreen(GAME_PROPS, MESSAGE_PROPS);
    }


    /**
     * Updates the current game screen and handles transitions between screens.
     *
     * @param input The current mouse/keyboard input.
     */
    public void updateGame(Input input) {
        boolean shouldTransition = currentScreen.update(input);
        currentScreen.showScreenText();

        switch (currentState) {
            case HOME:
                HomeScreen home = (HomeScreen) currentScreen;
                // Switch to a user-selected GamePlay screen when Home's update (user) requires
                if (shouldTransition) {
                    this.totalScore = 0;
                    GameState targetLevel = home.getTargetLevel();
                    if (targetLevel == GameState.GAME_PLAY_LEVEL1) {
                        this.currentScreen = new GamePlayLevel1(GAME_PROPS);
                        this.currentState = GameState.GAME_PLAY_LEVEL1;
                    } else {
                        this.currentScreen = new GamePlayLevel2(GAME_PROPS);
                        this.currentState = GameState.GAME_PLAY_LEVEL2;
                    }
                }
                break;

            case GAME_PLAY_LEVEL1:
                GamePlayScreen level1 = (GamePlayLevel1) currentScreen;
                // Switch to Level 2 if Level 1 is won, GameOver screen otherwise
                if (shouldTransition) {
                    updateScore(level1);
                    if (level1.isGameWon()) {
                        this.currentScreen = new GamePlayLevel2(GAME_PROPS);
                        this.currentState = GameState.GAME_PLAY_LEVEL2;
                    } else {
                        showGameOver(false);
                        this.currentState = GameState.GAME_OVER;
                    }
                }
                break;

            case GAME_PLAY_LEVEL2:
                GamePlayScreen level2 = (GamePlayLevel2) currentScreen;
                // Switch to GameOver screen when Level 2 is finished
                if (shouldTransition) {
                    updateScore(level2);
                    showGameOver(level2.isGameWon());
                    this.currentState = GameState.GAME_OVER;
                }
                break;

            case GAME_OVER:
                // Switch to Home when GameOver screen's update (user) requires
                if (shouldTransition) {
                    this.currentScreen = new HomeScreen(GAME_PROPS, MESSAGE_PROPS);
                    this.currentState = GameState.HOME;
                    this.totalScore = 0;
                }
                break;
        }
    }


    /**
     * Helps to create, transition to and render the GameOver screen with score and messages.
     *
     * @param gameWon decide whether to show win or lose message.
     */
    private void showGameOver(boolean gameWon) {
        // Initialise a new GameOver screen with win/lose status and total score
        GameOverScreen gameOver = new GameOverScreen(GAME_PROPS, MESSAGE_PROPS);
        gameOver.setIsWon(gameWon);
        gameOver.setFinalScore(totalScore);
        this.currentScreen = gameOver;
    }


    /**
     * Updates the current score.
     * Handles carrying over Level 1 score to Level 2. Set score to 0 when game is lost.
     */
    private void updateScore(GamePlayScreen screen) {
        if (screen.isGameWon()) {
            if (currentState == GameState.GAME_PLAY_LEVEL1) {
                this.totalScore += screen.getScore();
            } else {
                this.totalScore += screen.getScore() + screen.getTimeRemaining() * 3;
            }
        } else {
            this.totalScore = 0;
        }
    }
}