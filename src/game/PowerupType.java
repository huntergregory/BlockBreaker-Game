package game;

import java.util.Random;

/**
 * All the types of powerups
 * @author Hunter Gregory
 */
public enum PowerupType {
    LASER(0, "Laser!", "laserpower.gif"),
    SPLIT(1, "Split!", "sizepower.gif"),
    BIG_PADDLE(2, "BIG Paddle!", "extraballpower.gif"),
    POWER_SHOT(3, "Power Shot!", "pointspower.gif"); //renamed to power shot (not supercharge) to align with "p" on gif

    private int myId;
    private String myText;
    private String myImageName;

    PowerupType(int id, String text, String name) {
        myId = id;
        myText = text;
        myImageName = name;
    }

    /**
     * @return text associated with the type
     */
    public String getText() { return myText; }

    /**
     * @return name of image corresponding to Powerup
     */
    public String getImageName() { return myImageName; }

    /**
     * @return random PowerupType
     */
    public static PowerupType getRandomType() {
        var rand = new Random();
        int randId = rand.nextInt(4); // 4 is the number of PowerupTypes
        for (PowerupType type : PowerupType.values()) {
            if (randId == type.myId)
                return type;
        }
        return null; //won't ever reach
    }
}
