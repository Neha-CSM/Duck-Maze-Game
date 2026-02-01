/**
 * The ScoreManager class tracks and manages the player's score in the game.
 * @author Bradley Nguyen
 * Collaborators Kaushiki Kapoor, Neha Sharma
 * Teacher Mrs. Bailey
 * Period 1
 * DueDate 05-12-25
 */
public class ScoreManager {
    private int score = 0;
    private long startTime;

    //Constructor
    public ScoreManager() {
        score = 0;
        startTime = System.currentTimeMillis();
    }

    /**
     * This method adds the given number of points to the current score.
     * @param points the number of points to add to the score
     */
    public void addPoints(int points) {
        score += points;
    }

    /**
     * Deducts points from the player's score, considering the shield logic.
     * @param points the number of points to deduct
     * @param duck the player duck whose shield status is checked before deduction
     */
    public void losePoints(int points, PlayerDuck duck) {
        if (duck != null && duck.isShieldActive()) {
            return; // Shield active - skipping deduction
        }
        score -= points;
    }

    /**
     * Retrieves the current score of the player.
     * @return the current score of the player
     */
    public int getScore() {
        return score;
    }

    // Resets the player's score to 0.
    public void resetScore() {
        score = 0;
    }

    /**
     * Retrieves the elapsed time in seconds
     * @return the elapsed time in seconds
     */
    public int getTime() {
        long currentTime = System.currentTimeMillis();
        return (int)((currentTime - startTime) / 1000);
    }

    // Resets both the player's score and the start time.
    public void reset() {
        resetScore(); // reused to reset score
        startTime = System.currentTimeMillis(); // restart clock
    }
}