/**
 * Represents the current state of the game.
 *
 * This enum is used by the GameController to track which screen
 * is currently active and to control transitions between different
 * stages of the game.
 *
 * States:
 * - HOME: The home/start screen
 * - GAME_PLAY_LEVEL1: Gameplay screen for level 1
 * - GAME_PLAY_LEVEL2: Gameplay screen for level 2
 * - GAME_OVER: The game over screen after win or loss
 */
public enum GameState {
    HOME,
    GAME_PLAY_LEVEL1,
    GAME_PLAY_LEVEL2,
    GAME_OVER
}
