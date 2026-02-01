/**
 * The PlayerDuck class represents the duck character, handling movement and
 * state changes like shield activation and lightning effects.
 * @author Bradley Nguyen
 * Collaborators: Neha Sharma,Kaushiki Kapoor
 * Teacher Name: Mrs. Bailey
 * Period: 1
 * Due Date: 05-12-25
 */
import java.awt.*;
import java.util.ArrayList;

public class PlayerDuck {
    private int x = 50, y = 50;
    private double dx = 0, dy = 0;
    private boolean jumping = false;
    private boolean onGround = true;
    private boolean shieldActive = false;
    private boolean lightningActive = false;
    private long lightningStartTime = 0;
    private DuckMazeGame mazeGame;
    private int originalY;
    private int targetXAfterJump = -1;
    private boolean jumpingOverObstacle = false;
    private long shieldActivatedTime = 0;
    private static final long SHIELD_DURATION = 5000; // 5 seconds

    /**
     * Constructor for the PlayerDuck class
     * @param x the initial x-coordinate of the duck
     * @param y the initial y-coordinate of the duck
     * @param mazeGame the instance of DuckMazeGame to interact with
     */
    public PlayerDuck(int x, int y, DuckMazeGame mazeGame) {
        this.x = x;
        this.y = y;
        this.mazeGame = mazeGame;
        this.originalY = y;
    }

    /**
     * Retrieves the current x-coordinate of the player duck.
     * @return the current x-coordinate of the duck
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the current y-coordinate of the player duck.
     * @return the current y-coordinate of the duck
     */
    public int getY() {
        return y;
    }

    // Handles the duck's movement
    public void move() {
        if (!onGround) {
            dy += 1; // Simulate gravity effect
        }
        int nextX = (int)(x + dx);
        int nextY = (int)(y + dy);
        int maxX = mazeGame.getTotalRows() * DuckMazeGame.TILE_SIZE;
        int maxY = mazeGame.getTotalRows() * DuckMazeGame.TILE_SIZE;

        // X-axis movement
        if (dx != 0) {
            if (mazeGame.checkMove(x, y, nextX, y, jumping)) {
                x = nextX;
            }
        }

        // Y-axis movement
        if (dy < 0) {
            if (mazeGame.checkMove(x, y, x, nextY, jumping)) {
                y = nextY;
            } else {
                dy = 0;
            }
        } else if (dy > 0) {
            if (jumpingOverObstacle) {
                if (y + dy >= originalY) {
                    y = originalY;
                    dy = 0;
                    jumping = false;
                    jumpingOverObstacle = false;
                    onGround = true;
                } else if (nextY <= maxY && mazeGame.checkMove(x, y, x, nextY, jumping)) {
                    y = nextY;
                    onGround = false;
                }
            } else {
                if (nextY <= maxY && mazeGame.checkMove(x, y, x, nextY, jumping)) {
                    y = nextY;
                    onGround = false;
                } else {
                    dy = 0;
                    onGround = true;
                    jumping = false;
                }
            }
        }

        if (y >= maxY) {
            y = maxY;
            dy = 0;
            onGround = true;
            jumping = false;
        }

        if (jumpingOverObstacle && targetXAfterJump != -1) {
            boolean doneJumping = (dx > 0 && x >= targetXAfterJump) || (dx < 0 && x <= targetXAfterJump);
            if (doneJumping || y >= originalY) {
                x = targetXAfterJump;
                y = originalY;
                dx = 0;
                dy = 0;
                jumpingOverObstacle = false;
                jumping = false;
                onGround = true;
            }
        }
    }

    /**
     * Checks if the duck is currently jumping.
     * @return true if the duck is jumping, false otherwise
     */
    public boolean isJumping() {
        return jumping;
    }

    /**
     * establishes jump target
     * @param targetX the target x-coordinate to jump towards
     * @param currentY the current y-coordinate of the duck
     */
    public void jump(int targetX, int currentY) {
        if (onGround) {
            dy = -8;
            dx = (targetX - x) / 5.0;
            jumping = true;
            jumpingOverObstacle = true;
            onGround = false;
            targetXAfterJump = targetX;
            originalY = currentY;
        }
    }

    /**
     * Check if the duck is jumping over an obstacle.
     * @return true if the duck is jumping over an obstacle, false otherwise
     */
    public boolean jumpObstacle() {
        return jumpingOverObstacle;
    }

