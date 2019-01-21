package game;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class SplashGameScene extends GameScene {
    public static final Paint BACKGROUND_COLOR = Color.BLACK;

    private boolean myShouldGoToLevels;
    private boolean myShouldGoToCustom;

    /**
     * Create a Splash page
     * @param width
     * @param height
     */
    public SplashGameScene(int width, int height) {
        super(width, height, BACKGROUND_COLOR);
        myShouldGoToLevels = false;
        myShouldGoToCustom = false;
        addText();
        addButtons();
    }

    //method inspired by https://www.geeksforgeeks.org/javafx-button-with-examples/
    private void addButtons() {
        Button playButton = new Button("Play");
        EventHandler<ActionEvent> playEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                myShouldGoToLevels = true;
            }
        };
        playButton.setOnAction(playEvent);
        getRoot().getChildren().add(playButton);

        Button customButton = new Button("Customize");
        EventHandler<ActionEvent> customEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                myShouldGoToCustom = true;
            }
        };
        customButton.setOnAction(customEvent);
        getRoot().getChildren().add(customButton);
    }

    private void addText() {
        //FIX
    }

    /**
     * @return true if the Customization button was pressed
     */
    public boolean getShouldGoToCustom() {
        if (myShouldGoToCustom) {
            myShouldGoToCustom = false;
            return true;
        }
        return false;
    }

    /**
     * @return true if the Play button was pressed
     */
    public boolean getShouldGoToLevels() {
        if (myShouldGoToLevels) {
            myShouldGoToLevels = false;
            return true;
        }
        return false;
    }
}
