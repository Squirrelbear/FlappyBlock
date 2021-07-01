import java.awt.*;
import java.util.Random;

/**
 * Flappy Block
 * Author: Peter Mitchell (2021)
 *
 * Obstacle class:
 * Represents a top and bottom obstacle moving
 * to the left for dodging by the player.
 */
public class Obstacle {
    /**
     * Collidable objects to avoid.
     */
    private CollidableRect top, bottom;
    /**
     * Collidable object to pass through for score.
     */
    private CollidableRect scoreGainRect;
    /**
     * If true, it means that the score block has been collided with and should not be used for more.
     */
    private boolean scoreApplied;
    /**
     * The width and height used for resetting to new random heights after passing the end of the screen.
     */
    private int gameWidth, gameHeight;
    /**
     * The distance in pixels between the top and bottom. Also the height of the score collision.
     */
    private int gapBetweenObstacles;

    /**
     * Width of the obstacles in pixels.
     */
    private final int RECT_WIDTH = 50;

    /**
     * Creates a simple Obstacle with one block at the top and one at the bottom.
     * Also creates a hidden collision point just past the obstacle to count for scores.
     */
    public Obstacle(int startX, int gapBetweenObstacles, int gameWidth, int gameHeight) {
        this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;
        this.gapBetweenObstacles = gapBetweenObstacles;
        top = new CollidableRect(new Position(startX,-gameHeight),RECT_WIDTH, gameHeight, new Color(85, 66, 34));
        bottom = new CollidableRect(new Position(startX,gapBetweenObstacles),RECT_WIDTH, gameHeight, new Color(85, 66, 34));
        scoreGainRect = new CollidableRect(new Position(startX+RECT_WIDTH,0), RECT_WIDTH, gapBetweenObstacles, Color.BLACK);
        randomiseOffset();
    }

    /**
     * Moves based on moveX (should call with negative number to move left).
     * Then checks if the obstacle is off screen and resets if this is the case.
     *
     * @param moveX Amount of move in pixels on the horizontal axis.
     */
    public void update(int moveX) {
        top.position.x += moveX;
        bottom.position.x += moveX;
        scoreGainRect.position.x += moveX;
        if(top.position.x + top.width <= 0)
            reset();
    }

    /**
     * Resets the obstacle to just off the right side of the screen with a random y position.
     */
    public void reset() {
        top.position.x = gameWidth;
        bottom.position.x = gameWidth;
        scoreGainRect.position.x = gameWidth+RECT_WIDTH;
        randomiseOffset();
        scoreApplied = false;
    }

    /**
     * Checks if the otherObject is colliding with either top or bottom.
     *
     * @param otherObject Any other CollidableRect object, presumably Flappy Bird
     * @return True if there is a collision with top or bottom obstacles.
     */
    public boolean isCollidingForLoss(CollidableRect otherObject) {
        return top.isCollidingWith(otherObject) || bottom.isCollidingWith(otherObject);
    }

    /**
     * Checks if the score has not been gained for this obstacle yet, and then also if
     * the hidden rect for gaining score just past the top/bottom has been collided with.
     *
     * @param otherObject Any other CollidableRect object, presumably Flappy Bird
     * @return True if score not triggered from this obstacle and the hidden score rect has been collided with.
     */
    public boolean isCollidingForScore(CollidableRect otherObject) {
        if(!scoreApplied && scoreGainRect.isCollidingWith(otherObject)) {
            scoreApplied = true;
            return true;
        }
        return false;
    }

    /**
     * Draws the top and bottom obstacles.
     *
     * @param g Reference to the Graphics object for drawing.
     */
    public void paint(Graphics g) {
        top.paint(g);
        bottom.paint(g);

        // Can uncomment the following to see the hidden score rects.
        //scoreGainRect.paint(g);
    }

    /**
     * Generates a random offset for the vertical obstacle alignment and then synchronises
     * the top, bottom, and scoreGainRect.
     */
    private void randomiseOffset() {
        Random rand = new Random();
        int offset = rand.nextInt(gameHeight-gapBetweenObstacles);
        top.position.y = -gameHeight+offset;
        bottom.position.y = gapBetweenObstacles+offset;
        scoreGainRect.position.y = offset;
    }
}
