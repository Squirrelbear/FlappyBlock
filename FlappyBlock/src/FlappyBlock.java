import java.awt.*;

/**
 * Flappy Block
 * Author: Peter Mitchell (2021)
 *
 * FlappyBlock class:
 * The player object that stays in the same place
 * horizontally, but can move up and down based on
 * a constant downward acceleration and force applied
 * by flapping.
 */
public class FlappyBlock extends CollidableRect {
    /**
     * Size of the block in pixels for width and height.
     */
    private static final int SIZE = 40;
    /**
     * The game height used for resetting starting position.
     */
    private int gameHeight;

    /**
     * Gravity (Downward acceleration).
     */
    private static final int gravity = 100;   // constant downward acceleration
    /**
     * Force applied when a flap occurs.
     */
    private static final int flapping = -150;   // upward acceleration whenever isFlapping is true

    /**
     * True if during the last window leading up to the update a flap was triggered by spacebar.
     */
    private boolean isFlapping = false;   // Is the bird flapping
    /**
     * Vertical velocity.
     */
    private double dY = 0; // current vertical speed

    /**
     * Constant based on the game's tick rate to simulate deltaTime for updates.
     */
    private double updatePerTime;

    /**
     * Creates a FlappyBlock centered by the gameHeight offset from the 0 by offset on the x axis.
     *
     * @param xOffset The distance from the left side of the screen to move to.
     * @param gameHeight The height of the game panel.
     */
    public FlappyBlock(int xOffset, int gameHeight) {
        super(new Position(xOffset, gameHeight / 2 - SIZE / 2), SIZE, SIZE, new Color(232, 110, 43));
        this.gameHeight = gameHeight;
        updatePerTime = Game.TICK_RATE / 1000.0;
    }

    /**
     * Updates the position of the FlappyBlock.
     * The velocity is modified by gravity and if space was hit before this update it will apply the
     * flap force as well.
     */
    public void update() {
        dY += gravity * updatePerTime;
        if(isFlapping) {
            dY += flapping;
            isFlapping = false;
        }
        position.y += (dY * updatePerTime);
    }

    /**
     * Reset to no velocity, removes a flap if one was queued, and
     * resets the vertical position to centered in the game height.
     */
    public void reset() {
        dY = 0;
        isFlapping = false;
        position.y = gameHeight / 2 - SIZE / 2;
    }

    /**
     * Queues a force application to simulate a flap during the next update().
     */
    public void flap() {
        isFlapping = true;
    }

    /**
     * Checks that the block is still in an allowed place. Must remain on screen at least partly.
     *
     * @return True if FlappyBlock is too high up or too low down.
     */
    public boolean isOutOfBounds() {
        return position.y+SIZE < 0 || position.y > gameHeight;
    }
}
