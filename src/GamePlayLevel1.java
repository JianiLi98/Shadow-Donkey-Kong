import bagel.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This class manages the gameplay logic and state for Level 1 of the game.
 *
 * This includes initializing game entities such as Mario, Donkey,
 * barrels, ladders, and platforms, as well as handling entity updates,
 * collision detection, score/time tracking, and determining win/loss conditions.
 */
public class GamePlayLevel1 extends GamePlayScreen {

    // Constant for loading correct data to initialise entities
    private static final int LEVEL = 1;

    // All game entities for Level1
    private ArrayList<Platform> platforms = new ArrayList<>();
    private ArrayList<Ladder> ladders = new ArrayList<>();
    private ArrayList<Barrel> barrels = new ArrayList<>();
    private Donkey donkey;
    private Hammer hammer;
    private Mario mario;

    // Level1 game states tracking variables
    private int score = 0;
    private int currFrame = 0;
    private int timeRemaining;
    private boolean gameWon = false;
    private boolean gameOver = false;


    public GamePlayLevel1(Properties gameProps) {
        super(gameProps);
        initialiseLevel1Entities();
    }


    /**
     * Sets up and initializes all the game entities for Level 1.
     */
    private void initialiseLevel1Entities() {
        this.donkey = initialiseDonkey(LEVEL);
        this.mario = initialiseMario(LEVEL);
        this.platforms = initialisePlatforms(LEVEL);
        this.ladders = initialiseLadders(LEVEL);
        this.barrels = initialiseBarrels(LEVEL);
        this.hammer = initialiseHammer(LEVEL);
    }


    /**
     * Called from main for delegation, then updates to the appropriate screens.
     * The main gameplay updating logic for Level 1 to calculate time remaining, check game win/lose
     * state, update entities, and render texts.
     *
     * @param input keyboard input for update logic
     */
    @Override
    public boolean update(Input input) {
        trackTime();

        // If game still on, render static entities
        drawPlatforms();
        drawHammer();

        // Update moving entities
        updateLadders();
        updateMario(input);
        updateDonkey();
        updateBarrels();

        // Then render texts for Level 1 Screen
        showScreenText();

        // Requires transitioning is game has ended (lost/won)
        return gameOver || gameWon;
    }


    /**
     * Renders the on-screen score and remaining time text.
     */
    @Override
    public void showScreenText() {
        // Shows current score
        getGamePlayFont().drawString(getScoreMessage() + this.score, getScoreX(), getScoreY());

        // Shows current time remaining
        getGamePlayFont().drawString(getTimeMessage() + this.timeRemaining, getScoreX(),
                getScoreY() + getTimeDisplayDiffY());
    }


    /**
     * Returns the current score for Level 1.
     *
     * @return The current score
     */
    @Override
    public int getScore() {
        return this.score;
    }


    /**
     * Returns the remaining time (in seconds) before game ends.
     *
     * @return The time remaining
     */
    @Override
    public int getTimeRemaining() {
        return this.timeRemaining;
    }


    /**
     * Returns whether the level was completed successfully.
     *
     * @return {@code true} if the player won, {@code false} otherwise
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
     * Updates Mario’s state, including interaction with platforms, hammers and ladders.
     *
     * @param input The current keyboard input
     */
    private void updateMario(Input input) {
        mario.checkHammers(hammer);
        mario.updateLevel1(input, platforms, ladders);
    }


    /**
     * Updates Donkey’s position and checks if Mario collides with Donkey.
     * Ends the game if a collision occurs.
     */
    private void updateDonkey() {
        donkey.update(platforms);

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
                    score += getBarrelDestroyScore();
                } else {
                    this.gameOver = true;
                    this.gameWon = false;
                }
            }
            barrel.update(platforms);
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
}
