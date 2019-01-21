package game;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class SplashGameScene extends GameScene {
    public static final Paint BACKGROUND_COLOR = Color.BLACK;
    public static final String TITLE = "Welcome To BlockBreaker!";
    public static final String ADVICE = "Jump into playing or customize your ball and background.";
    public static final String DESCRIPTION = "Try to destroy all the bricks and prevent your balls from falling to the floor.";
    public static final String NORMAL_KEYS = "Press LEFT and RIGHT to move the paddle.";
    public static final String POWERUPS = "Powerups:";
    public static final String POWER_SHOT = "Power Shot - press LEFT and RIGHT to aim, SPACE to shoot.";
    public static final String BIG_PADDLE = "BIG Paddle - (nothing extra)";
    public static final String SPLIT = "Split - (nothing extra)";
    public static final String LASER = "Lasers - press SPACE to shoot";
    public static final String CHEAT_KEYS = "Cheat Keys:";
    public static final String FAST_FORWARD = "Fast forward - press F to speed the balls up";
    public static final String SLOW_MO = "Slow mo - press D to slow the balls down";
    public static final String ACTIVATE_POWERUPS = "Activate your powerups with B for BIG paddle, S for Split, P for Power shot, and L for Laser.";
    public static final String REVIVE = "Revive - press R to give yourself another life";
    public static final String CHANGE_LEVEL = "Change level - press Z to go back a level and X to skip the current level";
    public static final String[] ALL_TEXT = {TITLE, ADVICE, DESCRIPTION, NORMAL_KEYS, POWERUPS, POWER_SHOT,
                                             BIG_PADDLE,SPLIT, LASER,CHEAT_KEYS,FAST_FORWARD,SLOW_MO,
                                                ACTIVATE_POWERUPS,REVIVE,CHANGE_LEVEL};

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
        playButton.relocate(4*myAssignedWidth/5, 9 * myAssignedHeight / 10);

        getRoot().getChildren().add(playButton);

        Button customButton = new Button("Customize");
        EventHandler<ActionEvent> customEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                myShouldGoToCustom = true;
            }
        };
        customButton.setOnAction(customEvent);
        customButton.relocate(myAssignedWidth/5, 9 * myAssignedHeight / 10);
        getRoot().getChildren().add(customButton);
    }

    private void addText() {
        String allStrings = "";
        for (String string : ALL_TEXT) {
            allStrings += string + "\n";
        }
        Text fullText = new Text(allStrings);
        fullText.setFill(Color.WHITE);
        fullText.setWrappingWidth(9 * myAssignedWidth / 10);
        fullText.relocate(myAssignedWidth / 20, myAssignedHeight / 20);
        getRoot().getChildren().add(fullText);
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
