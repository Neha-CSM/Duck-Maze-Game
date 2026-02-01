/**
 * The Lightning class represents a lightning bolt power-up in the maze so that
 * when collected, all the turtles disappear for 5 seconds
 * @author: Neha Sharma
 * Collaborators: Kaushiki Kapoor, Bradley Nguyen
 * Teacher Name: Mrs. Bailey
 * Period: 1
 * Due Date: 05-12-25
 */
import java.awt.*;

public class Lightning extends Icons {
    private boolean used = false;
    private boolean effectActive = false;
    private long effectStartTime = 0;
    private static final long EFFECT_DURATION = 5000; // 5 seconds

    /**
     * Constructs a Lightning power-up
     * @param x The x-coordinate of the lightning icon.
     * @param y The y-coordinate of the lightning icon.
     */
    public Lightning(int x, int y) {
        super(x, y, "lightning");
    }

    /**
     * Applies the lightning effect to the duck and records the start time of effect
     * @param duck The player duck receiving the effect.
     */
    public void applyEffect(PlayerDuck duck) {
        if (!effectActive) {
            duck.activateLightning();
            effectActive = true;
            effectStartTime = System.currentTimeMillis();
        }
    }

    /**
     * Checks whether this lightning icon should be removed from the maze.
     * @return true if the lightning has been used/collected.
     */
    @Override
    public boolean shouldRemove() {
        return used;
    }

    /**
     * Handles the collision logic when the duck collides with the lightning power-up.
     * @param duck The PlayerDuck that collided with the lightning.
     * @param scoreManager The ScoreManager for updating the score.
     * @param isJumping Whether the duck was jumping at the time of collision (unused here).
     */
    @Override
    public void handleCollision(PlayerDuck duck, ScoreManager scoreManager, boolean isJumping) {
        if (!used) {
            if (scoreManager != null) {
                scoreManager.addPoints(5);
            }
            applyEffect(duck);
            used = true;
        }
    }

    /**
     * Returns whether the lightning effect is currently active and not expired.
     * @return true if the effect is active and within its duration.
     */
    public boolean isEffectActive() {
        return effectActive && !isEffectExpired();
    }

    /**
     * Checks whether the lightning effect duration has expired.
     * @return true if the effect has passed its duration.
     */
    public boolean isEffectExpired() {
        return (System.currentTimeMillis() - effectStartTime) >= EFFECT_DURATION;
    }

    /**
     * Resets the lightning effect status
     */
    public void resetLightningEffect() {
        effectActive = false;
    }

    /**
     * Draws the lightning icon
     * @param g The Graphics object used to render the icon.
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.MAGENTA);
        g2.setStroke(new BasicStroke(2));
        int[] xPoints = {x + 10, x + 13, x + 9, x + 12, x + 8};
        int[] yPoints = {y, y + 5, y + 10, y + 13, y + 19};
        for (int i = 0; i < xPoints.length - 1; i++) {
            g2.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
        }
    }

    /**
     * Returns whether this icon is an obstacle.
     * @return false since lightning is a power-up.
     */
    @Override
    public boolean isObstacle() {
        return false;
    }

    /**
     * Returns the bounding rectangle for the lightning
     * @return A Rectangle with fixed size (20x20).
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 20, 20);
    }
}