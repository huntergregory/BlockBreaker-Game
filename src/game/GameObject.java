package game;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A representation using an ImageView for an object in any level of this game.
 * Allows the Game's main method to work with GameObjects without dealing with ImageView's.
 * The one exception is adding or removing a GameObject from the current scene's root.
 */
public abstract class GameObject {
    public ImageView myImageView;

    /**
     * Construct a GameObject with a particular image
     * @param imageName
     * @param width
     * @param height
     */
    public GameObject(String imageName, int width, int height) {
        myImageView = new ImageView();
        setImageFromName(imageName);
        myImageView.setPreserveRatio(false);
        myImageView.setFitWidth(width);
        myImageView.setFitHeight(height);
    }

    /**
     * @return true if this GameObject is intersecting another
     */
    public boolean hitGameObject(GameObject object) {
        return object.getParentBounds().intersects(this.getParentBounds());
    }

    /**
     * Don't use with Paddle
     * @return true if GameObject has left the boundaries of the Scene
     */
    public boolean isOutOfBounds(double sceneWidth, double sceneHeight) {
        int buffer = 0; //in case an object temporarily overshoots the boundaries
        return this.getX() <= -buffer
                || this.getX() + this.getWidth() >= sceneWidth + buffer
                || this.getY() <= StatusBar.HEIGHT - buffer
                || this.getY() + this.getHeight() >= sceneHeight + buffer;
    }

    /**
     * Updates the image of the ImageView
     * @param imageName
     */
    protected void setImageFromName(String imageName) {
        Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
        myImageView.setImage(image);
    }

    protected ImageView getImageView() { return myImageView; }

    /**
     * @return Bounds in parent for the ImageView associated with the GameObject
     */
    protected Bounds getParentBounds() { return myImageView.getBoundsInParent(); }

    /**
     * @return width of ImageView corresponding to GameObject
     */
    protected double getWidth() { return this.getParentBounds().getWidth(); }

    /**
     * @return height of ImageView corresponding to GameObject
     */
    protected double getHeight() { return this.getParentBounds().getHeight(); }

    /**
     * @return  x position of GameObject's ImageView
     */
    protected double getX() { return myImageView.getX(); }

    /**
     * @return y position of GameObject's ImageView
     */
    protected double getY() { return myImageView.getY(); }

    /**
     * Set x position of GameObject's ImageView
     * @param x
     */
    protected void setX(double x) { myImageView.setX(x); }

    /**
     * Set y position of GameObject's ImageView
     * @param y
     */
    protected void setY(double y) { myImageView.setY(y); }
}
