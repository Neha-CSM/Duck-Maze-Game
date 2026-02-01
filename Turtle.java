/**
 * The Turtle class represents a turtle obstacle in the maze.
 * @author: Neha Sharma
 * Collaborators: Kaushiki Kapoor, Bradley Nguyen
 */
import java.awt.*;

public class Turtle extends Icons {
    private boolean wasHit = false;
    private boolean used = false;
    private Lightning lightningRef;

    /**
     * Constructs a Turtle
     * @param x The x-coordinate of the turtle.
     * @param y The y-coordinate of the turtle.
     * @param lightningRef Reference to lightning for power-up logic.
     */
    public Turtle(int x, int y, Lightning lightningRef) {
        super(x, y, "turtle");
        this.lightningRef = lightningRef;
    }

    public void applyEffect(PlayerDuck duck) {
        // No effect for turtle
    }

    /**
     * Determines if the turtle should be removed from the game.
     * @return true if it was hit or if lightning is active.
     */
    @Override
    public boolean shouldRemove() {
        return wasHit || (lightningRef != null && lightningRef.isEffectActive());
    }

    /**
     * Handles the collision between the duck and the turtle.
     * @param duck The PlayerDuck object that collided with the turtle.
     * @param scoreManager The ScoreManager to update the score.
     * @param isJumping Whether the duck is jumping.
     */
    @Override
    public void handleCollision(PlayerDuck duck, ScoreManager scoreManager, boolean isJumping) {
        if (isJumping) {
            if (!used) {
                scoreManager.addPoints(20);
                used = true;
            }
        } else {
            if (!wasHit) {
                wasHit = true; 
                scoreManager.losePoints(25, duck); 
            }
        }
    }

    /**
     * Checks whether the turtle is an obstacle.
     * @return true, as the turtle is an obstacle.
     */
    @Override
    public boolean isObstacle() {
        return true;
    }

    /**
     * Draws the turtle
     * @param g The Graphics object used to draw the turtle.
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(1));
        g.setColor(new Color(0, 100, 0));
        g.fillArc(x, y + 6, 25, 25, 0, 180);
        g.fillRect(x + 4, y + 18, 4, 4);
        g.fillRect(x + 17, y + 18, 4, 4);
        g.setColor(new Color(144, 238, 144));
        g.fillOval(x + 20, y + 11, 9, 9);
        g.setColor(Color.BLACK);
        g.fillOval(x + 22, y + 13, 2, 2);
        g.fillOval(x + 26, y + 13, 2, 2);
        g.drawLine(x + 22, y + 12, x + 24, y + 14);
        g.drawLine(x + 28, y + 12, x + 26, y + 14);
        g.drawArc(x + 23, y + 16, 4, 3, 0, 180);
    }

    /**
     * Returns the front bounding rectangle for collision detection.
     * @return A Rectangle representing the front of the turtle.
     */
    @Override
    public Rectangle getFrontBounds() {
        return new Rectangle(x + 20, y, 10, 30);
    }

    /**
     * Returns the back bounding rectangle for collision detection.
     * @return A Rectangle representing the back of the turtle.
     */
    @Override
    public Rectangle getBackBounds() {
        return new Rectangle(x, y, 5, 30);
    }
}
