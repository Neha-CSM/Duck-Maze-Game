/**
 * The Log class represents a log obstacle in the maze.
 * @author: Neha Sharma
 * Collaborators: Kaushiki Kapoor, Bradley Nguyen
 * Teacher Name: Mrs. Bailey
 * Period: 1
 * Due Date: 05-12-25
 */
import java.awt.*;

public class Log extends Icons {
    private boolean used = false;

    public Log(int x, int y) {
        super(x, y, "log");
    }

    @Override
    public boolean shouldRemove() {
        return wasHit;
    }

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
        return new Rectangle(x + 20, y, 5, 25);
    }

    @Override
    public Rectangle getBackBounds() {
        return new Rectangle(x, y, 5, 25);
    }
}