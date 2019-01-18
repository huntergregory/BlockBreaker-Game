package game;

import javafx.scene.Scene;
import javafx.scene.Group;

import java.util.ArrayList;


/**
 * Constructs, stores, and manages all scenes needed for a Block Breaker game.
 * @author Hunter Gregory
 */
public class SceneManager {

    private Scene splashScene;
    private Scene customizationScene;
    private int mySceneWidth;
    private int mySceneHeight;
    private Level[] myLevels;
    private Scene[] myLevelScenes;
    private Group[] myLevelRoots;
    private int myCurrentNumScene;

    /**
     * Levels must be in this order: Customization, Splash, LevelOne, LevelTwo, ...
     * @param levels
     */
    public SceneManager(Level[] levels, int width, int height) {
        myLevels = levels;
        myCurrentNumScene = 0;
        mySceneWidth = width;
        mySceneHeight = height;

        myLevelScenes = new Scene[levels.length];
        myLevelRoots = new Group[levels.length];
        setupScenes();
    }

    /**
     * Call from the splash screen or after completing a level
     * @return next level's blocks
     */
    public ArrayList<Block> getNextLevelBlocks() {
        myCurrentNumScene += 1;
        Level nextLevel = myLevels[myCurrentNumScene];
        return nextLevel.initialize(mySceneWidth, mySceneHeight);
    }
    }

    /**
     * Use to return to the splash screen after the game is over or the player has won
     * @return splash screen Scene
     */
    public Scene getSplashScreen() {
        return splashScene;
    }

    private Scene getCustomization() {
        return customizationScene;
    }

    private Scene setupScenes() {
        for (int num = 0; num< myLevelScenes.length; num++) {
            myLevelRoots[num] = new Group();
            myLevelScenes[num] = new Scene(myLevelRoots[num], mySceneWidth, mySceneHeight, backgroundColor);
        }
    }

    /**
     * @return the current scene's height
     */
    public double getSceneHeight() { return myLevelScenes[myCurrentNumScene].getHeight(); }

    /**
     * @return the current scene's width
     */
    public double getSceneWidth() { return myLevelScenes[myCurrentNumScene].getHeight(); }

    /**
     * @return current root of level
     */
    public Group getCurrentRoot() { return myLevelRoots[myCurrentNumScene]; }
}
