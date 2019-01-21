package game;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * Holds information pertaining to current situation in the game.
 * Data should be updated after a switch in level, after a block is hit, after a life is lost,
 * or when a powerup is gained or lost.
 * @author Hunter Gregory
 */
public class StatusBar {
    public static final int HEIGHT = 40;
    public static final Paint FILL_COLOR = Color.BLACK;
    public static final int BLOCK_POINTS = 50;

    private int myLevelNumber;
    private int myScore;
    private int myLives;
    private PowerupType myPowerupType;
    private Rectangle myRect;

    /**
     * Create a StatusBar with no assigned attributes.
     */
    public StatusBar(Group root, double width) {
        myRect = new Rectangle(0,0, width, HEIGHT);
        myRect.setFill(FILL_COLOR);
        root.getChildren().add(myRect);
    }

    public String getTextFromType() {
        if (myPowerupType != null)
            return myPowerupType.getText();
        return "Normal";
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
    public void setLives(int livesLeft) { myLives = livesLeft; System.out.println("Set lives to " + myLives);}

    /**
     * Update this bar's score
     * @param score
     */
    public void setScore(int score) { myScore = score; System.out.println("Set score to " + myScore);}

    /**
     * Call when a block is hit to increase the score.
     */
    public void increaseScore() { myScore += BLOCK_POINTS; System.out.println("Increased score to " + myScore);}

    /**
     * Set most recent PowerupType caught
     * @param PowerupType
     */
    public void setPowerupType(PowerupType type) { myPowerupType = type; System.out.println("Set type to " + getTextFromType());}
}
