package game;

import java.util.ArrayList;

/**
 * An ImageView with velocity components, representing a ball.
 * Provides methods to internally update velocity upon hitting multiple GameObjects
 * @author Hunter Gregory
 */
public class Ball extends GameObject implements Movable {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    public static final int MAX_VEL = 160;
    public static final int MIN_VEL = 60;
    public static final String IMAGE_NAME = "ball.gif"; //FIX to make customizable

    private int myVelX;
    private int myVelY;
    private boolean myIsHalted;

    /**
     * Create a Ball with an image and initial velocity of 0
     */
    public Ball() {
        this(0, 0, 0, 0);
    }

    /**
     * Create a Ball with an image and specified velocity
     * @param velX
     * @param velY
     */
    public Ball(double x, double y, int velX, int velY) {
        super(IMAGE_NAME, WIDTH, HEIGHT);
        this.setX(x);
        this.setY(y);
        myVelX = velX;
        myVelY = velY;
        myIsHalted = false;
    }

    @Override
    public void updatePosition(double elapsedTime) {
        this.setX(this.getX() + myVelX * elapsedTime);
        this.setY(this.getY() + myVelY * elapsedTime);
    }

    /**
     * Updates the velocity of the ball
     * @param level
     * @param paddle
     * @return ArrayList of Blocks hit
     */
    public Block reflectOffAnyObstacles(Level level, Paddle paddle, ArrayList<Block> blocks) {
        reflectOffWall(level);
        reflectOffPaddle(paddle);
        return reflectOffBlock(blocks);
    }

    private void reflectOffWall(Level level) {
        if (this.getY() <= 0 || this.getY() + HEIGHT >= level.getCurrentHeight())
            this.multiplyVelY(-1);

        if (this.getX() <= 0 || this.getX() + WIDTH >= level.getCurrentWidth())
            this.multiplyVelX(-1);
    }

    private Block reflectOffBlock(ArrayList<Block> list) {
        for (Block block : list) {
            if (!this.hitGameObject(block))
                continue;
            double multiplier = block.getMultiplier();
            boolean vertical = isVerticalCollision(block);
            this.multiplyVelX(vertical ? multiplier : -1 * multiplier);
            this.multiplyVelY(vertical ? -1 * multiplier : multiplier);
            return block;
        }
        return null;
    }

    //FIX
    private boolean isVerticalCollision(Block block) {
        double centerX = this.getX() + WIDTH / 2;
        return block.getX() < centerX && centerX < block.getX() + block.getWidth();
    }

    private void reflectOffPaddle(Paddle paddle) {
        if (!this.hitGameObject(paddle))
            return;

        int thirdOfWidth = (int) (paddle.getWidth() / 3);
        boolean hitLeftThird = this.getX() < paddle.getX() + thirdOfWidth;
        boolean hitRightThird = this.getX() > paddle.getX() + 2 * thirdOfWidth;
        double multiplier = (hitLeftThird || hitRightThird) ? 1.5 : 0.75;

        this.multiplyVelY(-1 * multiplier);

        if (hitLeftThird && myVelX > 0 || hitRightThird && myVelX <= 0)
            multiplier *= -1;
        this.multiplyVelX(multiplier);
    }

    private void multiplyVelX(double multiplier) {
        myVelX = assertExtrema(multiplier, myVelX);
    }

    private void multiplyVelY(double multiplier) {
        myVelY = assertExtrema(multiplier, myVelY);
    }

    private int assertExtrema(double multiplier, int oldVel) {
        if (myIsHalted)
            return oldVel;
        int newVel = (int) Math.round(multiplier * oldVel);
        boolean isNegative = newVel < 0;
        if (Math.abs(newVel) < MIN_VEL)
            newVel = isNegative ? -1 * MIN_VEL : MIN_VEL;
        if (Math.abs(newVel) > MAX_VEL)
            newVel = isNegative ? -1 * MAX_VEL : MAX_VEL;
        return newVel;
    }

    /**
     * Brings Ball's velocity to the maximum y value and adjusts the x velocity accordingly
     * to send the Ball in the specified direction
     * @param angle
     */
    public void unhaltWithMaxVelocity(double angle) {
        myVelY = -MAX_VEL;
        int tangent = (int) (Math.tan(Math.abs(Math.toRadians(angle))) * Math.abs(myVelY));
        myVelX = (angle >= 0) ? tangent : -tangent;
        myIsHalted = false;
    }

    /**
     * Sets the Ball's velocity to zero
     */
    public void halt() {
        myVelX = 0;
        myVelY = 0;
        myIsHalted = true;
    }

    public boolean hitFloor(GameScene gameScene) {
        return this.getY() + HEIGHT >= gameScene.getCurrentHeight();
    }

    /**
     * @return x component of velocity
     */
    public int getVelX() { return myVelX; }

    /**
     * @return y component of velocity
     */
    public int getVelY() { return myVelY; }
}
