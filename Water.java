/**
 * The Water class represents a water trap obstacle in the maze.
 * @author: Neha Sharma
 * Collaborators: Kaushiki Kapoor, Bradley Nguyen
 * Teacher Name: Mrs. Bailey
 * Period: 1
 * Due Date: 05-12-25
 */
import java.awt.*;

public class Water extends Icons {
    private boolean used = false;

    public Water(int x, int y) {
        super(x, y, "water");
    }

    @Override
    public boolean shouldRemove() {
        return wasHit;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(100, 149, 237));
        g.fillOval(x + 2, y + 2, 20, 20);
        g.setColor(new Color(135, 206, 250));
        g.fillOval(x + 7, y + 7, 11, 11);
        g.setColor(Color.WHITE);
        g.fillOval(x + 11, y + 11, 4, 4);
    }

    @Override
    public boolean isObstacle() {
        return true;
    }

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

    @Override
    public Rectangle getFrontBounds() {
        return new Rectangle(x + 21, y + 6, 3, 13);
    }

    @Override
    public Rectangle getBackBounds() {
        return new Rectangle(x + 1, y + 6, 3, 13);
    }
}