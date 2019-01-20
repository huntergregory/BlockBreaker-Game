package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * An ImageView with velocity components, representing a ball.
 * Position must be set using ImageView methods after the object is created.
 * @author Hunter Gregory
 */
public class Ball extends GameObject {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    public static final int MAX_VEL = 160;
    public static final int MIN_VEL = 60;
    public static final String IMAGE_NAME = "ball.gif"; //FIX to make customizable

    private int myVelX;
    private int myVelY;

    /**
     * Create a game.Ball with an image and initial velocity of 0
     */
    public Ball() {
        this(0, 0, 0, 0);
    }

    /**
     * Create a Ball with an image and specified velocity
     * @param velX
     * @param velY
     */
    public Ball(int x, int y, int velX, int velY) {
        super(IMAGE_NAME, WIDTH, HEIGHT);
        this.setX(x);
        this.setY(y);
        myVelX = velX;
        myVelY = velY;
    }

    /**
     * Multiply and update x component of velocity
     * @param multiplier
     */
    public void multiplyVelX(double multiplier) {
        myVelX = assertExtrema(multiplier, this.getVelX());
    }

    /**
     * Multiply and update x component of velocity
     * @param multiplier
     */
    public void multiplyVelY(double multiplier) {
        myVelY = assertExtrema(multiplier, this.getVelY());
    }

    private int assertExtrema(double multiplier, int oldVel) {
        int newVel = (int) Math.round(multiplier * oldVel);
        boolean isNegative = newVel < 0;
        if (Math.abs(newVel) < MIN_VEL)
            newVel = isNegative ? -1 * MIN_VEL : MIN_VEL;
        if (Math.abs(newVel) > MAX_VEL)
            newVel = isNegative ? -1 * MAX_VEL : MAX_VEL;
        return newVel;
    }

    /**
     * Internally updates x and y positions based on x and y velocities and a specified time step
     * @param elapsedTime
     */
    public void updatePosition(double elapsedTime) {
        this.setX(this.getX() + myVelX * elapsedTime);
        this.setY(this.getY() + myVelY * elapsedTime);
    }

    /**
     * @return x component of velocity
     */
    public int getVelX() { return myVelX; }

    /**
     * @return y component of velocity
     */
    public int getVelY() { return myVelY; }
}
