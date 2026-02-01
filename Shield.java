/**
 * The Shield class represents a shield power-up in the maze
 * @author: Neha Sharma
 * Collaborators: Kaushiki Kapoor, Bradley Nguyen
 * Teacher Name: Mrs. Bailey
 * Period: 1
 * Due Date: 05-12-25
 */
import java.awt.*;

public class Shield extends Icons {
    /** Tracks whether the score has already been incremented for this shield. */
    private boolean scoreIncremented = false;
    /** Indicates whether the shield effect is currently active on the duck. */
    private boolean effectActive = false;

    /**
     * Constructs a Shield power-up at the specified position.
     * @param x The x-coordinate of the shield.
     * @param y The y-coordinate of the shield.
     */
    public Shield(int x, int y) {
        super(x, y, "shield");
    }

    /**
     * Applies the shield effect to the duck if it is not already active.
     * @param duck The PlayerDuck receiving the shield effect.
     */
    public void applyEffect(PlayerDuck duck) {
        if (!effectActive) {
            duck.activateShield();
            effectActive = true;
        }
    }

    /**
     * Determines whether the shield should be removed from the maze.
     * @return true if the score has already been incremented (i.e., collected).
     */
    @Override
    public boolean shouldRemove() {
        return scoreIncremented;
    }

    /**
     * Indicates that this is not an obstacle.
     * @return false since the shield does not block movement.
     */
    @Override
    public boolean isObstacle() {
        return false;
    }

    /**
     * Draws the shield
     * @param g The Graphics object used to render the shield.
     */
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.drawOval(x, y, 20, 20); // Outline
        g.setColor(Color.LIGHT_GRAY);
        g.fillOval(x + 5, y + 5, 10, 10); // Center fill
    }

    /**
     * Handles the duck’s collision with the shield
     * @param duck The PlayerDuck interacting with the shield.
     * @param scoreManager The ScoreManager for updating the score.
     * @param b Unused flag (may indicate jump in other icons).
     */
    @Override
    public void handleCollision(PlayerDuck duck, ScoreManager scoreManager, boolean b) {
        if (!scoreIncremented && scoreManager != null) {
            scoreManager.addPoints(5);
            scoreIncremented = true;
        }
        applyEffect(duck);
    }

    /**
     * Gets the bounding rectangle for collision detection.
     * @return A Rectangle representing the area of the shield.
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 25, 25);
    }

    /**
     * Resets the shield effect and score flag
     */
    public void resetShieldEffect() {
        effectActive = false;
        scoreIncremented = false;
    }
}