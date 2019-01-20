package game;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Random;

/**
 * Creates a template for any level in this game.
 * @author Hunter Gregory
 */
public abstract class Level extends GameScene {
    public static final int SEPARATION_DISTANCE = 5;
    public static final int NUM_POWERUPS = 5;

    ArrayList<Block> myBlockConfiguration;
    ArrayList<Powerup> myPowerupConfiguration;
    Random myRand;
    Pauser myPauser;


    public Level(int width, int height) {
        this(width, height, Color.WHITE, new Random());
    }

    public Level(int width, int height, Paint backgroundColor, Random rand) {
        super(width, height, backgroundColor);
        myRand = rand;
        myBlockConfiguration = new ArrayList<>();
        myPowerupConfiguration = new ArrayList<>();
        myPauser = new Pauser(myAssignedWidth, myAssignedHeight, myRoot);
    }

    /**
     * @return list of Blocks in this level
     */
    protected ArrayList<Block> getBlocks() { return myBlockConfiguration; }

    /**
     * @return list of Powerups in this level
     */
    protected ArrayList<Powerup> getPowerups() { return myPowerupConfiguration; }

    /**
     * Call to initializeAndGetBlocks scene or restart after lost life.
     * @return list of one Ball
     */
    protected ArrayList<Ball> resetAndGetBalls() {
        ArrayList<Ball> list = new ArrayList<>();
        Ball ball = new Ball(100, 350, 60, -65); // FIX magic numbers
        list.add(ball);
        addGameObjectToRoot(ball);
        return list;
    }

    /**
     * Call to initializeAndGetBlocks scene or restart after lost life.
     * @return Paddle
     */
    protected Paddle resetAndGetPaddle() {
        Paddle paddle = new Paddle(myAssignedWidth - myAssignedWidth / 2 - Paddle.DEFAULT_WIDTH / 2,
                myAssignedHeight - Paddle.HEIGHT - 2);
        addGameObjectToRoot(paddle);
        return paddle;
    }

    /**
     * @return Pauser for the Level's Scene
     */
    protected Pauser getPauser() { return myPauser; }

    /**
     * Override in subclass if Level uses indestructible blocks
     * @return numBlocks
     */
    protected int getNumIndestructibleBlocks() { return 0; }
}
