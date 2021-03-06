package game;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

/**
 * An ImageView representing a multitude of different block types, some of which are indestructible, and some of
 * which dampen or increase the velocity of a reflected ball. Each instance internally deals with changing its
 * block type upon a call to the updateOnCollision() method.
 * @author Hunter Gregory
 */
public class Block extends GameObject {
    public final static int INDESTRUCTIBLE = -1;
    public static final int WIDTH = 45;
    public static final int HEIGHT = 15;

    private BlockType myType;

    /**
     * Default constructor creates an alpha block
     */
    public Block() {
        this(0, 0, BlockType.ALPHA);
    }

    /**
     * Create a game.Block with specified type and unspecified rectangle dimensions and position
     * @param type of block
     */
    public Block(double x, double y, BlockType type) {
        super(type.getImageName(), WIDTH, HEIGHT);
        myType = type;
        this.setX(x);
        this.setY(y);
    }

    /**
     * Updates internal state and updates block's type if necessary.
     * @return true if block still has more 'lives'
     */
    public boolean updateOnCollision() {
        BlockType nextType = getNextType();
        if (nextType == null) {
            return false;
        }
        myType = nextType;
        setImageFromName(myType.getImageName());
        return true;
    }

    //returns null if the current object is an alpha block
    //and returns myType if the current block is indestructible
    //FIX comments when considering super ball powerup
    private BlockType getNextType() {
        BlockType nextType = null;
        int currentHits = myType.getHitsRemaining();
        if (currentHits != INDESTRUCTIBLE) {
            for (BlockType type : BlockType.values()) {
                if (type.getHitsRemaining() == currentHits - 1) {
                    nextType = type;
                    break;
                }
            }
        }
        return nextType;
    }

    /**
     * @return this Block's BlockType
     */
    public BlockType getType() { return myType; }

    /**
     * @return block's reflection multiplier
     */
    public double getMultiplier() { return myType.getMultiplier(); }
}