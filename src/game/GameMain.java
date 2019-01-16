package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 A variant of the classic game Breakout, inspired by the original and its
 descendant - Brick Breaker.
 @author Hunter Gregory
 */
public class GameMain extends Application {
    public static final String TITLE = "BlockBreaker";
    public static final int SIZE = 400;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.AZURE; //FIX to make customizable
    public static final String BALL_IMAGE = "ball.gif"; //FIX to make customizable
    public static final int MAX_LIVES = 3;
    private Level[] myLevels = { new LevelOne() };

    private Scene myScene;
    private Group myRoot;
    private int myLives = MAX_LIVES;
    private int currentLevelNumber; // 0 based
    private ArrayList<Block> myBlocks;
    private ArrayList<Ball> myBalls;
    private Paddle myPaddle;
    private int blocksLeft; // FIX might not need this

    /**
     * Initialize what will be displayed and how it will be updated.
     * Adapted from lab_bounce (authored by Robert Duvall).
     */
    @Override
    public void start (Stage stage) {
        // attach scene to the stage and display it
        myScene = setupGame(SIZE, SIZE, BACKGROUND);
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();

        // attach "game loop" to timeline to play it
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), event -> step(SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private Scene setupGame(int width, int height, Paint backgroundColor) {
        myRoot = new Group();
        myScene = new Scene(myRoot, width, height, backgroundColor);
        currentLevelNumber = 0; //FIX??
        createNewLevel();
        resetBallAndPaddle();
        addAllChildrenTo();

        return myScene;
    }

    //assumes currentLevelNumber was incremented and that there is a next level
    private void createNewLevel() {
        Level nextLevel = myLevels[currentLevelNumber];
        myBlocks = nextLevel.initialize(myScene.getWidth(), myScene.getHeight());
    }

    private void resetBallAndPaddle() {
        //Erase all old balls and create new one
        myBalls = new ArrayList<>();
        Image image = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
        Ball ball = new Ball(image, 60,-60); // FIX
        ball.setX(100);                                  // FIX
        ball.setY(350);                                  // FIX
        myBalls.add(ball);

        //reset paddle
        myPaddle = new Paddle();
    }

    private void addAllChildrenTo() {
        for (Block block : myBlocks) {
            myRoot.getChildren().add(block);
        }

        //myRoot.getChildren().add(myPaddle); //FIX

        for (Ball ball : myBalls) {
            myRoot.getChildren().add(ball);
        }
    }

    // Change properties of shapes to animate them
    /*
    FIX THESE:
        2. consider when ball hits block from left and right (only works now if it hits bottom or top)
     */
    private void step (double elapsedTime) {
        moveBalls(elapsedTime);
        updateBallsOnWallCollision();
        updateAllOnBlockCollision();
    }

    private void moveBalls(double elapsedTime) {
        for (Ball ball : myBalls) {
            ball.setX(ball.getX() + ball.getVelX() * elapsedTime);
            ball.setY(ball.getY() + ball.getVelY() * elapsedTime);
        }
    }

    //FIX ball bounces off bottom
    private void updateBallsOnWallCollision() {
        for (Ball ball : myBalls) {
            if (ball.getY() <= 0 || ball.getY() + ball.getBoundsInParent().getHeight() >= myScene.getHeight()) //DELETE second part, see below
                ball.setVelY(ball.getVelY() * -1);

            if (ball.getX() <= 0 || ball.getX() + ball.getBoundsInParent().getWidth() >= myScene.getWidth())
                ball.setVelX(ball.getVelX() * -1);

            if (ball.getY() + ball.getBoundsInParent().getHeight() >= myScene.getHeight() &&
                        myBalls.size() == 1)
                //endCurrentLife(); //FIX
                continue;
        }
    }

    private void updateAllOnBlockCollision() {
        for (Ball ball : myBalls) {
            for (Block block : myBlocks) {
                if (block.getBoundsInParent().intersects(ball.getBoundsInParent())) {
                    reflectBallOffBlock(ball, block);
                    updateBlock(block);
                }
            }
        }
    }

    private void updateBlock(Block block) {
        boolean blockIsDestroyed = block.updateOnCollision();
        if (blockIsDestroyed) {
            myRoot.getChildren().remove(block);
            myBalls.remove(block);
        }
    }

    private void reflectBallOffBlock(Ball ball, Block block) {
        double multiplier = block.getMultiplier();
        double newVelX = Math.round(multiplier * ball.getVelX());
        double newVelY = Math.round(multiplier * ball.getVelY());
        if (true) //FIX to vertical
            newVelY *= -1;
        else
            newVelX *= -1;

        ball.setVelX((int) newVelX);
        ball.setVelY((int) newVelY);
    }

    private boolean verticalCollision(Ball ball, Block block) {
        return ball.getX() + ball.getBoundsInParent().getWidth() >= block.getX() ||
                ball.getX() <= block.getX() + block.getBoundsInParent().getWidth();
    }


    //FIX
    private void endCurrentLife() {
        myLives -= 1;
        if (myLives == 0)
            endGame();
        else
            continueWithNewLife();
    }

    //FIX
    private void continueWithNewLife() {
        //??????
    }

    //FIX
    private void endGame() {
        //???????
    }

    public static void main(String[] args) {
        launch(args);
    }
}