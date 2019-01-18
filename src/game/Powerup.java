package game;

import javafx.scene.image.ImageView;

/**
 * Consists of a PowerupType and ImageView representing the ball
 * @author Hunter Gregory
 */
public class Powerup {
    public static final int VEL_X = 20;

    private PowerupType myType;
    private ImageView myImageView;

    public Powerup(PowerupType type) {
        myType = type;
        myImageView = new ImageView(myType.getImageName());
    }

    public ImageView getMyImageView() { return myImageView; }
}
