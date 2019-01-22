package game;

import javafx.scene.Group;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;

/**
 * @author Hunter Gregory
 */
public class Paddle extends GameObject {
    public static final int DEFAULT_WIDTH = 60;
    public static final int HEIGHT = 10;
    public static final int SPEED = 30;
    public static final String IMAGE_NAME = "paddle.gif";

    private boolean myCanShootLasers;
    private PowerAimer myAimer;
    private boolean myIsSidePaddle;

    /**
     * Create a game.Paddle with an empty, rounded rectangle
     */
    public Paddle(double x, double y, boolean isSidePaddle) {
        super(IMAGE_NAME, DEFAULT_WIDTH, HEIGHT);
        this.setX(x);
        this.setY(y);
        myCanShootLasers = false;
        myAimer = new PowerAimer();
        myIsSidePaddle = isSidePaddle;
        if (myIsSidePaddle)
            getImageView().setRotate(90);
    }

    /**
     * @return true if is paddle on left or right
     */
    public boolean getIsSidePaddle() { return myIsSidePaddle; }

    /**
     *
     */
    public void activateIfPowerShot(Group root, Ball ball) {
        boolean shouldActivate = this.getAimer().getPowerShotIsOn()
                && !this.getAimer().getIsCurrentlyAiming()
                && this.hitGameObject(ball);
        if (shouldActivate)
            this.getAimer().activate(root, ball);
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
