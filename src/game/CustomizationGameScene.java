package game;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class CustomizationGameScene extends GameScene {
    public static final Paint BACKGROUND_COLOR = Color.BLACK;
    public static final String DEFAULT_IMAGE = "ball.gif";
    public static final Color DEFAULT_COLOR = Color.WHITE;
    public static final Paint[] COLOR_OPTIONS = { Color.TOMATO, Color.AQUA, Color.WHITE };

    private Button[] myColorButtons;
    private String mySelectedImage;
    private Color mySelectedColor;
    private boolean myShouldGoToSplash;

    public CustomizationGameScene(int width, int height) {
        super(width,height,BACKGROUND_COLOR);
        mySelectedImage = DEFAULT_IMAGE;
        mySelectedColor = Color.WHITE;
        myShouldGoToSplash = false;
        addColorButtons();
        addBallButtons();
        //addExitButton(); //not enough time
    }

    private void addColorButtons() {
        myColorButtons = new Button[COLOR_OPTIONS.length];
        Paint[] options = COLOR_OPTIONS;
        for (int k=0; k<myColorButtons.length; k++) {
            Button button = new Button("Customize");
            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    //mySelectedColor = options[k]; //variable k needs to be "effectively final" for some reason so that doesn't work
                }
            };
            button.setOnAction(event);
            button.relocate(k*myAssignedWidth/myColorButtons.length, myAssignedHeight / 4);
            myColorButtons[k] = button;
            getRoot().getChildren().add(button);
        }
    }

    private void addBallButtons() {
        //FIX, ran out of time
        //Make like addColorButtons
    }


    public boolean getShouldGoToSplash() {
        if (myShouldGoToSplash) {
            myShouldGoToSplash = false;
            return true;
        }
        return false;
    }


}
