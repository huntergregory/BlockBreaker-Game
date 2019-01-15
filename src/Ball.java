
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball {
    private ImageView myImageView;
    private int myVelX;
    private int myVelY;

    /**
     * Create a Ball with an image and initial velocity of 0
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
