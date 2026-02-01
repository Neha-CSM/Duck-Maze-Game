/**
 * Icons is an abstract class for all objects the duck can collide with
 * @author Neha Sharma
 * Collaborators: Kaushiki Kapoor, Bradley Nguyen
 * Teacher Name: Mrs. Bailey
 * Period: 1
 * Due Date: 05-12-25
 */
import java.awt.*;

public abstract class Icons implements Hittable {
    protected int x, y;
    protected String type;
    protected boolean wasHit = false;

    /**
     * Constructs an icon at the specified position and type.
     * @param x: x-coordinate of the icon.
     * @param y: y-coordinate of the icon.
     * @param type A string representing the type of icon
     */
    public Icons(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    /**
     * Returns the type of the icon
     * @return The string representing the icon type.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns whether the icon has been hit or used.
     * @return true if it was hit
     */
    public boolean wasHit() {
        return wasHit;
    }

    /**
     * Sets the hit status of the icon.
     * @param hit true if the icon was hit.
     */
    public void setWasHit(boolean hit) {
        this.wasHit = hit;
    }

    /**
     * Draws the icon on the screen.
     * @param g The Graphics object used to draw.
     * @param cellSize The size of the cell to fit the drawing in.
     */
    public void draw(Graphics g, int cellSize) {
        // Base class does nothing here — subclasses should override
        // g.fillRect(x, y, cellSize, cellSize); // temp test
    }

    /**
     * Handles what happens when the duck collides with this icon.
     * @param duck the PlayerDuck that collided with the icon.
     * @param scoreManager the ScoreManager for updating the score.
     * @param b Optional flag for conditional logic.
     */
    public void handleCollision(PlayerDuck duck, ScoreManager scoreManager, Boolean b) {
    }

    /**
     * Returns whether the icon is an obstacle.
     * @return true if it is an obstacle, false otherwise
     */
    public boolean isObstacle() {
        return false;
    }

    /**
     * Returns the bounding rectangle used collisions
     * @return rectangle representing the bounds of the icon.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, 25, 25); // fallback hitbox
    }

    /**
     * Returns the front bounding box for detecting collisions
     * @return A Rectangle representing the front collision area.
     */
    public Rectangle getFrontBounds() {
        return new Rectangle(x, y, DuckMazeGame.TILE_SIZE, DuckMazeGame.TILE_SIZE);
    }

    /**
     * Returns the back bounding box for detecting collisions
     * @return A Rectangle representing the back collision area.
     */
    public Rectangle getBackBounds() {
        return new Rectangle(x, y, DuckMazeGame.TILE_SIZE, DuckMazeGame.TILE_SIZE); // same as front by default
    }

    /**
     * Gets the x-coordinate of the icon.
     * @return The x position.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the icon.
     * @return The y position.
     */
    public int getY() {
        return y;
    }
}