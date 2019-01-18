package game;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

/**
 * An ImageView representing a multitude of different block types, some of which are indestructible, and some of
 * which dampen or increase the velocity of a reflected ball. Each instance internally deals with changing its
 * block type upon a call to the updateOnCollision() method.
 * @author Hunter Gregory
 */
public class Block {
    public final static int INDESTRUCTIBLE = -1;
    public static final int WIDTH = 45;
    public static final int HEIGHT = 15;

    private BlockType myType;
    private ImageView myImageView;

    /**
     * Default constructor creates an alpha block
     */
    public Block() {
        this(BlockType.ALPHA);
    }

    /**
     * Create a game.Block with specified type and unspecified rectangle dimensions and position
     * @param type of block
     */
    public Block(BlockType type) {
        myType = type;
        myImageView = new ImageView();
        updateBlockImage();
    }

    private void updateBlockImage() {
        Image image = new Image(getClass().getClassLoader().getResourceAsStream(myType.getImageName()));
        myImageView.setImage(image);
    }

    /**
     * Updates internal state and updates block's type if necessary.
     * @return true if block should be destroyed after collision, false otherwise
     */
    public boolean updateOnCollision() {
        BlockType nextType = getNextType();
        if (nextType == null) {
            return true;
        }
        myType = nextType;
        updateBlockImage();
        return false;
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
     * @return ImageView representing the block
     */
    public ImageView getImageView() { return myImageView; }

    /**
     * @return block's reflection multiplier
     */
    public double getMultiplier() { return myType.getMultiplier(); }

    /**
     * Positions the Block
     * @param x
     * @param y
     */
    public void setPosition(double x, double y) {
        myImageView.setX(x);
        myImageView.setY(y);
    }

    /**
     * @return x position of block's ImageView
     */
    public double getX() {
        return myImageView.getX();
    }

    /**
     * @return y position of block's ImageView
     */
    public double getY() {
        return myImageView.getY();
    }
}