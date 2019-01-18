package game;

import javafx.scene.shape.Rectangle;

/**
 * @author Hunter Gregory
 */
public class Paddle {
    public static final int DEFAULT_WIDTH = 60;
    public static final int HEIGHT = 10;

    private Rectangle myRect;

    /**
     * Create a game.Paddle with an empty, rounded rectangle
     */
    public Paddle(int x, int y) {
        myRect = new Rectangle();
        myRect.setX(x);
        myRect.setY(y);
        myRect.setWidth(DEFAULT_WIDTH);
        myRect.setHeight(HEIGHT);
        myRect.setArcWidth(10); //making paddle a rounded rectangle
        myRect.setArcHeight(10);
    }

    public void updateRectWidth(double length) {
        myRect.setWidth(length); //FIX animate it
    }

    /**
     * @return Paddle's Rectangle
     */
    public Rectangle getRect() { return myRect; }

    /**
     * @return width of Paddle's Rectangle
     */
    public double getWidth() { return myRect.getBoundsInParent().getWidth(); }

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
