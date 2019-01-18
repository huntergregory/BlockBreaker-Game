package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * An ImageView with velocity components, representing a ball.
 * Position must be set using ImageView methods after the object is created.
 * @author Hunter Gregory
 */
public class Ball {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    public static final int MAX_VEL = 120;
    public static final int MIN_VEL = 50;
    public static final String IMAGE_NAME = "ball.gif"; //FIX to make customizable

    private int myVelX;
    private int myVelY;
    private ImageView myImageView;

    /**
     * Create a game.Ball with an image and initial velocity of 0
     * @param image
     */
    public Ball(Image image) {
        this(image, 0, 0, 0, 0);
    }

    /**
     * Create a Ball with an image and specified velocity
     * @param image
     * @param velX
     * @param velY
     */
    public Ball(Image image, int x, int y, int velX, int velY) {
        myImageView = new ImageView(image);
        myImageView.setX(x);
        myImageView.setY(y);
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

    public void updatePosition(double elapsedTime) {
        myImageView.setX(this.getX() + myVelX * elapsedTime);
        myImageView.setY(this.getY() + myVelY * elapsedTime);
    }

    /**
     * @return ImageView representing ball
     */
    public ImageView getImageView() { return myImageView; }

    /**
     * @return width of ball's ImageView
     */
    public double getWidth() {return myImageView.getBoundsInParent().getWidth(); }

    /**
     * @return height of ball's ImageView
     */
    public double getHeight() { return myImageView.getBoundsInParent().getHeight(); }

    /**
     * @return x position of ball's ImageView
     */
    public double getX() { return myImageView.getX(); }

    /**
     * @return y position of ball's ImageView
     */
    public double getY() { return myImageView.getY(); }

    /**
     * @return x component of velocity
     */
    public int getVelX() { return myVelX; }

    /**
     * @return y component of velocity
     */
    public int getVelY() { return myVelY; }
}
