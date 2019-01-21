package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 A variant of the classic game Breakout, inspired by the original and its
 descendant - Brick Breaker.
 Stores all GameScenes, keeps track of current GameScene.
 Delegates management of levels to two entities:
    1. LevelHandler, which takes care of keyboard and mouse input.
    2. the Level abstract class, which manages all GameObjects within its GameScene.
 GameMain is in charge of running the Application, resetting Levels, and switching between all GameScenes.

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

    public static final GameScene[] GAME_SCENES = { new LevelOne(SIZE_WIDTH, SIZE_HEIGHT) , new LevelOne(SIZE_WIDTH, SIZE_HEIGHT), new LevelOne(SIZE_WIDTH, SIZE_HEIGHT)  };
    public static final int SPLASH_SCENE = 1;
    public static final int CUSTOM_SCENE = 0;


    private Stage myStage;
    private int myNumScene;
    private int myLives;
    private LevelHandler myLevelHandler;

    /**
     * Initialize what will be displayed and how it will be updated.
     * Adapted from lab_bounce (authored by Robert Duvall).
     */
    @Override
    public void start (Stage stage) {
        myStage = stage;
        myLives = MAX_LIVES;
        myLevelHandler = null;
        switchToScene(SPLASH_SCENE);

        myStage.setScene(getCurrentGameScene().getScene());
        myStage.setTitle(TITLE);
        myStage.show();

        attachGameLoopAndPlay();
    }

    private GameScene getCurrentGameScene() {
        return GAME_SCENES[myNumScene];
    }

    private boolean currentGameSceneIsLevel() {
        return getCurrentGameScene() instanceof Level;
    }

    private Level getCurrentLevel() {
        return (Level) getCurrentGameScene();
    }

    private void switchToScene(int numScene) {
        if (numScene >= GAME_SCENES.length || numScene < 0)
            myNumScene = SPLASH_SCENE;
        else {
            myNumScene = numScene;
            if (currentGameSceneIsLevel()) {
                getCurrentLevel().resetLevel();
                getCurrentLevel().setLives(myLives);
                myLevelHandler = new LevelHandler(getCurrentLevel());
            }
            else {
                myLevelHandler = null;
                //FIX, addEventListeners();
            }
        }
    }

    private void attachGameLoopAndPlay() {
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), event -> step(SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    //FIX, REFACTOR, REDUCE SIMILARITY
    private void step (double elapsedTime) {
        if (currentGameSceneIsLevel())
            if (getCurrentLevel().getBlocksLeft() == 0)
                switchToScene(myNumScene + 1);
            else {
                myLives = getCurrentLevel().getLives();
                if (myLives == 0)
                    endGame();
                else
                    getCurrentLevel().step(elapsedTime);
            }
        else {
            return; //FIX
        }
    }

    private void endGame() {
        //FIX, need to stop LevelHandler if GAME IS OVER
        //FIX, add more
    }

    public static void main(String[] args) {
        launch(args);
    }
}