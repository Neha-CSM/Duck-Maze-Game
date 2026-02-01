/**
 * The Feather class represents a feather power-up in the maze so that when
 * collected it gives the duck super strength
 * @author: Neha Sharma
 * Collaborators: Kaushiki Kapoor, Bradley Nguyen
 */
import java.awt.*;

public class Feather extends Icons {
    private boolean used = false; // Tracks if score has already been incremented

    /**
     * Constructs a Feather
     * @param x The x-coordinate of the feather.
     * @param y The y-coordinate of the feather.
     */
    public Feather(int x, int y) {
        super(x, y, "feather");
    }

    /**
     * Determines if the feather should be removed from the game.
     * @return true if the feather has been used.
     */
    @Override
    public boolean shouldRemove() {
        return used;
    }

    /**
     * Checks whether the feather is an obstacle.
     * @return false, as the feather is not an obstacle.
     */
    public boolean isObstacle() {
        return false;
    }

    /**
     * Draws the feather
     * @param g The Graphics object used to draw the feather.
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2)); // Shaft
        g2.drawLine(x + 10, y + 4, x + 10, y + 18);
        g2.setColor(Color.GRAY); // Barbs
        g2.setStroke(new BasicStroke(1));
        for (int i = 0; i < 6; i++) {
            int yOff = 4 + i * 2;
            int left = (int)(x + 10 - i * 0.7);
            int right = (int)(x + 10 + i * 0.7);
            g2.drawLine(x + 10, y + yOff, left, y + yOff - 3);
            g2.drawLine(x + 10, y + yOff, right, y + yOff - 3);
        }
        g2.setColor(Color.BLACK); // Outline
        g2.drawOval(x + 4, y, 12, 14);
    }

    /**
     * Handles the collision between the duck and the feather.
     * @param duck The PlayerDuck object that collided with the feather.
     * @param scoreManager The ScoreManager to update the score.
     * @param b Unused boolean parameter (reserved for future use or compatibility).
     */
    public void handleCollision(PlayerDuck duck, ScoreManager scoreManager, boolean b) {
        if (!used && scoreManager != null) {
            scoreManager.addPoints(10);
            used = true;
        }
    }

    /**
     * Returns the bounding rectangle for collision detection.
     * @return A Rectangle object representing the bounds of the feather.
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 25, 25);
    }

}
