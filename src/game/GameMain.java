package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 A variant of the classic game Breakout, inspired by the original and its
 descendant - Brick Breaker.
 This class has large dependencies on SceneManager which will do the following for GameMain:
  - store all scenes, levels, and roots
  - initiate and manage the splash scene and customization scene entirely
  - switch between scenes and keep track of current scene and root
  - reset and return balls, paddles, and blocks
 GameMain must manage all ball, paddle, block updates as well as user input within levels.
 @author Hunter Gregory
 */
public class GameMain extends Application {
    public static final String TITLE = "BlockBreaker";
    public static final int SIZE = 400;
    public static final int FRAMES_PER_SECOND = 300; // less paddle lag at this number
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int PADDLE_SPEED = 20;
    public static final int MAX_LIVES = 3;
    public static final Level[] LEVELS = { new LevelOne(), new LevelOne() };
    public static final int PAUSE_RECT_WIDTH = 30;
    public static final int PAUSE_RECT_HEIGHT = 80;
    public static final Rectangle PAUSE_RECT_1 = new Rectangle(SIZE / 2 - 3 * PAUSE_RECT_WIDTH / 2,
                                                            SIZE / 2 - PAUSE_RECT_HEIGHT,
                                                                PAUSE_RECT_WIDTH, PAUSE_RECT_HEIGHT);
    public static final Rectangle PAUSE_RECT_2 = new Rectangle(SIZE / 2 + PAUSE_RECT_WIDTH / 2,
                                                            SIZE / 2 - PAUSE_RECT_HEIGHT,
                                                                PAUSE_RECT_WIDTH, PAUSE_RECT_HEIGHT);


    private Stage myStage;
    private SceneManager mySceneManager;
    private int myLives = MAX_LIVES;
    private ArrayList<Block> myBlocks;
    private ArrayList<Ball> myBalls;
    private Paddle myPaddle;
    private int blocksLeft; // FIX might not need this
    private boolean myGameIsPaused;
    private boolean myGameIsOver;

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
        myBlocks = mySceneManager.getNewLevelBlocks();
        myBalls = mySceneManager.resetAndGetBalls();
        myPaddle = mySceneManager.resetAndGetPaddle();
        addEventListeners();
    }

    private void addEventListeners() {
        Scene scene = mySceneManager.getCurrentScene();
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        scene.setOnMouseClicked(e -> handleMouseClick());
    }

    private void handleKeyInput(KeyCode code) {
        handleMovingPaddle(code);
        handleCheatCodes(code);
    }

    private void handleMovingPaddle(KeyCode code) {
        if (myGameIsOver)
            return;
        if (code == KeyCode.RIGHT && myPaddle.getX() + myPaddle.getWidth() < SIZE) {
            myPaddle.setX(myPaddle.getX() + PADDLE_SPEED); //currently overshoots boundary a little due to large PADDLE_SPEED
        }
        else if (code == KeyCode.LEFT && myPaddle.getX() > 0) {
            myPaddle.setX(myPaddle.getX() - PADDLE_SPEED); //same as above
        }
    }

    private void handleCheatCodes(KeyCode code) {
        //FIX
    }

    private void handleMouseClick() {
        if (myGameIsOver)
            return;
        myGameIsPaused = !myGameIsPaused;
        ObservableList<Node> rootChildren = mySceneManager.getCurrentRoot().getChildren();
        if (myGameIsPaused) {
            rootChildren.add(PAUSE_RECT_1);
            rootChildren.add(PAUSE_RECT_2);
        }
        else {
            rootChildren.remove(PAUSE_RECT_1);
            rootChildren.remove(PAUSE_RECT_2);
        }
    }

    private void step (double elapsedTime) {
        if (myGameIsPaused || myGameIsOver)
            return;

        moveBalls(elapsedTime);
        updateBallsOnWallCollision();
        updateBallsOnPaddleCollision();
        updateAllOnBlockCollision();
        myGameIsOver = !oneBallInBounds();
    }

    private void moveBalls(double elapsedTime) {
        for (Ball ball : myBalls) {
            ball.updatePosition(elapsedTime);
        }
    }

    private void updateBallsOnWallCollision() {
        for (Ball ball : myBalls) {
            double sceneHeight = mySceneManager.getCurrentScene().getHeight();
            if (ball.getY() <= 0 || ball.getY() + ball.HEIGHT >= sceneHeight)
                ball.multiplyVelY(-1);

                double sceneWidth = mySceneManager.getCurrentScene().getWidth();
            if (ball.getX() <= 0 || ball.getX() + ball.WIDTH >= sceneWidth)
                ball.multiplyVelX(-1);
        }
    }

    private void updateBallsOnPaddleCollision() {
        for (Ball ball : myBalls) {
            if (myPaddle.getRect().getBoundsInParent().intersects(ball.getImageView().getBoundsInParent())) {
                //FIX consider the holding the ball powerup
                reflectBallOffPaddle(ball);
            }
        }
    }

    private void reflectBallOffPaddle(Ball ball) {
        int thirdOfWidth = (int) (myPaddle.getWidth() / 3);
        boolean hitLeftThird = ball.getX() < myPaddle.getX() + thirdOfWidth;
        boolean hitRightThird = ball.getX() > myPaddle.getX() + 2 * thirdOfWidth;
        double multiplier = (hitLeftThird || hitRightThird) ? 1.5 : 0.75;

        ball.multiplyVelY(-1 * multiplier);


        if (hitLeftThird && ball.getVelX() > 0 || hitRightThird && ball.getVelX() <= 0)
            multiplier *= -1;
        ball.multiplyVelX(multiplier);
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
        boolean vertical = isVerticalCollision(ball, block);
        ball.multiplyVelX(vertical ? multiplier : -1 * multiplier);
        ball.multiplyVelY(vertical ? -1 * multiplier : multiplier);
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

    private boolean oneBallInBounds() {
        for (Ball ball : myBalls) {
            if (ball.getY() + Ball.HEIGHT >= mySceneManager.getCurrentScene().getHeight()) {
                if (myBalls.size() == 1) {
                    ball.multiplyVelY(0); //freeze the ball if its the last one
                    ball.multiplyVelX(0);
                    return false;
                }
                else {
                    deleteBall(ball);
                    break;
                }
            }
        }
        return true;
    }

    private void deleteBall(Ball ball) {
        mySceneManager.getCurrentRoot().getChildren().remove(ball.getImageView());
        myBalls.remove(ball);
    }

    public static void main(String[] args) {
        launch(args);
    }
}