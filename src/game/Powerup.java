package game;

/**
 * Consists of a PowerupType and ImageView representing the ball
 * @author Hunter Gregory
 */
public class Powerup extends GameObject{
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    public static final int VEL_Y = 20;

    private PowerupType myType;
    private boolean myIsHidden;

    public Powerup(PowerupType type) {
        super(type.getImageName(), WIDTH, HEIGHT);
        myType = type;
        setIsHidden(true);
    }

    /**
     * A value of true will hide the ImageView
     */
    public void setIsHidden(boolean bool) {
        myIsHidden = bool;
        if (myIsHidden)
            myImageView.setImage(null);
        else
            setImageFromName(myType.getImageName());
    }

    /**
     * @return true if ImageView is hidden
     */
    public boolean getIsHidden() { return myIsHidden; }

    /**
     * @return random Powerup
     */
    public static Powerup getRandomPowerup() {
        return new Powerup(PowerupType.getRandomType());
    }
}
