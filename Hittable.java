/**
 * The Hittable interface defines shared behaviors to ensure that all elements
 * can be drawn, detected for collision, and removed from the game.
 * @author: Neha Sharma
 * Collaborators: Kaushiki Kapoor, Bradley Nguyen
 */
import java.awt.*;

public interface Hittable {
    /**
     * Determines whether the object should be removed from the game
     * @return true if the object should be removed
     */
    boolean shouldRemove();

    /**
     * Draws the object
     * @param g the Graphics context to draw on
     */
    void draw(Graphics g);

    /**
     * Gets the bounding rectangle representing the front side of the object
     * @return the front collision bounds
     */
    Rectangle getFrontBounds(); // directional collision

    /**
     * Gets the bounding rectangle representing the back side of the object
     * @return the back collision bounds
     */
    Rectangle getBackBounds();

    /**
     * Gets the overall bounding rectangle for collision detection.
     * @return the full collision bounds
     */
    Rectangle getBounds();

    /**
     * Gets the X coordinate of the object.
     * @return the X position in pixels
     */
    int getX();

    /**
     * Gets the Y coordinate of the object.
     * @return the Y position in pixels
     */
    int getY();

    /**
     * Handles the interaction logic between the object and the duck
     * @param duck the player-controlled duck
     * @param scoreManager the score manager handling score updates
     * @param isJumping whether the duck is currently jumping
     */
    void handleCollision(PlayerDuck duck, ScoreManager scoreManager, boolean isJumping);

}
