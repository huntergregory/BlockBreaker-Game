package game;

import java.util.ArrayList;

/**
 * Represents a laser coming out of a Paddle, capable of destroying Blocks.
 * @author Hunter Gregory
 */
public class Laser extends GameObject implements Movable {
    public static final int WIDTH = 3;
    public static final int HEIGHT = 12;
    public static final int VEL_Y = -200;
    private static final String IMAGE_NAME = "brick2.gif";

    /**
     * Create a Laser
     * @param x position
     * @param y position
     */
    public Laser(double x, double y) {
        super(IMAGE_NAME,WIDTH,HEIGHT);
        this.setX(x);
        this.setY(y);
    }

    @Override
    public void updatePosition(double elapsedTime) {
        this.setY(this.getY() + elapsedTime * VEL_Y);
    }

    /**
     * @param list of Blocks
     * @return Block hit by Laser
     */
    public Block getBlockHit(ArrayList<Block> list) {
        for (Block block : list) {
            if (this.hitGameObject(block))
                return block;
        }
        return null;
    }
}
