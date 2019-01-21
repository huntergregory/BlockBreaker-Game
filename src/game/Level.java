package game;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * Creates a template for any level in this game.
 * @author Hunter Gregory
 */
public abstract class Level extends GameScene {
    public static final int SEPARATION_DISTANCE = 5;
    public static final int NUM_POWERUPS = 5;

    private ArrayList<Block> myBlockConfiguration;     //implement algorithm to configure in subclass
    private ArrayList<Powerup> myPowerupConfiguration; //implement algorithm to configure in subclass
    private ArrayList<Block> myBlocks;
    private ArrayList<Block> myIndestructibleBlocks;
    private ArrayList<Powerup> myPowerups;
    private ArrayList<Powerup> myFallingPowerups;
    private ArrayList<Ball> myBalls;
    private ArrayList<Laser> myLasers;
    private Paddle myPaddle;
    private Random myRand;
    private Pauser myPauser;
    private int myLives;
    private StatusBar myBar;


    /**
     * Create a Level with default white Background and default random seed.
     * @param width
     * @param height
     */
    protected Level(int width, int height) {
        this(width, height, Color.WHITE, new Random());
    }

    /**
     * Create Level with specified background color and random seed.
     * @param width
     * @param height
     * @param backgroundColor
     * @param rand
     */
    protected Level(int width, int height, Paint backgroundColor, Random rand) {
        super(width, height, backgroundColor);
        myRand = rand;
        myBlockConfiguration = new ArrayList<>();
        myPowerupConfiguration = new ArrayList<>();
        myBlocks = new ArrayList<>();
        myIndestructibleBlocks = new ArrayList<>();
        myPowerups = new ArrayList<>();
        myFallingPowerups = new ArrayList<>();
        myBalls = new ArrayList<>();
        myLasers = new ArrayList<>();
        myPauser = new Pauser(myAssignedWidth, myAssignedHeight, myRoot);
        myLives = 0;
        myBar = null;
        myPaddle = new Paddle(0,0); //initiate this to prevent a null pointer exception in resetPaddleAndBalls
    }

     /*
    ------------------------------------------------
                    Reset Methods
    ------------------------------------------------
     */

    /**
     * Equivalent to calling resetBlocksAndPowerups() and then resetPaddleAndBalls()
     */
    protected void resetLevel() {
        resetBlocksAndPowerups();
        resetPaddleAndBalls();
        myBar = new StatusBar(this.getRoot(), this.getAssignedWidth());
    }

    /**
     * Resets blocks and powerups to their respective configurations.
     */
    protected void resetBlocksAndPowerups() {
        myBlocks = myBlockConfiguration;
        myPowerups = myPowerupConfiguration;
    }

    /**
     * Creates a new Paddle and new list of one Ball, each in their default positions.
     * Call at the beginning of a new Level or when restarting after losing a life.
     */
    protected void resetPaddleAndBalls() {
        removeGameObjectFromRoot(myPaddle);
        myBalls = new ArrayList<>();
        Ball ball = new Ball(100, 350, 60, -65); // FIX magic numbers
        myBalls.add(ball);
        this.addGameObjectToRoot(ball);

        myPaddle = new Paddle(myAssignedWidth - myAssignedWidth / 2 - Paddle.DEFAULT_WIDTH / 2,
                                    myAssignedHeight - Paddle.HEIGHT - 2);
        this.addGameObjectToRoot(myPaddle);
    }

    /*
    ------------------------------------------------
                    Mutator Methods
    ------------------------------------------------
     */

    /**
     * Update all instance variables to animate
     * @param elapsedTime
     */
    protected void step(double elapsedTime) {
        myBar.updateText();
        if (myLives == 0)
            return;

        for (Powerup powerup : myFallingPowerups)
            powerup.updatePosition(elapsedTime);

        for (Laser laser : myLasers)
            laser.updatePosition(elapsedTime);

        for (Ball ball : myBalls)
            ball.updatePosition(elapsedTime);

        catchPowerups();
        reflectBallsOffObstacles();
        ArrayList<Block> blocksHit = getBlocksHit(); //Can't delete blocks until after reflecting balls and deleting lasers
        deleteLasers(); //can't delete lasers before finding blocks to delete
        for (int k=0; k<blocksHit.size(); k++)
            myBar.increaseScore();
        updateBlocksAndPowerups(blocksHit);
        deleteBallsOutOfBounds();           //can't delete balls until after finding blocks to delete
        if (myBalls.size() == 0) {
            resetPaddleAndBalls();
            setLives(myLives - 1);
        }
            //if (getBlocksLeft() == 0)
    }


    //FIX, all but split need timers
    private void catchPowerups() {
        ArrayList<Powerup> caughtPowerups = new ArrayList<>();
        for (Powerup powerup : myFallingPowerups) {
            if (!powerup.hitGameObject(myPaddle))
                continue;

            caughtPowerups.add(powerup);
            this.removeGameObjectFromRoot(powerup);
            switch (powerup.getType()) {
                case LASER: {
                    myPaddle.setCanShootLasers(true);
                    break;
                }
                case SPLIT: {
                    splitBalls();
                    break; //FIX
                }
                case BIG_PADDLE: {
                    myPaddle.makeBig();
                }
                case POWER_SHOT:{
                    myPaddle.initPowerShot();
                    break; //FIX
                }
            }
            myBar.setPowerupType(powerup.getType());
            System.out.println("setting type from level");
        }
        myFallingPowerups.removeAll(caughtPowerups);
        myPowerups.removeAll(caughtPowerups);
        //set boolean to eventually set ball velocity to 0 if power_shot
        //check to see if a power up fell way past the screen <-- DELETE them
    }

