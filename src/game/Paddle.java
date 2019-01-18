package game;

/**
 * @author Hunter Gregory
 */
public class Paddle extends GameObject {
    public static final int DEFAULT_WIDTH = 60;
    public static final int HEIGHT = 10;
    public static final String IMAGE_NAME = "paddle.gif";

    /**
     * Create a game.Paddle with an empty, rounded rectangle
     */
    public Paddle(int x, int y) {
        super(IMAGE_NAME, DEFAULT_WIDTH, HEIGHT);
        this.setX(x);
        this.setY(y);
    }

    /**
     * Set width of ImageView associated with Paddle
     * @param length
     */
    public void setWidth(double length) {
        this.getImageView().setFitWidth(length); //FIX animate it
    }

    /**
     * @return width of Paddle's Rectangle
     */
    public double getWidth() { return this.getImageView().getBoundsInParent().getWidth(); }
}
