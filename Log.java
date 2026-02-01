/**
 * The Log class represents a log obstacle in the maze.
 * @author: Neha Sharma
 * Collaborators: Kaushiki Kapoor, Bradley Nguyen
 */
import java.awt.*;

public class Log extends Icons {
    private boolean used = false;

    /**
     * Constructs a Log
     * @param x The x-coordinate of the log.
     * @param y The y-coordinate of the log.
     */
    public Log(int x, int y) {
        super(x, y, "log");
    }

    /**
     * Determines if the log should be removed from the game.
     * @return true if the log was hit.
     */
    @Override
    public boolean shouldRemove() {
        return wasHit;
    }

    /**
     * Draws the log
     * @param g The Graphics object used to draw the log.
     */
    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(160, 82, 45));
        g.fillRect(x + 2, y + 6, 20, 12);
        g.setColor(new Color(101, 67, 33));
        g.drawRect(x + 2, y + 6, 20, 12);
        g.setColor(new Color(139, 69, 19));
        g.drawLine(x + 4, y + 9, x + 20, y + 9);
        g.drawLine(x + 4, y + 13, x + 20, y + 13);
    }

    /**
     * Checks whether the log is an obstacle.
     * @return true, as the log is an obstacle.
     */
    @Override
    public boolean isObstacle() {
        return true;
    }

    /**
     * Handles the collision between the duck and the log.
     * @param duck The PlayerDuck object that collided with the log.
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
     * @return A Rectangle representing the front of the log.
     */
    @Override
    public Rectangle getFrontBounds() {
        return new Rectangle(x + 20, y, 5, 25);
    }

    /**
     * Returns the back bounding rectangle for collision detection.
     * @return A Rectangle representing the back of the log.
     */
    @Override
    public Rectangle getBackBounds() {
        return new Rectangle(x, y, 5, 25);
    }
}
