package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
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
    public static final String BOUNCER_IMAGE = "ball.gif"; //FIX to make customizable

    private Scene myScene;
    private Ball[] myBalls;
    private int myLives;
    private Paddle myPaddle;
    private ArrayList<Block> myBlocks;
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
/*
        // attach "game loop" to timeline to play it
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), event -> step(SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play(); */
    }

    private Scene setupGame(int width, int height, Paint backgroundColor) {
        Group root = new Group();
        myScene = new Scene(root, width, height, backgroundColor);
        
        createLevel();
        for (Block b : myBlocks) {
            root.getChildren().add(b);
        }

        return myScene;
    }

    private void createLevel() {
        myBlocks = new ArrayList<>();
        Block b = new Block(Block.BLOCK_TYPE.ALPHA);
        b.setPosition(10, 10);
        myBlocks.add(b);
        b = new Block(Block.BLOCK_TYPE.BETA);
        b.setPosition(100, 100);
        myBlocks.add(b);
    }

    // Change properties of shapes to animate them
    private void step (double elapsedTime) {

    }

    public static void main(String[] args) {
        launch(args);
    }
}