package game;

/**
 * All the types of powerups
 * @author Hunter Gregory
 */
public enum PowerupType {
    LASER("laserpower.gif"),
    BALL_SPLIT("extraballpower.gif"),
    OVERSIZE("sizepower.gif"),
    POWER_SHOT("pointspower.gif"); //renamed to power shot (not supercharge) to align with "p" on gif

    private String myImageName;

    PowerupType(String name) {
        myImageName = name;
    }

    /**
     * @return name of image corresponding to powerup
     */
    public String getImageName() { return myImageName; }
}
