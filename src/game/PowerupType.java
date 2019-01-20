package game;

import java.util.Random;

/**
 * All the types of powerups
 * @author Hunter Gregory
 */
public enum PowerupType {
    LASER(0, "laserpower.gif"),
    BALL_SPLIT(1, "extraballpower.gif"),
    OVERSIZE(2, "sizepower.gif"),
    POWER_SHOT(3, "pointspower.gif"); //renamed to power shot (not supercharge) to align with "p" on gif

    private int myId;
    private String myImageName;

    PowerupType(int id, String name) {
        myId = id;
        myImageName = name;
    }

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
