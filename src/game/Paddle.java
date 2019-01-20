package game;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;

/**
 * @author Hunter Gregory
 */
public class Paddle extends GameObject {
    public static final int DEFAULT_WIDTH = 60;
    public static final int HEIGHT = 10;
    public static final int SPEED = 20;
    public static final String IMAGE_NAME = "paddle.gif";

    private boolean myCanShootLasers;
    private PowerAimer myAimer;

    /**
     * Create a game.Paddle with an empty, rounded rectangle
     */
    public Paddle(double x, double y) {
        super(IMAGE_NAME, DEFAULT_WIDTH, HEIGHT);
        this.setX(x);
        this.setY(y);
        myCanShootLasers = false;
        myAimer = new PowerAimer();
    }

    /**
     * Handles multiple keyboard inputs for the Paddle and its PowerAimer, and adds Lasers to the
     * GameScene and specified list of Lasers if necessary. Action taken only if gameScene is a Level
     * @param code
     * @param gameScene
     * @param lasers
     */
    public void handleOfficialCodes(KeyCode code, GameScene gameScene, ArrayList<Laser> lasers) {
        if (!(gameScene instanceof Level))
            return;
        handleAimer(code);
        handleMovement(code, gameScene);
        handleAddingLaser(code, gameScene, lasers);
    }

    private void handleAimer(KeyCode code) {
        if (!this.getAimer().getIsCurrentlyAiming())
            return;
        if (code == KeyCode.RIGHT) {
            this.getAimer().rotateClockwise();
        }
        if (code == KeyCode.LEFT) {
            this.getAimer().rotateCounterClockwise();
        }
        if (code == KeyCode.SPACE) {
            this.getAimer().fire();
        }
    }

    private void handleMovement(KeyCode code, GameScene gameScene) {
        boolean isAiming = this.getAimer().getIsCurrentlyAiming();
        if (code == KeyCode.RIGHT && !isAiming && this.getX() + this.getWidth() < gameScene.getAssignedWidth())
            this.setX(this.getX() + Paddle.SPEED); //currently overshoots boundary a little due to large SPEED
        if (code == KeyCode.LEFT && !isAiming && this.getX() > 0)
            this.setX(this.getX() - Paddle.SPEED); //same as above
    }

    private void handleAddingLaser(KeyCode code, GameScene gameScene, ArrayList<Laser> lasers) {
        if (code == KeyCode.SPACE) {
            if (this.getCanShootLasers()) {
                Laser laser = new Laser(this.getX() + this.getWidth() / 2, this.getY());
                lasers.add(laser);
                gameScene.addGameObjectToRoot(laser);
            }
        }
    }

    /**
     * @return PowerAimer for Paddle
     */
    public PowerAimer getAimer() { return myAimer; }

    /**
     * Prepares paddle for performing a POWER_SHOT and disables lasers
     */
    public void initPowerShot() {
        myAimer.turnPowerShotOn();
        this.setCanShootLasers(false);
    }

    /**
     * Set to true to allow the paddle to shoot lasers
     * @param bool
     */
    public void setCanShootLasers(boolean bool) { myCanShootLasers = bool; } //FIX, add timer

    /**
     * @return true if Paddle can shoot lasers
     */
    public boolean getCanShootLasers() { return myCanShootLasers; }

    /**
     * Increases width of Paddle
     */
    public void makeBig() {
        this.setWidth(this.getWidth() + DEFAULT_WIDTH / 2); // FIX make a timer
    }

    private void setWidth(double length) {
        myImageView.setFitWidth(length); //FIX animate it
    }
}
