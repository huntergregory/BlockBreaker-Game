package game;

/**
 * Consists of a PowerupType and ImageView representing the ball
 * @author Hunter Gregory
 */
public class Powerup extends GameObject implements Movable {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    public static final int VEL_Y = 30;

    private PowerupType myType;
    private boolean myIsHidden;

    public Powerup(PowerupType type) {
        super(type.getImageName(), WIDTH, HEIGHT);
        myType = type;
        setIsHidden(true);
    }

    @Override
    public void updatePosition(double elapsedTime) {
        setY(elapsedTime * VEL_Y + getY());
    }


    /**
     * @param block
     * @return true if Powerup is hidden within the Block.
     */
    public boolean isWithinAlpha(Block block) {
        return this.hitGameObject(block) && block.getType().equals(BlockType.ALPHA); //hitGameObject is really just intersection
    }

    /**
     * A value of true will hide the ImageView
     * @param bool
     */
    public void setIsHidden(boolean bool) {
        myIsHidden = bool;
        if (myIsHidden)
            myImageView.setImage(null);
        else
            setImageFromName(myType.getImageName());
    }

    /**
     * @return PowerupType
     */
    public PowerupType getType() { return myType; }

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
