package game;

import java.util.ArrayList;

public class Laser extends GameObject implements Movable {
    public static final int WIDTH = 3;
    public static final int HEIGHT = 12;
    public static final int VEL_Y = -160;
    private static final String IMAGE_NAME = "brick2.gif";

    /**
     * Create a Laser
     * @param x
     * @param y
     */
    public Laser(Paddle paddle) {
        super(IMAGE_NAME,WIDTH,HEIGHT);
        this.setX(paddle.getX() + paddle.getWidth() / 2);
        this.setY(paddle.getY());
    }

    @Override
    public void updatePosition(double elapsedTime) {
        this.setY(this.getY() + elapsedTime * VEL_Y);
    }

    /**
     * @param list of Blocks
     * @return Block hit by laser
     */
    public Block getBlockHit(ArrayList<Block> list) {
        for (Block block : list) {
            if (this.hitGameObject(block))
                return block;
        }
        return null;
    }
}
