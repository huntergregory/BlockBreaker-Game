package game;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

/**
 * An object which draws and toggles Rectangles indicating that a game is paused.
 * @author Hunter Gregory
 */
public class Pauser {
    public static final int PAUSE_RECT_WIDTH = 30;
    public static final int PAUSE_RECT_HEIGHT = 80;

    private Rectangle myPauseRect1;
    private Rectangle myPauseRect2;
    private int myWidth;
    private int myHeight;
    private Group myRoot;
    private boolean myIsPaused;

    /**
     * Create a Pauser for a Scene
     * @param width
     * @param height
     * @param root
     */
    public Pauser(int width, int height, Group root) {
        myWidth = width;
        myHeight = height;
        myRoot = root;
        myPauseRect1 = new Rectangle(myWidth / 2 - 3 * PAUSE_RECT_WIDTH / 2,
                                    myHeight / 2 - PAUSE_RECT_HEIGHT,
                                        PAUSE_RECT_WIDTH,
                                        PAUSE_RECT_HEIGHT);
        myPauseRect2  = new Rectangle(myWidth / 2 + PAUSE_RECT_WIDTH / 2,
                                      myHeight / 2 - PAUSE_RECT_HEIGHT,
                                          PAUSE_RECT_WIDTH,
                                          PAUSE_RECT_HEIGHT);
    }

    /**
     * Add or remove pause symbols
     * @param bool
     */
    public void setIsPaused(boolean bool) {
        myIsPaused = bool;
        if (bool) {
            myRoot.getChildren().add(myPauseRect1);
            myRoot.getChildren().add(myPauseRect2);
        }
        else {
            myRoot.getChildren().remove(myPauseRect1);
            myRoot.getChildren().remove(myPauseRect2);
        }
    }

    /**
     * @return true if Pause status is on
     */
    public boolean getIsPaused() { return myIsPaused; }
}
