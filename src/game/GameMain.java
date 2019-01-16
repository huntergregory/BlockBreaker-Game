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
    public static final double BLOCK_WIDTH = 45; // adjusted to this value for aesthetics
    public static final double BLOCK_HEIGHT = 15; // adjusted to this value for aesthetics
    public static final int MAX_LIVES = 3;

    private Scene myScene;
    private int myLives = MAX_LIVES;
    private int currentLevelNumber;
    private ArrayList<Level> myLevels;
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
        Group root = new Group();
        myScene = new Scene(root, width, height, backgroundColor);

        currentLevelNumber = 1; //FIX??
        createNewLevel();
        resetBallAndPaddle();
        addAllChildrenTo(root);

        return myScene;
    }

    //assumes currentLevelNumber was incremented and that there is a next level
    private void createNewLevel() {
        Level nextLevel = myLevels.get(currentLevelNumber);
        myBlocks = nextLevel.initialize();
    }

    private void resetBallAndPaddle() {
        //Erase all old balls and create new one
        myBalls = new ArrayList<>();
        Image image = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
        Ball ball = new Ball(image, 10,10);
        ball.setX(45);
        ball.setY(75);
        myBalls.add(ball);

        //reset paddle
        myPaddle = new Paddle();
    }

    private void addAllChildrenTo(Group root) {
        for (Block block : myBlocks) {
            root.getChildren().add(block);
        }

        //root.getChildren().add(myPaddle); //FIX

        for (Ball ball : myBalls) {
            root.getChildren().add(ball);
        }
    }

    // Change properties of shapes to animate them
    private void step (double elapsedTime) {
        //FIX
    }

    public static void main(String[] args) {
        launch(args);
    }
}