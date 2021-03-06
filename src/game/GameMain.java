package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

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
//////
//
// TO FIX:
//  ball bouncing before hits floor on bottom
//  side paddle reflection in general
//  side paddles overshooting top and bottom
//  lasers and aimers for all
//   increase height for BIG_Paddle for side paddles
//   reset lasers after life lost
//  add timers to powerups in general
//  this was never called in level for a side paddle: paddle.activateIfPowerShot
//////
public class GameMain extends Application {
    public static final String TITLE = "BlockBreaker";
    public static final int SIZE_HEIGHT = 700;
    public static final int SIZE_WIDTH = 700;
    public static final int FRAMES_PER_SECOND = 500; // less paddle lag at this number
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int MAX_LIVES = 3;

    public static final GameScene[] GAME_SCENES = { new CustomizationGameScene(SIZE_WIDTH, SIZE_HEIGHT),
                                                    new SplashGameScene(SIZE_WIDTH, SIZE_HEIGHT),
                                                    new LevelOne(SIZE_WIDTH, SIZE_HEIGHT),
                                                    new LevelOne(SIZE_WIDTH, SIZE_HEIGHT),
                                                    new LevelOne(SIZE_WIDTH, SIZE_HEIGHT) };
    public static final int SPLASH_SCENE = 1;
    public static final int CUSTOM_SCENE = 0;


    private Stage myStage;
    private int myNumScene;
    private int myLives;
    private int myScore;
    private boolean myIsPausedBetweenScene;
    private Paint myBackgroundColor; //ran out of time to implement these
    private String ballImageName;    //same as above. Would make a method that extracts them from CustomizationGameScene
                                        //and then passes these to every Level in getCurrentLevel().resetLevel();
                                            //i.e. new method signature resetLevel(String ballImageName, Paint sceneBackgroundColor)
    private LevelHandler myLevelHandler;

    /**
     * Initialize what will be displayed and how it will be updated.
     * Adapted from lab_bounce (authored by Robert Duvall).
     */
    @Override
    public void start (Stage stage) {
        myStage = stage;
        myLives = MAX_LIVES;
        myScore = 0;
        myLevelHandler = null;
        myIsPausedBetweenScene = false;
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
        if (currentGameSceneIsLevel())
            myScore = getCurrentLevel().getStatusBar().getScore();
        if (numScene >= GAME_SCENES.length || numScene < 0)
            myNumScene = SPLASH_SCENE;
        else {
            myNumScene = numScene;
            if (currentGameSceneIsLevel()) {
                getCurrentLevel().resetLevel();
                getCurrentLevel().getStatusBar().setLevelNumber(myNumScene - 1);
                getCurrentLevel().getStatusBar().setScore(myScore);
                getCurrentLevel().setLives(myLives);
                myLevelHandler = new LevelHandler(getCurrentLevel());
            }
            else {
                myLevelHandler = null;
                myScore = 0;
                //FIX, addEventListeners();
            }
        }
        myStage.setScene(getCurrentGameScene().getScene());
    }

    private void attachGameLoopAndPlay() {
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), event -> step(SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private void step (double elapsedTime) {
        if (myNumScene == SPLASH_SCENE) {
            myLives = MAX_LIVES;
            SplashGameScene splash = (SplashGameScene) getCurrentGameScene();
            if (splash.getShouldGoToLevels())
                switchToScene(myNumScene + 1);
            else if (splash.getShouldGoToCustom())
                switchToScene(CUSTOM_SCENE);
        }
        if (myNumScene == CUSTOM_SCENE) {
            CustomizationGameScene custom = (CustomizationGameScene) getCurrentGameScene();
            if (custom.getShouldGoToSplash())
                switchToScene(myNumScene + 1);
        }
        if (currentGameSceneIsLevel()) {
            if (getCurrentLevel().getPauser().getIsPaused())
                return;
            if (myLevelHandler.getShouldSkipToPrevious() && myNumScene > SPLASH_SCENE + 1) {
                switchToScene(myNumScene - 1);
            }
            if (getCurrentLevel().getBlocksLeft() == 0 || myLevelHandler.getShouldSkipToNext())
                switchToScene(myNumScene + 1);
            else {
                myLives = getCurrentLevel().getLives();
                if (myLives == 0)
                    switchToScene(SPLASH_SCENE);
                else
                    getCurrentLevel().step(elapsedTime);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}