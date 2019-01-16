package game;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

/**
 *
 * @author Hunter Gregory
 */
public class Block extends ImageView {
    private final static int INDESTRUCTIBLE = -1;

    /**
     * Contains all predefined properties of all block types.
     * Properties are remainingHits until block is destroyed (-1 for indestructible blocks),
     * reflectionMultiplier (value used to increase, decrease, or maintain ball's velocity after
     * reflecting off the block), and the fillColor of the block.
     */
    protected enum BLOCK_TYPE {
        ALPHA(1, 1, "brick6.gif"), // block with 1 hit remaining
        BETA(2, 1, "brick7.gif"),  // block with 2 hits remaining
        GAMMA(3, 1, "brick8.gif"), // block with 3 hits remaining
        METAL(INDESTRUCTIBLE, 1, "brick3.gif"), // normal indestructible block
        SAND(INDESTRUCTIBLE, 0.5, "brick5.gif"), // indestructible, velocity-dampening block
        TRAMPOLINE(INDESTRUCTIBLE, 1.5, "brick4.gif"); // indestructible, velocity-increasing block

        private int remainingHits;
        private double reflectionMultiplier;
        private String image;

        //internal constructor
        BLOCK_TYPE(int remainingHits, double reflectionMultiplier, String image) {
            this.remainingHits = remainingHits;
            this.reflectionMultiplier = reflectionMultiplier;
            this.image = image;
        }

        /**
         * @return remaining hits for block
         */
        public int getHitsRemaining() { return remainingHits; }

        /**
         * @return block's reflection multiplier
         */
        public double getMultiplier() { return reflectionMultiplier; }

        /**
         * @return block's image
         */
        public String getImage() { return image; }
    }

    private BLOCK_TYPE myType;

    /**
     * Create an alpha block
     */
    public Block() {
        this(BLOCK_TYPE.ALPHA);
    }

    /**
     * Create a game.Block with specified type and unspecified rectangle dimensions and position
     * @param type of block
     */
    public Block(BLOCK_TYPE type) {
        super();
        myType = type;
        updateBlockImage();
    }

    private void updateBlockImage() {
        Image image = new Image(getClass().getClassLoader().getResourceAsStream(myType.getImage()));
        this.setImage(image);
    }

    /**
     * Updates internal state upon a collision with a ball.
     */
    public void collision() {
        BLOCK_TYPE nextType = getNextType();
        if (nextType == myType)
            return;
        transitionToBlock(nextType); // assumes input != null i.e. block will be transitioning
    }

    //returns null if the current object is an alpha block
    //and returns myType if the current block is indestructible
    //FIX comments when considering super ball powerup
    private BLOCK_TYPE getNextType() {
        BLOCK_TYPE nextType = null;
        int currentHits = myType.getHitsRemaining();
        if (currentHits != INDESTRUCTIBLE) {
            for (BLOCK_TYPE type : BLOCK_TYPE.values()) {
                if (type.getHitsRemaining() == currentHits - 1) {
                    nextType = type;
                    break;
                }
            }
        }
        return nextType;
    }

    private void transitionToBlock(BLOCK_TYPE nextType) {
        myType = nextType;
        updateBlockImage();
    }

    /**
     * @return block's reflection multiplier
     */
    public double getMultiplier() { return myType.getMultiplier(); }

    /**
     * Position the Block
     * @param x
     * @param y
     */
    public void setPosition(double x, double y) {
        this.setX(x);
        this.setY(y);
    }
}