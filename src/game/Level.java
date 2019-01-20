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


    public Level(int width, int height) {
        this(width, height, Color.WHITE, new Random());
    }

    public Level(int width, int height, Paint backgroundColor, Random rand) {
        super(width, height, backgroundColor);
        myRand = rand;
        myBlockConfiguration = new ArrayList<>();
        myPowerupConfiguration = new ArrayList<>();
    }

    /**
     * @return list of Blocks in this level
     */
    public ArrayList<Block> getBlocks() { return myBlockConfiguration; }

    /**
     * @return list of Powerups in this level
     */
    public ArrayList<Powerup> getPowerups() { return myPowerupConfiguration; }

    /**
     * Call to initializeAndGetBlocks scene or restart after lost life.
     * @return list of one Ball
     */
    public ArrayList<Ball> resetAndGetBalls() {
        ArrayList<Ball> list = new ArrayList<>();
        Ball ball = new Ball(100, 350, 60, -65); // FIX magic numbers
        list.add(ball);
        addNodeToRoot(ball.getImageView());
        return list;
    }

    /**
     * Call to initializeAndGetBlocks scene or restart after lost life.
     * @return Paddle
     */
    public Paddle resetAndGetPaddle() {
        Paddle paddle = new Paddle(myAssignedWidth - myAssignedWidth / 2 - Paddle.DEFAULT_WIDTH / 2,
                myAssignedHeight - Paddle.HEIGHT - 2);
        addNodeToRoot(paddle.getImageView());
        return paddle;
    }
}