    /**
     * Activates the shield for the duck, preventing point deduction for a
     * limited time.
     */
    public void activateShield() {
        shieldActive = true;
        shieldActivatedTime = System.currentTimeMillis(); // starts the clock
    }

    /**
     * Activates the lightning effect for the duck, allowing it to interact with
     * lightning hazards.
     */
    public void activateLightning() {
        lightningActive = true;
        lightningStartTime = System.currentTimeMillis();
    }

    /**
     * Checks if the lightning effect is still active
     * @return true if the lightning effect is active
     */
    public boolean isLightningActive() {
        return lightningActive && (System.currentTimeMillis() - lightningStartTime) < 5000;
    }

    /**
     * Checks if the shield is currently active,
     * @return true if the shield is active
     */
    public boolean isShieldActive() {
        if (shieldActive && (System.currentTimeMillis() - shieldActivatedTime < SHIELD_DURATION)) {
            return true;
        } else {
            shieldActive = false;
            return false;
        }
    }

    /**
     * Retrieves the hitbox for the player duck
     * @return the bounds of the duck as a Rectangle
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, 15, 15); // effective hitbox
    }

    /**
     * Draws the player duck on the screen
     * @param g the Graphics object
     */
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(x + 2, y + 4, 15, 19); // body
        g.setColor(Color.YELLOW);
        g.fillOval(x + 5, y - 2, 11, 9); // head
        g.setColor(Color.ORANGE);
        int[] xPts = {x + 11, x + 14, x + 11};
        int[] yPts = {y + 3, y + 4, y + 6};
        g.fillPolygon(xPts, yPts, 3); // beak
        g.setColor(Color.BLACK);
        g.fillOval(x + 8, y, 2, 2); // eyes
        g.fillOval(x + 11, y, 2, 2);
        g.setColor(Color.WHITE);
        g.fillOval(x + 8, y, 1, 1);
        g.fillOval(x + 11, y, 1, 1);
        g.setColor(Color.ORANGE);
        g.fillRect(x + 4, y + 21, 3, 2); // feet
        g.fillRect(x + 11, y + 21, 3, 2);
    }

    /**
     * Sets the horizontal speed of the duck.
     * @param dx the new horizontal speed of the duck
     */
    public void setDX(int dx) {
        this.dx = dx;
    }

    /**
     * Sets the vertical speed of the duck.
     * @param dy the new vertical speed of the duck
     */
    public void setDY(double dy) {
        this.dy = dy;
    }

    /**
     * Checks if there is an obstacle ahead of the duck
     * @param x: current x-coordinate of the duck
     * @param y: current y-coordinate of the duck
     * @return true if an obstacle is ahead, false otherwise
     */
    public boolean isObstacleAhead(int x, int y) {
        int col = x / DuckMazeGame.TILE_SIZE;
        int row = y / DuckMazeGame.TILE_SIZE;
        int nextCol;
        if (dx >= 0) {
            nextCol = col + 1;
        } else {
            nextCol = col - 1;
        }
        for (Hittable icon : mazeGame.getHittables()) {
            int iconCol = icon.getX() / DuckMazeGame.TILE_SIZE;
            int iconRow = icon.getY() / DuckMazeGame.TILE_SIZE;
            if ((icon instanceof Log || icon instanceof Water || icon instanceof Turtle) &&
                iconCol == nextCol && iconRow == row) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there is an obstacle behind the duck
     * @param x: current x-coordinate of the duck
     * @param y: current y-coordinate of the duck
     * @return true if an obstacle is behind
     */
    public boolean isObstacleBehind(int x, int y) {
        int col = x / DuckMazeGame.TILE_SIZE;
        int row = y / DuckMazeGame.TILE_SIZE;
        int prevCol;
        if (dx >= 0) {
            prevCol = col - 1;
        } else {
            prevCol = col + 1;
        }
        for (Hittable icon : mazeGame.getHittables()) {
            int iconCol = icon.getX() / DuckMazeGame.TILE_SIZE;
            int iconRow = icon.getY() / DuckMazeGame.TILE_SIZE;
            if ((icon instanceof Log || icon instanceof Water || icon instanceof Turtle) &&
                iconCol == prevCol && iconRow == row) {
                return true;
            }
        }
        return false;
    }
}