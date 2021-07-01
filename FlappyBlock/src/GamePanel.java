import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Flappy Block
 * Author: Peter Mitchell (2021)
 *
 * GamePanel class:
 * Manages the game state and rendering for FlappyBlock.
 */
public class GamePanel extends JPanel {
    /**
     * States to use for the GameState machine.
     * WaitForBegin: Used to represent waiting before the game starts. (Press space to move to next state).
     * Flapping: Game is running with block flapping until a game over is reached from a collision.
     * GameOver: Shows a game over message and pressing R will return to the WaitForBegin.
     */
    public enum GameState { WaitForBegin, Flapping, GameOver }

    /**
     * Width and height of the game space.
     */
    private int gameWidth, gameHeight;
    /**
     * List of all the obstacles that are currently active.
     */
    private List<Obstacle> obstacleList;
    /**
     * The player's character that can flap to try and pass between obstacles.
     */
    private FlappyBlock flappyBlock;
    /**
     * Maintains the game state. See GameState enum for more.
     */
    private GameState gameState;

    /**
     * Current score. The number of obstacles that have been passed.
     */
    private int score;
    /**
     * Font used for rendering the score.
     */
    private Font scoreFont = new Font("Arial", Font.BOLD, 50);
    /**
     * Font used for rendering the gameStateMessage and gameOverMessage.
     */
    private Font endGameFont = new Font("Arial", Font.PLAIN, 30);
    /**
     * Text shown during the WaitForBegin state.
     */
    private String gameStartMessage = "Press SPACE to begin! Press space to \"flap\".";
    /**
     * Text shown during the GameOver state.
     */
    private String gameOverMessage = "Block can flap no more. Press R to restart.";

    /**
     * Creates the flappyBlock and a collection of obstacles to prepare the game.
     * Defaults to the WaitForBegin state.
     *
     * @param gameWidth Width of the game space.
     * @param gameHeight Height of the game space.
     */
    public GamePanel(int gameWidth, int gameHeight) {
        this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;
        setPreferredSize(new Dimension(gameWidth,gameHeight));
        setBackground(new Color(151, 151, 250));

        obstacleList = new ArrayList<>();
        createObstacles();
        flappyBlock = new FlappyBlock(100,gameHeight);
        gameState = GameState.WaitForBegin;
    }

    /**
     * Only performs updates if in the Flapping state to make obstacles move,
     * also the flappyBlock move, and then check for collisions for either
     * a GameOver or for increasing score.
     * Will always force a repaint().
     */
    public void update() {
        if(gameState == GameState.Flapping) {
            // Move the obstacles
            for (Obstacle obstacle : obstacleList) {
                obstacle.update(-3);
            }

            flappyBlock.update();

            if(flappyBlock.isOutOfBounds()) {
                gameState = GameState.GameOver;
            }

            // Check for collisions with any parts in the obstacles
            for (Obstacle obstacle : obstacleList) {
                if(obstacle.isCollidingForLoss(flappyBlock)) {
                    gameState = GameState.GameOver;
                    break;
                } else if(obstacle.isCollidingForScore(flappyBlock)) {
                    score++;
                }
            }
        }
        repaint();
    }

    /**
     * Checks for Escape to quit, Space to start the game during WaitForBegin,
     * Space to flap while the game is playing, or R for reset at any time.
     *
     * @param keyCode A KeyEvent code for the key that has been pressed
     */
    public void processInput(int keyCode) {
        if(keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }

        if(keyCode == KeyEvent.VK_SPACE) {
            if (gameState == GameState.WaitForBegin) gameState = GameState.Flapping;
            else if(gameState == GameState.Flapping) flappyBlock.flap();
        } else if(keyCode == KeyEvent.VK_R) {
            reset();
        }

    }

    /**
     * Draws all the obstacles to the panel, the flappyBlock,
     * the score, and any relevant text based on game state.
     *
     * @param g Reference to the Graphics object for drawing.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for(Obstacle obstacle : obstacleList) {
            obstacle.paint(g);
        }
        flappyBlock.paint(g);
        showScore(g);
        if(gameState == GameState.GameOver)
            showEndText(g);
        else if(gameState == GameState.WaitForBegin)
            showStartText(g);
    }

    /**
     * Resets the game back to a default state by resetting FlappyBlock,
     * creates a fresh set of obstacles, resets the score back to 0,
     * and returns to the WaitForBegin state.
     */
    public void reset() {
        flappyBlock.reset();
        createObstacles();
        score = 0;
        gameState = GameState.WaitForBegin;
    }

    /**
     * Draws the score centered to the top of the screen.
     *
     * @param g Reference to the Graphics object for drawing.
     */
    private void showScore(Graphics g) {
        g.setFont(scoreFont);
        String scoreText = String.valueOf(score);
        int textWidth = g.getFontMetrics().stringWidth(scoreText);
        g.setColor(new Color(255, 255, 255, 85));
        g.fillRect(gameWidth/2-textWidth/2 - 20, 50, textWidth+40,65);

        g.setColor(new Color(128,0,0));
        g.drawString(scoreText, gameWidth/2-textWidth/2, 100);
    }

    /**
     * Draws the game over text to the middle of the screen.
     *
     * @param g Reference to the Graphics object for drawing.
     */
    private void showEndText(Graphics g) {
        g.setFont(endGameFont);
        g.setColor(Color.BLACK);
        int textWidth = g.getFontMetrics().stringWidth(gameOverMessage);
        g.drawString(gameOverMessage, gameWidth/2-textWidth/2, gameHeight/2);
    }

    /**
     * Draws the waiting for begin text to the middle of the screen.
     *
     * @param g Reference to the Graphics object for drawing.
     */
    private void showStartText(Graphics g) {
        g.setFont(endGameFont);
        g.setColor(Color.BLACK);
        int textWidth = g.getFontMetrics().stringWidth(gameStartMessage);
        g.drawString(gameStartMessage, gameWidth/2-textWidth/2, gameHeight/2);
    }

    /**
     * Creates enough obstacles to fill the screen and prepares them for the game beginning.
     */
    private void createObstacles() {
        obstacleList.clear();
        int x = 400;
        int gapBetweenX = 300;
        int gapBetweenY = 200;
        int createCount = gameWidth / gapBetweenX;
        for(int i = 0; i < createCount; i++) {
            obstacleList.add(new Obstacle(x, gapBetweenY, gameWidth, gameHeight));
            x += gapBetweenX;
        }
    }
}
