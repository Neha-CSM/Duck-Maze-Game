/**
 * The DuckMazeGame class generates a maze and allows the duck (player)to
 * interact with power-ups and obstacles.
 * @author Kaushiki Kapoor
 * Collaborators: Neha Sharma, Bradley Nguyen
 */
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.*;

public class DuckMazeGame {
    public static final int TILE_SIZE = 25;
    private int totalRows;
    private int totalCols;
    private Cell[][] mazeGrid;
    private PlayerDuck playerDuck;
    private Random rng = new Random();
    private static final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    private GamePanel panel;

    /**
     * Constructs the DuckMazeGame by initializing the maze grid and setting up
     * the player duck.
     * @param rows: number of rows in the maze
     * @param cols: number of columns in the maze
     */
    public DuckMazeGame(int rows, int cols) {
        this.totalRows = rows;
        this.totalCols = cols;
        mazeGrid = new Cell[totalRows][totalCols];
        for (int r = 0; r < totalRows; r++) {
            for (int c = 0; c < totalCols; c++) {
                mazeGrid[r][c] = new Cell();
            }
        }
        buildMaze(0, 0);
        playerDuck = new PlayerDuck(0, 0, this);
    }

    /**
     * Retrieves the maze grid
     * @return the maze grid as a 2D array of Cell objects
     */
    public Cell[][] getMazeGrid() {
        return mazeGrid;
    }

    /**
     * Checks whether the player can move to a new position based on their
     * current and surrounding cells.
     * @param currX the current X coordinate of the player
     * @param currY the current Y coordinate of the player
     * @param nextX the X coordinate to move to
     * @param nextY the Y coordinate to move to
     * @param isJumping boolean indicating whether the player is jumping (which
     * bypasses walls)
     * @return true if the player can move to the new position, false otherwise
     */
    public boolean checkMove(int currX, int currY, int nextX, int nextY, boolean isJumping) {
        int col = (currX + 12) / TILE_SIZE;
        int row = (currY + 12) / TILE_SIZE;
        int nextCol = (nextX + 12) / TILE_SIZE;
        int nextRow = (nextY + 12) / TILE_SIZE;
        if (!isValid(row, col) || !isValid(nextRow, nextCol)) {
            return false;
        }
        // Allow all movement if jumping
        if (isJumping) {
            return true;
        }
        Cell current = mazeGrid[row][col];
        if (row != nextRow) {
            if (nextRow < row) {
                return !current.hasTop;
            } else {
                return !current.hasBottom;
            }
        } else if (col != nextCol) {
            if (nextCol < col) {
                return !current.hasLeft;
            } else {
                return !current.hasRight;
            }
        }
        return true;
    }

    /**
     * Checks if the inputted row and column are within the bounds of the maze
     * grid.
     * @param r the row index to check
     * @param c the column index to check
     * @return true if the indices are within bounds, false otherwise
     */
    private boolean isValid(int r, int c) {
        return r >= 0 && r < totalRows && c >= 0 && c < totalCols;
    }

    /**
     * Draws the maze grid on the screen using Graphics object by iterating over
     * each cell
     * @param g the Graphics object used to draw the maze on the screen
     */
    public void drawMaze(Graphics g) {
        g.setColor(Color.BLACK);
        int shrink = 15;
        for (int r = 0; r < totalRows; r++) {
            for (int c = 0; c < totalCols; c++) {
                int x = c * TILE_SIZE;
                int y = r * TILE_SIZE;
                Cell cell = mazeGrid[r][c];
                // Top wall
                if (cell.hasTop) {
                    int x1, x2;
                    if (c == 0) {
                        x1 = x + shrink;
                    } else {
                        x1 = x;
                    }
                    if (c == totalCols - 1) {
                        x2 = x + TILE_SIZE - shrink;
                    } else {
                        x2 = x + TILE_SIZE;
                    }
                    g.drawLine(x1, y, x2, y);
                }
                // Left wall
                if (cell.hasLeft) {
                    int y1, y2;
                    if (r == 0) {
                        y1 = y + shrink;
                    } else {
                        y1 = y;
                    }
                    if (r == totalRows - 1) {
                        y2 = y + TILE_SIZE - shrink;
                    } else {
                        y2 = y + TILE_SIZE;
                    }
                    g.drawLine(x, y1, x, y2);
                }
                // Right wall
                if (cell.hasRight) {
                    int y1, y2;
                    if (r == 0) {
                        y1 = y + shrink;
                    } else {
                        y1 = y;
                    }
                    if (r == totalRows - 1) {
                        y2 = y + TILE_SIZE - shrink;
                    } else {
                        y2 = y + TILE_SIZE;
                    }
                    g.drawLine(x + TILE_SIZE, y1, x + TILE_SIZE, y2);
                }
                // Bottom wall
                if (cell.hasBottom) {
                    int x1, x2;
                    if (c == 0) {
                        x1 = x + shrink;
                    } else {
                        x1 = x;
                    }
                    if (c == totalCols - 1) {
                        x2 = x + TILE_SIZE - shrink;
                    } else {
                        x2 = x + TILE_SIZE;
                    }
                    g.drawLine(x1, y + TILE_SIZE, x2, y + TILE_SIZE);
                }
            }
        }
        // Draw start and end tiles
        g.setColor(Color.GREEN);
        g.fillRect(2, 2, TILE_SIZE - 3, TILE_SIZE - 3); // Start tile
        g.setColor(Color.RED);
        int exitX = (totalCols - 1) * TILE_SIZE;
        int exitY = (totalRows - 1) * TILE_SIZE;
        g.fillRect(exitX + 2, exitY + 2, TILE_SIZE - 3, TILE_SIZE - 3);
    }

