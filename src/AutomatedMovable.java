/**
 * This interface defines the contract for all entities in the game
 * that moves based on its own movement logic during update loops, without player input.
 * Used for entities including Bullet, Banana, and Monkey in the game.
 */
public interface AutomatedMovable {

    /**
     * Updates the entity’s movement based on its own logic (direction, speed).
     */
    void move();
}
