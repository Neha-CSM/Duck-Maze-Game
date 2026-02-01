/**
 * GamePanel handles user input, gameplay updates, and collision detection
 * between the duck and all icons.
 * @author: Kaushiki Kapoor
 * Collaborators: Neha Sharma, Bradley Nguyen
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer gameTimer;
    private Timer obstacleTimer;
    private Timer lightningTimer;
    private long startTime;
    private DuckMazeGame mazeGame;
    private PlayerDuck duck;
    private List icons;
    private Set occupiedCells;
    private ScoreManager scoreManager;
    public static List hittables = new ArrayList<>();
    private boolean lightningActive = false;
    private List removedTurtles = new ArrayList<>();
    private long lightningStrikeTime;
    private long lastLightningTime = -10000; // delaying first strike

    /**
     * Constructs the GamePanel, sets up timers, key listener, initial game
     * state, and starts timers.
     * @param mazeGame the main game controller class
     */
    public GamePanel(DuckMazeGame mazeGame) {
        this.mazeGame = mazeGame;
        this.duck = mazeGame.getPlayerDuck();
        this.icons = new ArrayList<>();
        this.occupiedCells = new HashSet<>();
        this.scoreManager = new ScoreManager();

        // Canvas size based on maze
        int w = mazeGame.getTotalCols() * DuckMazeGame.TILE_SIZE;
        int h = mazeGame.getTotalRows() * DuckMazeGame.TILE_SIZE;
        setPreferredSize(new Dimension(w, h));
        setBackground(new Color(34, 139, 34)); // dark-ish green
        setFocusable(true);
        addKeyListener(this);

        startTime = System.currentTimeMillis();

        // 60fps-ish game loop
        gameTimer = new Timer(16, this);
        gameTimer.start();

        // Periodically re-generates icons
        // Timer for obstacle regeneration
        obstacleTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                long secondsElapsed = (System.currentTimeMillis() - startTime) / 1000;
                long sinceLastLightning = System.currentTimeMillis() - lastLightningTime;
                // Light debounce on regen
                if (secondsElapsed % 20 == 0 && sinceLastLightning > 6000) {
                    regenerateIcons();
                }
            }
        });
        obstacleTimer.start();

        // Timer to restore turtles after lightning strike
        lightningTimer = new Timer(5000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                regenerateTurtles();
            }
        });
        lightningTimer.setRepeats(false);

        regenerateIcons(); // Drop icons when game starts
    }

    /**
     * Clears and regenerates all icons (obstacles and power-ups) in random
     * positions.
     */
    private void regenerateIcons() {
        icons.clear();
        occupiedCells.clear();
        hittables.clear();
        Lightning lightningRef = new Lightning(-100, -100); // placeholder
        for (int i = 0; i < 40; i++) {
            int row, col;
            // Ensure no overlap
            do {
                row = 1 + (int)(Math.random() * 18);
                col = 1 + (int)(Math.random() * 18);
            } while (!occupiedCells.add(new Point(row, col)));

            int x = col * DuckMazeGame.TILE_SIZE;
            int y = row * DuckMazeGame.TILE_SIZE;

            switch (i % 6) {
                case 0:
                    Log log = new Log(x, y);
                    icons.add(log);
                    hittables.add(log);
                    break;
                case 1:
                    Water water = new Water(x, y);
                    icons.add(water);
                    hittables.add(water);
                    break;
                case 2:
                    Turtle turtle = new Turtle(x, y, lightningRef);
                    icons.add(turtle);
                    hittables.add(turtle);
                    break;
                case 3:
                    Feather feather = new Feather(x, y);
                    icons.add(feather);
                    hittables.add(feather);
                    break;
                case 4:
                    Shield shield = new Shield(x, y);
                    icons.add(shield);
                    hittables.add(shield);
                    break;
                case 5:
                    lightningRef = new Lightning(x, y);
                    icons.add(lightningRef);
                    hittables.add(lightningRef);
                    break;
            }
        }
        repaint();
    }

    /**
     * Regenerates all turtles that were removed during a lightning strike.
     */
    private void regenerateTurtles() {
        for (Turtle t : removedTurtles) {
            icons.add(t);
            hittables.add(t);
        }
        removedTurtles.clear();
        repaint();
    }

    /**
     * Called by the game timer on each tick to update duck movement,
     * handle collisions, process icon effects, and repaint the screen.
     * @param e the action event triggered by the timer
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        duck.move();
        List toRemove = new ArrayList<>();
        for (Icons icon : icons) {
            if (duck.getBounds().intersects(icon.getBounds())) {
                icon.handleCollision(duck, scoreManager, duck.isJumping());
                if (icon.shouldRemove()) {
                    toRemove.add(icon);
                    if (icon instanceof Lightning) {
                        lightningActive = true;
                        lightningStrikeTime = System.currentTimeMillis();
                        lastLightningTime = lightningStrikeTime;
                    }
                }
            }
        }

        if (lightningActive) {
            List turtlesToZap = new ArrayList<>();
            for (Icons i : icons) {
                if (i instanceof Turtle) {
                    turtlesToZap.add(i);
                    removedTurtles.add((Turtle) i);
                }
            }
            icons.removeAll(turtlesToZap);
            hittables.removeAll(turtlesToZap);
            lightningTimer.start(); // triggers turtle regen
            lightningActive = false;
        }

        icons.removeAll(toRemove);
        hittables.removeAll(toRemove);
        repaint();
        checkWin();
    }

    /**
     * Checks if the duck has reached the finish point and transitions to the
     * result screen.
     */
    private void checkWin() {
        if (duck.getX() >= 19 * DuckMazeGame.TILE_SIZE && duck.getY() >= 19 * DuckMazeGame.TILE_SIZE) {
            gameTimer.stop();
            obstacleTimer.stop();
            mazeGame.finalResult(scoreManager.getScore() >= 0); // Score-based win/lose
        }
    }

    /**
     * Draws the maze, duck, all icons, score, and elapsed time.
     * @param g the Graphics context to draw on
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        mazeGame.drawMaze(g);
        duck.draw(g);
        for (Icons icon : icons) {
            icon.draw(g);
        }
        g.setColor(Color.BLACK);
        g.drawString("Score: " + scoreManager.getScore(), 10, 20);
        long seconds = (System.currentTimeMillis() - startTime) / 1000;
        g.drawString("Time: " + seconds + "s", 10, 40);
    }

    /**
     * Handles key presses to control duck movement or jumping.
     * @param e the key event
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        // Simple movement mapping
        if (code == KeyEvent.VK_LEFT) {
            duck.setDX(-2);
        } else if (code == KeyEvent.VK_RIGHT) {
            duck.setDX(2);
        } else if (code == KeyEvent.VK_UP) {
            duck.setDY(-3);
        } else if (code == KeyEvent.VK_DOWN) {
            duck.setDY(3);
        }

        // Jump logic
        if (code == KeyEvent.VK_SPACE) {
            int x = duck.getX();
            int y = duck.getY();
            int jump = DuckMazeGame.TILE_SIZE * 2;
            if (duck.isObstacleAhead(x + 10, y)) {
                duck.jump(x + jump, y);
            } else if (duck.isObstacleBehind(x, y)) {
                duck.jump(x - jump, y);
            }
        }
    }

    /**
     * Stops duck movement when keys are released.
     * @param e the key event
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT) {
            duck.setDX(0);
        }
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN) {
            duck.setDY(0);
        }
    }

    /**
     * Unused but required for KeyListener interface.
     * @param e the key event
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // No-op
    }

    /**
     * Returns the list of hittable icons currently on screen.
     * @return list of hittables
     */
    public List getHittables() {
        return hittables;
    }

}
