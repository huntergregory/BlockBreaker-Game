package game;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.image.ImageView;
import javafx.scene.Node;

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
        constructSplashSceneAndRoot();
        constructCustomSceneAndRoot();
        addLevelScenesAndRoots();
    }

    private void constructCustomSceneAndRoot() {
        makeNewRootAndSceneAtIndex(0);
        //FIX setup customization
    }

    private void constructSplashSceneAndRoot() {
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
     * Call from the splash screen or after completing a level.
     * @return next level's blocks
     */
    public ArrayList<Block> getNewLevelBlocks() {
        myCurrentNumScene += 1;
        Level nextLevel = myLevels[myCurrentNumScene];
        ArrayList<Block> list = nextLevel.initialize(mySceneWidth, mySceneHeight);
        for (Block block : list) {
            addNodeToRoot(block.getImageView());
        }
        return list;
    }

    /**
     * Call to initialize scene or restart after lost life.
     * @return list of one Ball
     */
    public ArrayList<Ball> resetAndGetBalls() {
        ArrayList<Ball> list = new ArrayList<>();
        Ball ball = new Ball(100, 350, 60, -65); // FIX magic numbers
        list.add(ball);
        addNodeToRoot(ball.getImageView());
        return list;
    }

    /**
     * Call to initialize scene or restart after lost life.
     * @return Paddle
     */
    public Paddle resetAndGetPaddle() {
        Paddle paddle = new Paddle(mySceneWidth - mySceneWidth / 2 - Paddle.DEFAULT_WIDTH / 2,
                                   mySceneHeight - Paddle.HEIGHT - 2);
        addNodeToRoot(paddle.getImageView());
        return paddle;
    }

    /**
     * Add a node to the children of the current scene's root
     * @param node
     */
    public void addNodeToRoot(Node node) {
        getCurrentRootChildren().add(node);
    }

    /**
     * Remove a node from the children of the current scene's root
     * @param node
     */
    public void removeNodeFromRoot(Node node) {
        getCurrentRootChildren().remove(node);
    }

    private ObservableList<Node> getCurrentRootChildren() { return mySceneRoots[myCurrentNumScene].getChildren(); }

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
     * @return true if current scene is a level
     */
    public boolean currentSceneIsLevel() {
        return myCurrentNumScene != SPLASH && myCurrentNumScene != CUSTOMIZATION;
    }
}
