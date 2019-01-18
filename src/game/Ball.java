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

    private int myVelX;
    private int myVelY;
    private ImageView myImageView;

    /**
     * Create a game.Ball with an image and initial velocity of 0
     * @param image
     */
    public Ball(Image image) {
        this(image, 0, 0);
    }

    /**
     * Create a Ball with an image and specified velocity
     * @param image
     * @param velX
     * @param velY
     */
    public Ball(Image image, int velX, int velY) {
        myImageView = new ImageView(image);
        myVelX = velX;
        myVelY = velY;
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
     * @param x position to set ball's ImageView to
     */
    public void setX(double x) { myImageView.setX(x); }

    /**
     * @param y position to set ball's ImageView to
     */
    public void setY(double y) { myImageView.setY(y); }

    /**
     * @return x component of velocity
     */
    public int getVelX() { return myVelX; }

    /**
     * @return y component of velocity
     */
    public int getVelY() { return myVelY; }

    /**
     * Set x component of velocity
     * @param newVelX
     */
    public void setVelX(int newVelX) { myVelX = newVelX; }

    /**
     * Set y component of velocity
     * @param newVelY
     */
    public void setVelY(int newVelY) { myVelY = newVelY; }
}
