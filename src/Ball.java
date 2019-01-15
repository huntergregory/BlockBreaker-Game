
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball extends ImageView {
    private int myVelX = 0;
    private int myVelY = 0;

    /**
     * Normal constructor for ball with intial velocity of 0
     * @param image
     */
    public Ball(Image image) {
        super(image);
    }

    /**
     * Constructor that specifies velocity
     * @param image
     * @param velX
     * @param velY
     */
    public Ball(Image image, int velX, int velY) {
        this(image);
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
