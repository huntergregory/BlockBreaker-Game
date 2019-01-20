package game;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 * A rotatable Rectangle that aims in the direction that a ball will go.
 * Activated by the POWER_SHOT powerup.
 * @author Hunter Gregory
 */
public class PowerAimer {
    public static final Paint FILL_COLOR = Color.GREY;
    public static final int WIDTH = 1;
    public static final int HEIGHT = 30;
    public static final int ROTATE_DELTA = 5;
    public static final int MAX_ANGLE = 45;
    public static final int MIN_ANGLE = -45;

    private Rectangle myRect;
    private Rotate myRotate;
    private Group myRoot;
    private Ball myBall;
    private double myAngle; // 0 will correspond to 90 degrees
    private boolean myPowerShotIsOn;
    private boolean myIsCurrentlyAiming;

    /**
     * Create a PowerAimer
     */
    public PowerAimer() {
        myRoot = null;
        myBall = null;
        myAngle = 0;
        myIsCurrentlyAiming = false;
        myPowerShotIsOn = false;
        myRotate = null;
        myRect = new Rectangle(0, 0, WIDTH, HEIGHT); //dummy x and y positions
        myRect.setFill(FILL_COLOR);
        myRect.setArcHeight(5); //make a rounded rectangle
        myRect.setArcWidth(5);
    }

    /**
     * Use when the POWER_SHOT powerup comes into contact with a Paddle
     */
    public void turnPowerShotOn() { myPowerShotIsOn = true; }


    /**
     * Turns on the aimer and freezes the ball in place on the paddle
     * @param root of level
     * @param ball
     */
    public void activate(Group root, Ball ball) {
        myIsCurrentlyAiming = true;
        myRoot = root;
        myBall = ball;
        myRect.setX(myBall.getX() + myBall.getWidth() / 2);
        myRect.setY(myBall.getY() + myBall.getHeight() - HEIGHT);
        myRotate = new Rotate(myAngle, myBall.getX() + myBall.getWidth() / 2, myBall.getY() + myBall.getHeight());
        myRect.getTransforms().add(myRotate);
        myRoot.getChildren().add(myRect);
        myBall.halt();
    }

    /**
     * Rotate the ball aimer towards the right
     */
    public void rotateClockwise() {
        if (myAngle < MAX_ANGLE)
            myAngle += ROTATE_DELTA;
        myRotate.setAngle(myAngle);
        //FIX, prevent from rotating below 15 deg. above horizontal
    }

    /**
     * Rotate the ball aimer towards the left
     */
    public void rotateCounterClockwise() {
        if (myAngle > MIN_ANGLE)
            myAngle -= ROTATE_DELTA;
        myRotate.setAngle(myAngle);
    }

    /**
     * Releases the Ball at max velocity in the direction aimed and turns off the POWER_SHOT powerup
     */
    public void fire() {
        myRoot.getChildren().remove(myRect);
        myBall.unhaltWithMaxVelocity(myAngle);
        myAngle = 0;
        myPowerShotIsOn = false;
        myIsCurrentlyAiming = false;
    }

    /**
     * @return true if the POWER_SHOT powerup is currently on for this Level
     */
    public boolean getPowerShotIsOn() { return myPowerShotIsOn; }


    /**
     * @return true if a POWER_SHOT ball is frozen and being aimed
     */
    public boolean getIsCurrentlyAiming() { return myIsCurrentlyAiming; }

}
