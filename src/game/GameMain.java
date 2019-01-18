package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.lang.reflect.Array;
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
    public static final Level[] LEVELS = { new LevelOne(), new LevelOne() };


    private Stage myStage;
    private SceneManager mySceneManager;
    private int myLives = MAX_LIVES;
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
        myStage = stage;
        mySceneManager = new SceneManager(LEVELS, SIZE, SIZE);
        addLevelComponents();
        myStage.setScene(mySceneManager.getSplashScreen());
        myStage.setTitle(TITLE);
        myStage.show();

        // attach "game loop" to timeline to play it
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), event -> step(SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private void addLevelComponents() {
        myBlocks = mySceneManager.getCurrentLevelBlocks();
        resetBallAndPaddle();
        addAllChildrenTo();
    }

    private void resetBallAndPaddle() {
        //Erase all old balls and create new one
        myBalls = new ArrayList<>();
        Image image = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
        Ball ball = new Ball(image, 0,0); // FIX
        ball.setX(100);                                  // FIX
        ball.setY(350);                                  // FIX
        myBalls.add(ball);

        //reset paddle
        myPaddle = new Paddle();
    }

    private void addAllChildrenTo() {
        Group currentRoot = mySceneManager.getCurrentRoot();
        for (Block block : myBlocks) {
            currentRoot.getChildren().add(block.getImageView());
        }

        //currentRoot.getChildren().add(myPaddle); //FIX

        for (Ball ball : myBalls) {
            currentRoot.getChildren().add(ball.getImageView());
        }
    }

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

    private void updateBallsOnWallCollision() {
        for (Ball ball : myBalls) {
            double sceneWidth = mySceneManager.getCurrentScene().getWidth();
            if (ball.getX() <= 0 || ball.getX() + ball.WIDTH >= sceneWidth)
                ball.setVelX(ball.getVelX() * -1);

            double sceneHeight = mySceneManager.getCurrentScene().getHeight();
            if (ball.getY() <= 0 || ball.getY() + ball.HEIGHT >= sceneHeight)
                ball.setVelY(ball.getVelY() * -1);
        }
    }

    private void updateAllOnBlockCollision() {
        for (Ball ball : myBalls) {
            for (Block block : myBlocks) {
                if (block.getImageView().getBoundsInParent().intersects(ball.getImageView().getBoundsInParent())) {
                    reflectBallOffBlock(ball, block);
                    deleteBlockIfNecessary(block);
                    break; // out of block loop //FIX?
                }
            }
        }
    }

    private void reflectBallOffBlock(Ball ball, Block block) {
        double multiplier = block.getMultiplier();
        double newVelX = Math.round(multiplier * ball.getVelX());
        double newVelY = Math.round(multiplier * ball.getVelY());

        if (isVerticalCollision(ball, block))
            newVelY *= -1;
        else
            newVelX *= -1;

        ball.setVelX((int) newVelX); //DEBUGGING, FIX
        ball.setVelY((int) newVelY); //DEBUGGING, FIX
    }

    private boolean isVerticalCollision(Ball ball, Block block) {
        double ballCenterX = ball.getX() + ball.WIDTH / 2;
        return block.getX() < ballCenterX && ballCenterX < block.getX() + block.WIDTH;
    }

    private void deleteBlockIfNecessary(Block block) {
        boolean blockIsDestroyed = block.updateOnCollision();
        if (blockIsDestroyed) {
            mySceneManager.getCurrentRoot().getChildren().remove(block.getImageView());
            myBlocks.remove(block);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}