package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

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
        myStage.setScene(getCurrentGameScene().getScene());
        myStage.setTitle(TITLE);
        myStage.show();

        attachGameLoopAndPlay();
    }

    private void switchToScene(int numScene) {
        myNumScene = numScene;
        if (getCurrentGameScene() instanceof Level) {
            assignLevelComponents();
            addEventListeners();
        }
        //else //FIX
    }

    private GameScene getCurrentGameScene() {
        return GAME_SCENES[myNumScene];
    }

    private void assignLevelComponents() {
        Level currentLevel = (Level) getCurrentGameScene();
        myBlocks = currentLevel.getBlocks();
        myPowerups = currentLevel.getPowerups();
        myBalls = currentLevel.resetAndGetBalls();
        myPaddle = currentLevel.resetAndGetPaddle();
    }

    private void addEventListeners() {
        Scene currentScene = getCurrentGameScene().getScene();
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
        if (code == KeyCode.RIGHT && myPaddle.getX() + myPaddle.getWidth() < SIZE_WIDTH)
            myPaddle.setX(myPaddle.getX() + Paddle.SPEED); //currently overshoots boundary a little due to large SPEED
        else if (code == KeyCode.LEFT && myPaddle.getX() > 0)
            myPaddle.setX(myPaddle.getX() - Paddle.SPEED); //same as above
    }

    private void handleCheatCodes(KeyCode code) {
        if (myGameIsOver)
            return;
        if (code == KeyCode.S)
            splitBalls();
        if (code == KeyCode.B)
            makeBigPaddle();
    }

    private void handleMouseClick() {
        if (myGameIsOver)
            return;

        myGameIsPaused = !myGameIsPaused;
        if (myGameIsPaused) {
            getCurrentGameScene().addNodeToRoot(PAUSE_RECT_1);
            getCurrentGameScene().addNodeToRoot(PAUSE_RECT_2);
        }
        else {
            getCurrentGameScene().removeNodeFromRoot(PAUSE_RECT_1);
            getCurrentGameScene().removeNodeFromRoot(PAUSE_RECT_2);
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
        if (myGameIsPaused)
            return;
        if (myGameIsOver) {
            //FIX
        }
        else {
            catchPowerups();

            for (Powerup powerup : myFallingPowerups) {
                powerup.updatePosition(elapsedTime);
            }

            for (Ball ball : myBalls) {
                ball.updatePosition(elapsedTime);
                Block blockHit = ball.reflectOffAnyObstacles((Level) getCurrentGameScene(), myPaddle, myBlocks);
                updateBlockAndFallingPowerups(blockHit);
            }
            deleteBallsOutOfBounds();

            if (myBlocks.size() == 0)
                switchToScene(myNumScene + 1);
            if (myBalls.size() == 0)
                loseLife();
        }
    }

    //FIX
    private void catchPowerups() {
        ArrayList<Powerup> caughtPowerups = new ArrayList<>();
        for (Powerup powerup : myFallingPowerups) {
            if (!powerup.hitGameObject(myPaddle))
                continue;

            caughtPowerups.add(powerup);
            getCurrentGameScene().removeNodeFromRoot(powerup.getImageView());
            switch (powerup.getType()) {
                case LASER: {
                    break;//FIX
                }
                case SPLIT: {
                    splitBalls();
                    break; //FIX
                }
                case BIG_PADDLE: {
                    makeBigPaddle();
                }
                case POWER_SHOT:{
                    break; //FIX
                }
            }
        }
        myFallingPowerups.removeAll(caughtPowerups);
        myPowerups.removeAll(caughtPowerups);
        //set boolean to eventually set ball velocity to 0 if power_shot
        //check to see if a power up fell way past the screen
    }

    private void splitBalls() {
        ArrayList<Ball> newBalls = new ArrayList<>();
        int[] xMult = {-1, -1, 1};
        int[] yMult = {-1, 1, -1};
        for (Ball ball : myBalls) {
            for (int k=0; k<xMult.length; k++) {
                Ball newBall = new Ball((int) ball.getX(),
                                            (int) ball.getY(),
                                       ball.getVelX() * xMult[k],
                                       ball.getVelY() * yMult[k]);
                getCurrentGameScene().addNodeToRoot(newBall.getImageView());
                newBalls.add(newBall);
            }
        }
        myBalls.addAll(newBalls);
    }

    private void makeBigPaddle() {
        myPaddle.setWidth(myPaddle.getWidth() + myPaddle.DEFAULT_WIDTH / 2); // FIX make a timer
    }

    private void updateBlockAndFallingPowerups(Block blockHit) {
        if (blockHit == null)
            return;
        releasePowerup(blockHit);
        boolean blockStillAlive = blockHit.updateOnCollision();
        if (!blockStillAlive) {
            getCurrentGameScene().removeNodeFromRoot(blockHit.getImageView());
            myBlocks.remove(blockHit);
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

    private void deleteBallsOutOfBounds() {
        ArrayList<Ball> outOfBoundsBalls = new ArrayList<>();
        for (Ball ball : myBalls) {
            if (ball.hitFloor(getCurrentGameScene())) {
                if (myBalls.size() == 1)
                    ball.halt();
                else
                    getCurrentGameScene().removeNodeFromRoot(ball.getImageView());

                outOfBoundsBalls.add(ball);
            }
        }
        myBalls.removeAll(outOfBoundsBalls);
    }

    private void loseLife() {
        myLives -= 1;
        if (myLives == 0)
            myGameIsOver = true;
    }

    public static void main(String[] args) {
        launch(args);
    }
}