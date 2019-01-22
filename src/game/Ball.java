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
    public static final int MAX_VEL = 120;
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
     * Update velocity of ball upon collision with wall
     * @param sceneWidth
     * @param sceneHeight
     */
    public void reflectOffWall(double sceneWidth, double sceneHeight) {
        if (this.getY() <= StatusBar.HEIGHT || this.getY() + HEIGHT >= sceneHeight)
            this.multiplyVelY(-1);

        if (this.getX() <= 0 || this.getX() + WIDTH >= sceneWidth)
            this.multiplyVelX(-1);
    }

    /**
     * Update velocity of ball upon collision with any Paddle
     * @param paddles
     */
    public void reflectOffPaddles(ArrayList<Paddle> paddles) {
        Paddle paddleHit = null;
        for (Paddle paddle : paddles) {
            if (this.hitGameObject(paddle))
                paddleHit = paddle;
        }
        if (paddleHit == null)
            return;

        if (!paddleHit.getIsSidePaddle()) {
            int thirdOfWidth = (int) (paddleHit.getWidth() / 3);
            boolean hitLeftThird = this.getX() < paddleHit.getX() + thirdOfWidth;
            boolean hitRightThird = this.getX() > paddleHit.getX() + 2 * thirdOfWidth;
            double multiplier = (hitLeftThird || hitRightThird) ? 1.5 : 0.75;
            this.multiplyVelY(-1 * multiplier);

            if (hitLeftThird && myVelX > 0 || hitRightThird && myVelX <= 0)
                multiplier *= -1;
            this.multiplyVelX(multiplier);
        }
        else {
            //int sixthOfWidth = (int) (paddleHit.getHeight() / 6);
            //boolean hitUpperThird = this.getY() < paddleHit.getY() + sixthOfWidth;
            //boolean hitLowerThird = this.getY() > paddleHit.getY() - sixthOfWidth;
            double multiplier = 1; //(hitUpperThird || hitLowerThird) ? 1.5 : 0.75;
            this.multiplyVelX(-1 * multiplier);

            //if (hitUpperThird && myVelY > 0 || hitLowerThird && myVelY <= 0)
              //  multiplier *= -1;
            this.multiplyVelY(multiplier);
        }
    }

    /**
     * Update the velocity of ball upon collision with Block
     * @param blocks
     * @return block hit
     */
    public Block reflectOffBlock(ArrayList<Block> blocks) {
        for (Block block : blocks) {
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

    /**
     * Multiply x component of velocity by specified amount
     * @param multiplier
     */
    public void multiplyVelX(double multiplier) {
        myVelX = assertExtrema(multiplier, myVelX);
    }

    /**
     * Multiply y component of velocity by specified amount
     * @param multiplier
     */
    public void multiplyVelY(double multiplier) {
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

    /**
     * @param list of Blocks
     * @return Block hit by Ball
     */
    public Block getBlockHit(ArrayList<Block> list) {
        for (Block block : list) {
            if (this.hitGameObject(block))
                return block;
        }
        return null;
    }

    /**
     * @param sceneHeight
     * @return true if Ball hit the floor of the Scene
     */
    public boolean didHitFloor(double sceneHeight) {
        return this.getY() + HEIGHT >= sceneHeight;
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
