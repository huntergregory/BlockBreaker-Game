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

import java.lang.reflect.Array;
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
    public static final int SIZE_HEIGHT = 500;
    public static final int SIZE_WIDTH = 400;
    public static final int FRAMES_PER_SECOND = 300; // less paddle lag at this number
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int PADDLE_SPEED = 20;
    public static final int MAX_LIVES = 3;
    private Paint myBackgroundColor = Color.TOMATO; //AQUA //WHITE
    public static final GameScene[] GAME_SCENES = { new LevelOne(SIZE_WIDTH, SIZE_HEIGHT) , new LevelOne(SIZE_WIDTH, SIZE_HEIGHT) };
    public static final int SPLASH_SCENE = 1;
    public static final int CUSTOM_SCENE = 0;
    public static final int PAUSE_RECT_WIDTH = 30;
    public static final int PAUSE_RECT_HEIGHT = 80;
    public static final Rectangle PAUSE_RECT_1 = new Rectangle(SIZE_WIDTH / 2 - 3 * PAUSE_RECT_WIDTH / 2,
                                                            SIZE_HEIGHT / 2 - PAUSE_RECT_HEIGHT,
                                                                PAUSE_RECT_WIDTH, PAUSE_RECT_HEIGHT);
    public static final Rectangle PAUSE_RECT_2 = new Rectangle(SIZE_WIDTH / 2 + PAUSE_RECT_WIDTH / 2,
                                                            SIZE_HEIGHT / 2 - PAUSE_RECT_HEIGHT,
                                                                PAUSE_RECT_WIDTH, PAUSE_RECT_HEIGHT);


    private Stage myStage;
    private int myNumScene;
    private int myLives = MAX_LIVES;
    private ArrayList<Block> myBlocks = new ArrayList<>();
    private ArrayList<Powerup> myPowerups = new ArrayList<>();
    private ArrayList<Powerup> myFallingPowerups = new ArrayList<>();
    private ArrayList<Ball> myBalls = new ArrayList<>();
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
        switchToScene(SPLASH_SCENE);
        myStage.setScene(GAME_SCENES[myNumScene].getScene());
        myStage.setTitle(TITLE);
        myStage.show();

        attachGameLoopAndPlay();
    }

    private void switchToScene(int numScene) {
        myNumScene = numScene;
        if (GAME_SCENES[myNumScene] instanceof Level) {
            assignLevelComponents();
            addEventListeners();
        }
        //else //FIX
    }

    private void assignLevelComponents() {
        Level currentLevel = (Level) GAME_SCENES[myNumScene];
        myBlocks = currentLevel.getBlocks();
        myPowerups = currentLevel.getPowerups();
        myBalls = currentLevel.resetAndGetBalls();
        myPaddle = currentLevel.resetAndGetPaddle();
    }

    private void addEventListeners() {
        Scene currentScene = GAME_SCENES[myNumScene].getScene();
        currentScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        currentScene.setOnMouseClicked(e -> handleMouseClick());
    }

    private void handleKeyInput(KeyCode code) {
        if (myGameIsOver || myGameIsPaused)
            return;
        handleMovingPaddle(code);
        handleCheatCodes(code);
    }

    private void handleMovingPaddle(KeyCode code) {
        if (myGameIsOver)
            return;
        if (code == KeyCode.RIGHT && myPaddle.getX() + myPaddle.getWidth() < SIZE_WIDTH) {
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
        if (myGameIsPaused) {
            GAME_SCENES[myNumScene].addNodeToRoot(PAUSE_RECT_1);
            GAME_SCENES[myNumScene].addNodeToRoot(PAUSE_RECT_2);
        }
        else {
            GAME_SCENES[myNumScene].removeNodeFromRoot(PAUSE_RECT_1);
            GAME_SCENES[myNumScene].removeNodeFromRoot(PAUSE_RECT_2);
        }
    }

    private void attachGameLoopAndPlay() {
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), event -> step(SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private void step (double elapsedTime) {
        if (myGameIsPaused || myGameIsOver)
            return;
        moveObjects(elapsedTime);
        catchPowerups();
        updateBallsOnWallCollision();
        updateBallsOnPaddleCollision();
        updateAllOnBlockCollision();
        myGameIsOver = !oneBallInBounds();
    }

    private void moveObjects(double elapsedTime) {
        for (Ball ball : myBalls) {
            ball.updatePosition(elapsedTime);
        }
        for (Powerup powerup : myFallingPowerups) {
            powerup.updatePosition(elapsedTime);
        }
    }

    private void catchPowerups() {
        //delete powerups and remove from falling powerups
        //set boolean to eventually set ball velocity to 0 if power_shot
    }

    private void updateBallsOnWallCollision() {
        for (Ball ball : myBalls) {
            if (ball.getY() <= 0 || ball.getY() + ball.getHeight() >= GAME_SCENES[myNumScene].getCurrentHeight())
                ball.multiplyVelY(-1);

            if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= GAME_SCENES[myNumScene].getCurrentWidth())
                ball.multiplyVelX(-1);
        }
    }

    private void updateBallsOnPaddleCollision() {
        for (Ball ball : myBalls) {
            if (myPaddle.getParentBounds().intersects(ball.getParentBounds())) {
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
                if (block.getParentBounds().intersects(ball.getParentBounds())) {
                    reflectBallOffBlock(ball, block);
                    releasePowerupIfNecessary(block);
                    deleteBlockIfNecessary(block);
                    break;
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

    //FIX
    private boolean isVerticalCollision(Ball ball, Block block) {
        double ballCenterX = ball.getX() + ball.WIDTH / 2;
        return block.getX() < ballCenterX && ballCenterX < block.getX() + block.getWidth();
    }

    private void releasePowerupIfNecessary(Block block) {
        for (Powerup powerup : myPowerups) {
            if (powerup.isWithin(block)) {
                powerup.setIsHidden(false);
                myFallingPowerups.add(powerup);
            }
        }
    }

    private void deleteBlockIfNecessary(Block block) {
        boolean blockIsDestroyed = block.updateOnCollision();
        if (blockIsDestroyed) {
            GAME_SCENES[myNumScene].removeNodeFromRoot(block.getImageView());
            myBlocks.remove(block);
        }
    }

    private boolean oneBallInBounds() {
        for (Ball ball : myBalls) {
            if (ball.getY() + Ball.HEIGHT >= GAME_SCENES[myNumScene].getCurrentHeight()) {
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
        GAME_SCENES[myNumScene].removeNodeFromRoot(ball.getImageView());
        myBalls.remove(ball);
    }

    public static void main(String[] args) {
        launch(args);
    }
}