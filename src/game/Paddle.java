package game;

import javafx.scene.shape.Rectangle;

/**
 * @author Hunter Gregory
 */
public class Paddle {
    public static final int DEFAULT_WIDTH = Block.WIDTH;
    public static final int DEFAULT_HEIGHT = Block.HEIGHT;

    private Rectangle myRect;

    /**
     * Create a game.Paddle with an empty, rounded rectangle
     */
    public Paddle() {
        myRect = new Rectangle();
        myRect.setWidth(DEFAULT_WIDTH);
        myRect.setHeight(DEFAULT_HEIGHT);
        myRect.setArcWidth(20); //making paddle a rounded rectangle
        myRect.setArcHeight(20);
    }

    /**
     * @return Paddle's rectangle
     */
    public Rectangle getRect() { return myRect; }

    /**
     * @return x position of Paddle's Rectangle
     */
    public double getX() { return myRect.getX(); }

    /**
     * @return y position of Paddle's Rectangle
     */
    public double getY() { return myRect.getY(); }

    /**
     * @param x position to set Paddle's Rectangle to
     */
    public void setX(double x) { myRect.setX(x);}

    /**
     * @param y position to set Paddle's Rectangle to
     */
    public void setY(double y) { myRect.setY(y); }
}
