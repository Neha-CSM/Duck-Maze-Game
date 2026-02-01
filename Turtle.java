/**
 * The Turtle class represents a turtle obstacle in the maze.
 * @author: Neha Sharma
 * Collaborators: Kaushiki Kapoor, Bradley Nguyen
 * Teacher Name: Mrs. Bailey
 * Period: 1
 * Due Date: 05-12-25
 */
import java.awt.*;

public class Turtle extends Icons {
    private boolean wasHit = false;
    private boolean used = false;
    private Lightning lightningRef;

    public Turtle(int x, int y, Lightning lightningRef) {
        super(x, y, "turtle");
        this.lightningRef = lightningRef;
    }

    public void applyEffect(PlayerDuck duck) {
    }

    @Override
    public boolean shouldRemove() {
        return wasHit || (lightningRef != null && lightningRef.isEffectActive());
    }

    @Override
    public void handleCollision(PlayerDuck duck, ScoreManager scoreManager, boolean isJumping) {
        if (isJumping) {
            if (!used) {
                scoreManager.addPoints(20);
                used = true;
            }
        } else {
            if (!wasHit) {
                wasHit = true; // one-time collision handling
                scoreManager.losePoints(25, duck); 
            }
        }
    }

    @Override
    public boolean isObstacle() {
        return true;
    }

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

    @Override
    public Rectangle getFrontBounds() {
        return new Rectangle(x + 20, y, 10, 30);
    }

    @Override
    public Rectangle getBackBounds() {
        return new Rectangle(x, y, 5, 30);
    }
}