    /**
     * Generates the maze iteratively using a depth-first traversal
     * @param startRow the starting row index for maze generation
     * @param startCol the starting column index for maze generation
     */
    private void buildMaze(int startRow, int startCol) {
        Stack<Point> stack = new Stack<>();
        mazeGrid[startRow][startCol].visited = true;
        stack.push(new Point(startRow, startCol));
        while (!stack.isEmpty()) {
            Point current = stack.peek();
            List<int[]> shuffledDirs = new ArrayList<>(Arrays.asList(DIRECTIONS));
            Collections.shuffle(shuffledDirs, rng);
            boolean carved = false;
            for (int[] dir : shuffledDirs) {
                int newRow = current.x + dir[0];
                int newCol = current.y + dir[1];
                if (isValid(newRow, newCol) && !mazeGrid[newRow][newCol].visited) {
                    breakWall(current.x, current.y, newRow, newCol);
                    mazeGrid[newRow][newCol].visited = true;
                    stack.push(new Point(newRow, newCol));
                    carved = true;
                    break;
                }
            }
            if (!carved) stack.pop();
        }
    }

    /**
     * This method removes the wall between the two cells (horizontal or
     * vertical), based on their relative positions.
     * @param r1 the row index of the first cell
     * @param c1 the column index of the first cell
     * @param r2 the row index of the second cell
     * @param c2 the column index of the second cell
     */
    private void breakWall(int r1, int c1, int r2, int c2) {
        if (r1 == r2) {
            if (c1 < c2) {
                mazeGrid[r1][c1].hasRight = false;
                mazeGrid[r2][c2].hasLeft = false;
            } else {
                mazeGrid[r1][c1].hasLeft = false;
                mazeGrid[r2][c2].hasRight = false;
            }
        } else if (c1 == c2) {
            if (r1 < r2) {
                mazeGrid[r1][c1].hasBottom = false;
                mazeGrid[r2][c2].hasTop = false;
            } else {
                mazeGrid[r1][c1].hasTop = false;
                mazeGrid[r2][c2].hasBottom = false;
            }
        }
    }

    /**
     * Returns the PlayerDuck object associated with the game.
     * @return the PlayerDuck object
     */
    public PlayerDuck getPlayerDuck() {
        return playerDuck;
    }

    /**
     * Represents the cell for each tile in the maze.
     */
    private static class Cell {
        boolean hasTop = true;
        boolean hasRight = true;
        boolean hasBottom = true;
        boolean hasLeft = true;
        boolean visited = false;
    }

    /**
     * Retrieves the list of all hittable objects in the game
     * @return a list of Hittable objects (obstacles and power-ups)
     */
    public List<Hittable> getHittables() {
        return panel.getHittables();
    }

    /**
     * Retrieves the total number of rows in the maze grid
     * @return the number of rows in the maze grid
     */
    public int getTotalRows() {
        return totalRows;
    }

    /**
     * Retrieves the total number of columns in the maze grid
     * and maze generation logic.
     * @return the number of columns in the maze grid
     */
    public int getTotalCols() {
        return totalCols;
    }

    /**
     * Launches the Duck Maze Game
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Shows instructions before launching the game
        JFrame frame = new JFrame("Duck Maze Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        DuckMazeGame game = new DuckMazeGame(20, 20);
        game.showInstructions();
        game.panel = new GamePanel(game); // Properly sets preferred size
        frame.add(game.panel);
        frame.pack(); // Uses preferred size to size the frame correctly
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Displays the instructions for playing Duck Maze Racer.
     */
    public void showInstructions() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("==================================");
        System.out.println(" Welcome to Duck Maze Racer");
        System.out.println("==================================");
        System.out.println("Instructions:");
        System.out.println("- Use the arrow keys to move your duck:");
        System.out.println(" - Right Arrow: Move right");
        System.out.println(" - Left Arrow: Move left");
        System.out.println(" - Up Arrow: Move forward");
        System.out.println(" - Down Arrow: Move backward");
        System.out.println("- Press the Space Bar to jump over obstacles.");
        System.out.println("- Avoid hazards like turtles, logs, and water traps.");
        System.out.println(" - Colliding with logs,water traps, and turtles deduct points unless jumped over");
        System.out.println(" - Interactions (colliding or jumping) with turtles have the greatest impact on score");
        System.out.println("- Collect power-ups for temporary boosts.");
        System.out.println(" - Feathers for strength (have the greatest impact on score)");
        System.out.println(" - Lightning for eliminating turtles for 5 seconds");
        System.out.println(" - Shield for invincibility for 5 seconds");
        System.out.println("- Scoring guidelines");
        System.out.println(" - Colliding with Obstacles: Log (-15), Water (-15), Turtle (-25)");
        System.out.println(" - Colliding with Powerups: Shield (+5), Lightning (+5), Feather (+10)");
        System.out.println(" - Jumping Over Obstacles: Log (+10), Water (+10), Turtle (+20)");
        System.out.println("- Navigate the maze and reach the finish line with a score greater than 0 to win.");
        System.out.println();
        System.out.print("Type OK to continue: ");
        String input = scanner.nextLine();
        while (!input.equalsIgnoreCase("OK")) {
            System.out.print("Please type OK to begin: ");
            input = scanner.nextLine();
        }
    }

    /**
     * Displays the result screen after the game ends
     * @param passed a boolean indicating whether the player passed
     */
    public void finalResult(boolean passed) {
        JFrame frame = new JFrame("Result");
        JPanel panel = new JPanel();
        JLabel label;
        if (passed) {
            label = new JLabel("You Passed!");
        } else {
            label = new JLabel("You Failed.");
        }
        label.setFont(new Font("Arial", Font.BOLD, 32));
        panel.add(label);
        frame.add(panel);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