    protected void splitBalls() {
        if (myPaddle.getAimer().getIsCurrentlyAiming())
            return;
        ArrayList<Ball> newBalls = new ArrayList<>();
        int[] xMult = {-1, -1, 1};
        int[] yMult = {-1, 1, -1};
        for (Ball ball : myBalls) {
            for (int k=0; k<xMult.length; k++) {
                Ball newBall = new Ball(ball.getX(), ball.getY(),ball.getVelX() * xMult[k],ball.getVelY() * yMult[k]);
                this.addGameObjectToRoot(newBall);
                newBalls.add(newBall);
            }
        }
        myBalls.addAll(newBalls);
    }

    private void reflectBallsOffObstacles() {
        for (Ball ball : myBalls) {
            ball.reflectOffWall(this.getAssignedWidth(), this.getAssignedHeight());
            ball.reflectOffPaddle(myPaddle);
            ball.reflectOffBlock(myBlocks);
            myPaddle.activateIfPowerShot(this.getRoot(), ball);
        }
    }

    private ArrayList<Block> getBlocksHit() {
        HashSet<Block> blocksHit = new HashSet<>();
        for (Laser laser : myLasers) {
            Block blockHit = laser.getBlockHit(myBlocks);
            if (blockHit != null)
                blocksHit.add(blockHit);
        }
        for (Ball ball : myBalls) {
            Block blockHit = ball.getBlockHit(myBlocks);
            if (blockHit != null)
                blocksHit.add(blockHit);
        }
        blocksHit.removeAll(myIndestructibleBlocks); //FIX if hit by PowerShot
        return new ArrayList<>(blocksHit);
    }

    private void updateBlocksAndPowerups(ArrayList<Block> blocksHit) {
        for (Block block : blocksHit) {
            releasePowerup(block);
            safeDeleteBlock(block);
        }
    }

    private void releasePowerup(Block block) {
        for (Powerup powerup : myPowerups) {
            if (powerup.isWithinAlpha(block)) {
                powerup.setIsHidden(false);
                myFallingPowerups.add(powerup);
            }
        }
    }

    private void safeDeleteBlock(Block blockHit) {
        boolean blockStillAlive = blockHit.updateOnCollision();
        if (!blockStillAlive) {
            this.removeGameObjectFromRoot(blockHit);
            myBlocks.remove(blockHit);
        }
    }

    private void deleteLasers() {
        ArrayList<Laser> destroyedLasers = new ArrayList<>();
        for (Laser laser : myLasers) {
            if (laser.getBlockHit(myBlocks) != null) {
                destroyedLasers.add(laser);
                this.removeGameObjectFromRoot(laser);
            }
        }
        myLasers.removeAll(destroyedLasers);
    }

    private void deleteBallsOutOfBounds() {
        ArrayList<Ball> outOfBoundsBalls = new ArrayList<>();
        for (Ball ball : myBalls) {
            if (ball.didHitFloor(this.getAssignedHeight())) { //Should this be current height?
                this.removeGameObjectFromRoot(ball);
                outOfBoundsBalls.add(ball);
            }
        }
        myBalls.removeAll(outOfBoundsBalls);
    }

    /*
    ------------------------------------------------
                    Getter Methods
    ------------------------------------------------
     */

    /**
     * @return number of lives left
     */
    protected int getLives() { return myLives; }

    /**
     * @return list of Blocks in Level's initial configuration
     */
    protected ArrayList<Block> getBlockConfiguration() { return myBlockConfiguration; }

    /**
     * @return list of Powerups in Level's initial configuration
     */
    protected ArrayList<Powerup> getPowerupConfiguration() { return myPowerupConfiguration; }

    /**
     * @return Random seed assigned to this Level
     */
    protected Random getRand() { return myRand; }

    /**
     * @return list of Blocks in this level
     */
    protected ArrayList<Block> getBlocks() { return myBlocks; }

    /**
     * @return list of Powerups in this level
     */
    protected ArrayList<Powerup> getPowerups() { return myPowerups; }

    /**
     * @return list of Balls in this level
     */
    protected ArrayList<Ball> getBalls() { return myBalls; }

    /**
     * @return list of Lasers in this level
     */
    protected ArrayList<Laser> getLasers() { return myLasers; }

    /**
     * @return the Paddle in this level
     */
    protected Paddle getPaddle() { return myPaddle; }

    /**
     * @return number of destructible blocks left in the Level
     */
    protected int getBlocksLeft() { return myBlocks.size() - myIndestructibleBlocks.size(); }

    /**
     * @return true if Pause status is on
     */
    protected boolean getPauseStatus() {
        return myPauser.getIsPaused();
    }

    /**
     * @return StatusBar displayed for this level
     */
    protected StatusBar getStatusBar() { return myBar; }

    /*
    ------------------------------------------------
                    Setter Methods
    ------------------------------------------------
     */

    /**
     * Set number of lives left in Level
     * @param lives
     */
    protected void setLives(int lives) {
        myLives = lives;
        myBar.setLives(myLives);
        myBar.updateText();
    }

    /**
     * Pauses the game if it was playing, resumes it if it was paused
     */
    protected void togglePauseStatus() {
        myPauser.setIsPaused(!myPauser.getIsPaused());
    }
}
