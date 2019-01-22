package game;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Holds information pertaining to current situation in the game.
 * Data should be updated after a switch in level, after a block is hit, after a life is lost,
 * or when a powerup is gained or lost.
 * @author Hunter Gregory
 */
public class StatusBar {
    public static final int HEIGHT = 40;
    public static final Paint BAR_COLOR = Color.BLACK;
    public static final Paint TEXT_COLOR = Color.WHITE;
    public static final int FONT_SIZE = 10;
    public static final int BLOCK_POINTS = 50;
    public static final int LEVEL_TEXT = 0;
    public static final int SCORES_TEXT = 1;
    public static final int LIVES_TEXT = 2;
    public static final int POWERUP_TEXT = 3;

    private int myLevelNumber;
    private int myScore;
    private int myLives;
    private PowerupType myPowerupType;
    private Rectangle myRect;
    private Text[] myTexts;
    /*myLevelText;
    private Text myScoreText;
    private Text myLivesText;
    private Text myTypeText;*/

    /**
     * Create a StatusBar with no assigned attributes.
     */
    public StatusBar(Group root, double width) {
        myRect = new Rectangle(0,0, width, HEIGHT);
        myRect.setFill(BAR_COLOR);
        root.getChildren().add(myRect);
        double textSeparation = width / 10;
        double yPos = 3 * HEIGHT / 4;
        myTexts = new Text[4];
        myTexts[LEVEL_TEXT] = new Text(textSeparation, yPos, getLevelString());
        myTexts[SCORES_TEXT] = new Text(3 * textSeparation, yPos, getScoreString());
        myTexts[LIVES_TEXT] = new Text(6 * textSeparation, yPos, getLivesString());
        myTexts[POWERUP_TEXT] = new Text(8 * textSeparation, yPos, getPowerupString());
        for (Text text : myTexts) {
            text.setFill(TEXT_COLOR);
            text.setFont(new Font(FONT_SIZE));
            root.getChildren().add(text);
        }
    }

    /**
     * Refreshes the StatusBar after its data has been updated
     */
    public void updateText() {
        myTexts[LEVEL_TEXT].setText(getLevelString());
        myTexts[SCORES_TEXT].setText(getScoreString());
        myTexts[LIVES_TEXT].setText(getLivesString());
        myTexts[POWERUP_TEXT].setText(getPowerupString());
    }

    /**
     * Update level number
     * @param levelNumber
     */
    public void setLevelNumber(int levelNumber) { myLevelNumber = levelNumber; }

    /**
     * Update lives left
     * @param livesLeft
     */
    public void setLives(int livesLeft) { myLives = livesLeft; }

    /**
     * Update this bar's score
     * @param score
     */
    public void setScore(int score) { myScore = score; }

    /**
     * Call when a block is hit to increase the score.
     */
    public void increaseScore() { myScore += BLOCK_POINTS; }

    /**
     * Set most recent PowerupType caught
     * @param PowerupType
     */
    public void setPowerupType(PowerupType type) { myPowerupType = type;  }

    /**
     * @return get Score of current level
     */
    public int getScore() { return myScore; }

    private String getScoreString() {
        return "Score: " + myScore;
    }

    private String getLivesString() {
        return "Lives: " + myLives;
    }

    private String getLevelString() {
        return "Level: " + myLevelNumber;
    }

    private String getPowerupString() {
        if (myPowerupType != null)
            return myPowerupType.getString();
        return "Normal";
    }
}
