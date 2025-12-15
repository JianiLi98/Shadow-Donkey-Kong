/**
 * This interface defines the contract for all entities in the game
 * that Mario can collect.
 */
public interface Collectable {

    /**
     * Called when Mario collides with this game entity.
     */
    void marioCollected();

    /**
     * Check if Mario has collected this entity.
     * @return true if already collected and should no longer appear
     */
    boolean isCollected();
}
