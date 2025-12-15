import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * The base class for all game entities that support rendering.
 *
 * Each Entity has a position, an image, and ability to generate its
 * bounding box for collision checking, and to be drawn.
 *
 * Subclasses override behaviours such as draw() and getBounds()
 * to allow specific rendering and collision checking.
 */
public abstract class Entity {

    // Entities' instance variables for image and position:
    private final Image entityImage;
    private Point position;


    public Entity(double x, double y, Image entityImage) {
        this.position = new Point(x, y);
        this.entityImage = entityImage;
    }

    /**
     * Draws the entities' image from the centre at its position.
     */
    public void draw() {
        this.entityImage.draw(this.getPosX(), this.getPosY());
    }

    /**
     * Gets bounding rectangle box for collision checks.
     */
    protected Rectangle getBounds() {
        return this.entityImage.getBoundingBoxAt(this.position);
    }

    /**
     * Returns half the height of the entity’s image.
     *
     * @param image The image to evaluate
     * @return Half of the image’s height
     */
    protected double halfImageHeight(Image image) {
        return image.getHeight() / 2.0;
    }

    /**
     * Returns the current position of the entity.
     *
     * @return The position as a Point
     */
    protected Point getPos() {
        return this.position;
    }

    /**
     * Returns the current x-coordinate of the entity.
     *
     * @return The x-coordinate
     */
    protected double getPosX() {
        return this.position.x;
    }

    /**
     * Returns the current y-coordinate of the entity.
     *
     * @return The y-coordinate
     */
    protected double getPosY() {
        return this.position.y;
    }


    /**
     * Sets the x-coordinate of the entity's position.
     *
     * @param x The new x-coordinate
     */
    protected void setPosX(double x) {
        this.position = new Point(x, this.position.y);
    }

    /**
     * Sets the y-coordinate of the entity's position.
     *
     * @param y The new y-coordinate
     */
    protected void setPosY(double y) {
        this.position = new Point(this.position.x, y);
    }

    /**
     * Returns the image associated with the entity.
     *
     * @return The entity's image
     */
    protected Image getEntityImage() {
        return this.entityImage;
    }
}
