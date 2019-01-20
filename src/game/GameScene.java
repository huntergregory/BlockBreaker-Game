package game;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;

/**
 * Stores a scene and a Group object as well as methods to modify the scene's children.
 * @author Hunter Gregory
 */
public class GameScene {
    Scene myScene;
    Group myRoot;
    int myAssignedWidth;
    int myAssignedHeight;

    /**
     * Create a GameScene with a Scene and a Group as its root
     * @param width
     * @param height
     * @param backgroundColor
     */
    public GameScene(int width, int height, Paint backgroundColor) {
        myRoot = new Group();
        myScene = new Scene(myRoot, width, height, backgroundColor);
        myAssignedWidth = width;
        myAssignedHeight = height;
    }

    /**
     * Add a node to the children of the scene's root
     * @param node
     */
    public void addNodeToRoot(Node node) {
       myRoot.getChildren().add(node);
    }

    /**
     * Remove a node from the children of the scene's root
     * @param node
     */
    public void removeNodeFromRoot(Node node) {
        myRoot.getChildren().remove(node);
    }

    /**
     * @return the associated scene
     */
    public Scene getScene() { return myScene; }

    /**
     * @return the root of the GameScene
     */
    public Group getRoot() { return myRoot; }

    /**
     * @return width assigned at initialization
     */
    public int getAssignedWidth() { return myAssignedWidth; }

    /**
     * @return height assigned at initialization
     */
    public int getAssignedHeight() { return myAssignedHeight; }

    /**
     * @return current width of Scene
     */
    public double getCurrentWidth() { return myScene.getWidth(); }

    /**
     * @return current height of Scene
     */
    public double getCurrentHeight() { return myScene.getHeight(); }
}
