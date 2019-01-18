package game;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

/**
 * Constructs and stores all scenes needed for a Block Breaker game.
 * @author Hunter Gregory
 */
public class SceneStorage {
    private static final int CUSTOMIZATION = 0;
    private static final int SPLASH = 1;

    private int myScreenWidth;
    private int myScreenHeight;
    private Level[] myLevels;
    private Scene[] myScenes;
    private int myCurrentNumScene;
    private Group myCurrentRoot;

    /**
     * Levels must be in this order: Customization, Splash, LevelOne, LevelTwo, ...
     * @param levels
     */
    public SceneStorage(Level[] levels, int width, int height) {
        myLevels = levels;
        myCurrentNumScene = 0;
        myScreenWidth = width;
        myScreenHeight = height;

        myScenes = new Scene[levels.length];
        for (int num=0; num<myScenes.length; num++) {
            myScenes[num] = setupScene(num);
        }
    }

    public Scene getNextScene() {
        myCurrentNumScene += 1;
        return myScenes[myCurrentNumScene];
    }

    /**
     * Use to return to the splash screen after the game is over or the player has won
     * @return splash screen Scene
     */
    public Scene goToSplashScreen() {
        return myScenes[SPLASH];
    }

    private Scene goToCustomization() {
        return myScenes[CUSTOMIZATION];
    }

    private Scene setupScene(Paint backgroundColor) {
        myCurrentRoot = new Group();
        Scene scene = new Scene(myCurrentRoot, width, height, backgroundColor);
        createNewLevel();
        resetBallAndPaddle();
        addAllChildrenTo();

        return myScene;
    }

    private ArrayList<Block> createNewLevel() {
        Level nextLevel = myLevels[myCurrentNumScene];
        return nextLevel.initialize(myScreenWidth, myScreenHeight);
    }
}
