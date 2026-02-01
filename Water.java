/**
 * The Water class represents a water trap obstacle in the maze.
 * @author: Neha Sharma
 * Collaborators: Kaushiki Kapoor, Bradley Nguyen
 */
import java.awt.*;

public class Water extends Icons {
    private boolean used = false;

    /**
     * Constructs a Water trap
     * @param x The x-coordinate of the water.
     * @param y The y-coordinate of the water.
     */
    public Water(int x, int y) {
        super(x, y, "water");
    }

    /**
     * Determines if the water should be removed from the game.
     * @return true if the water was hit.
     */
    @Override
    public boolean shouldRemove() {
        return wasHit;
    }

    /**
     * Draws the water
     * @param g The Graphics object used to draw the water.
     */
    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(100, 149, 237));
        g.fillOval(x + 2, y + 2, 20, 20);
        g.setColor(new Color(135, 206, 250));
        g.fillOval(x + 7, y + 7, 11, 11);
        g.setColor(Color.WHITE);
        g.fillOval(x + 11, y + 11, 4, 4);
    }

    /**
     * Checks whether the water is an obstacle.
     * @return true, as the water is an obstacle.
     */
    @Override
    public boolean isObstacle() {
        return true;
    }

    /**
     * Handles the collision between the duck and the water.
     * @param duck The PlayerDuck object that collided with the water.
     * @param scoreManager The ScoreManager to update the score.
     * @param isJumping Whether the duck is jumping.
     */
    @Override
    public void handleCollision(PlayerDuck duck, ScoreManager scoreManager, boolean isJumping) {
        if (isJumping) {
            if (!used) {
                scoreManager.addPoints(10);
                used = true;
            }
        } else {
            if (!wasHit) {
                wasHit = true;
                scoreManager.losePoints(15, duck);
            }
        }
    }

    /**
     * Returns the front bounding rectangle for collision detection.
     * @return A Rectangle representing the front of the water.
     */
    @Override
    public Rectangle getFrontBounds() {
        return new Rectangle(x + 21, y + 6, 3, 13);
    }

    /**
     * Returns the back bounding rectangle for collision detection.
     * @return A Rectangle representing the back of the water.
     */
    @Override
    public Rectangle getBackBounds() {
        return new Rectangle(x + 1, y + 6, 3, 13);
    }
}
