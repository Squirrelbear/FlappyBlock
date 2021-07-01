import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Flappy Block
 * Author: Peter Mitchell (2021)
 *
 * Game class:
 * Defines the entry point for the Flappy Block game.
 * Manages the game loop and input events passing them to the gamePanel.
 */
public class Game extends JFrame implements KeyListener, ActionListener {
    /**
     * The time in ms between updates.
     */
    public static int TICK_RATE = 40;

    /**
     * Creates a new game and starts the game.
     *
     * @param args Not used.
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.startGame();
    }

    /**
     * Timer used for the update/render loop.
     */
    private Timer gameLoopTimer;
    /**
     * Reference to the gamePanel to pass input to.
     */
    private GamePanel gamePanel;
    /**
     * Width of the play space in pixels.
     */
    private final int gameWidth = 1280;
    /**
     * Height of the play space in pixels.
     */
    private final int gameHeight = 960;

    /**
     * Creates a single JFrame with the boardPanel, then
     * configures the game loop timer, sets up the KeyListener
     * for input, and then makes it visible.
     */
    public Game() {
        super("Flappy Block");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        gamePanel = new GamePanel(gameWidth, gameHeight);
        getContentPane().add(gamePanel);

        gameLoopTimer = new Timer(TICK_RATE,this);
        addKeyListener(this);

        pack();
        setVisible(true);
    }

    /**
     * Starts the game by starting the game loop timer.
     */
    public void startGame() {
        gameLoopTimer.start();
    }

    /**
     * Event triggered every time the gameLoopTimer triggers.
     * Forces the gamePanel to perform an update and will include a repaint().
     *
     * @param e Not used.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        gamePanel.update();
    }

    /**
     * Passes the information about released keys to the gamePanel.
     *
     * @param e The information about the key that was released.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        gamePanel.processInput(e.getKeyCode());
    }

    /**
     * Not used.
     *
     * @param e Not used.
     */
    @Override
    public void keyTyped(KeyEvent e) {}
    /**
     * Not used.
     *
     * @param e Not used.
     */
    @Override
    public void keyPressed(KeyEvent e) {}
}
