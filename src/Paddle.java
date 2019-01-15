
import javafx.scene.shape.Rectangle;

public class Paddle {

    private Rectangle myRect;

    /**
     * Create a Paddle with an empty, rounded rectangle
     */
    public Paddle() {
        myRect = new Rectangle();
        myRect.setArcWidth(20); //making paddle a rounded rectangle
        myRect.setArcHeight(20);
    }

    /**
     * @return Paddle's rectangle
     */
    public Rectangle getRect() { return myRect; }

}
