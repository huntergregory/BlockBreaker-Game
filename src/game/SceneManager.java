package game;

import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.paint.Paint;

import java.util.ArrayList;


/**
 * Constructs and stores all scenes needed for a Block Breaker game.
 * Sets up splash and customization scene to be managed autonomously.
 * @author Hunter Gregory
 */
public class SceneManager {
    public static final int CUSTOMIZATION = 0;
    public static final int SPLASH = 1;

    private Paint myBackgroundColor;
    private int mySceneWidth;
    private int mySceneHeight;
    private Level[] myLevels;
    private Scene[] myScenes;
    private Group[] mySceneRoots;
    private int myCurrentNumScene;

    /**
     * Creates a SceneManager from an ordered array of levels
     * @param levels
     * @param width of all scenes
     * @param height of all scenes
     */
    public SceneManager(Level[] levels, int width, int height) {
        myLevels = levels;
        myCurrentNumScene = 0;
        mySceneWidth = width;
        mySceneHeight = height;

        myScenes = new Scene[levels.length + 2];
        mySceneRoots = new Group[levels.length + 2];
        addSplashSceneAndRoot();
        addCustomizationSceneAndRoot();
        addLevelScenesAndRoots();
    }

    private void addCustomizationSceneAndRoot() {
        makeNewRootAndSceneAtIndex(0);
        //FIX setup customization
    }

    private void addSplashSceneAndRoot() {
        makeNewRootAndSceneAtIndex(1);
        //FIX setup splash
    }

    private void addLevelScenesAndRoots() {
        for (int num = 2; num< myScenes.length; num++) {
            makeNewRootAndSceneAtIndex(num);
        }
    }

    private void makeNewRootAndSceneAtIndex(int k) {
        mySceneRoots[k] = new Group();
        myScenes[k] = new Scene(mySceneRoots[k], mySceneWidth, mySceneHeight, myBackgroundColor);
    }

    private void updateSceneColor(int index) {
        myScenes[index].setFill(myBackgroundColor);
    }

    /**
     * Call from the splash screen or after completing a level
     * @return next level's blocks
     */
    public ArrayList<Block> getCurrentLevelBlocks() {
        myCurrentNumScene += 1;
        Level nextLevel = myLevels[myCurrentNumScene];
        return nextLevel.initialize(mySceneWidth, mySceneHeight);
    }

    /**
     * Use to return to the splash screen after the game is over or the player has won
     * @return splash screen Scene
     */
    public Scene getSplashScreen() {
        return myScenes[SPLASH];
    }

    private Scene getCustomizationScene() {
        return myScenes[CUSTOMIZATION];
    }

    /**
     * @return current scene
     */
    public Scene getCurrentScene() { return myScenes[myCurrentNumScene]; }

    /**
     * @return root of current scene
     */
    public Group getCurrentRoot() { return mySceneRoots[myCurrentNumScene]; }

    /**
     * @return true if current scene is a level
     */
    public boolean currentSceneIsLevel() {
        return myCurrentNumScene != SPLASH && myCurrentNumScene != CUSTOMIZATION;
    }
}
