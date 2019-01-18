package game;

/**
 * Contains all predefined properties of all block types.
 * Properties are remainingHits until block is destroyed (-1 for indestructible blocks),
 * reflectionMultiplier (value used to increase, decrease, or maintain ball's velocity after
 * reflecting off the block), and the fillColor of the block.
 * @author Hunter Gregory
 */
public enum BlockType {
    ALPHA(1, 1, "brick6.gif"), // block with 1 hit remaining
    BETA(2, 1, "brick7.gif"),  // block with 2 hits remaining
    GAMMA(3, 1, "brick8.gif"), // block with 3 hits remaining
    METAL(Block.INDESTRUCTIBLE, 1, "brick3.gif"), // normal indestructible block
    SAND(Block.INDESTRUCTIBLE, 0.5, "brick5.gif"), // indestructible, velocity-dampening block
    TRAMPOLINE(Block.INDESTRUCTIBLE, 1.5, "brick4.gif"); // indestructible, velocity-increasing block

    private int myRemainingHits;
    private double myReflectionMultiplier;
    private String myImageName;

    //-1 remaining hits represents an indestructible block
    BlockType(int remainingHits, double reflectionMultiplier, String imageName) {
        myRemainingHits = remainingHits;
        myReflectionMultiplier = reflectionMultiplier;
        myImageName = imageName;
    }

    /**
     * @return remaining hits for type
     */
    public int getHitsRemaining() { return myRemainingHits; }

    /**
     * @return type's reflection multiplier
     */
    public double getMultiplier() { return myReflectionMultiplier; }

    /**
     * @return type's image
     */
    public String getImageName() { return myImageName; }
}