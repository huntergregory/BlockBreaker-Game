package game;

import javafx.scene.image.ImageView;

/**
 * Consists of a PowerupType and ImageView representing the ball
 * @author Hunter Gregory
 */
public class Powerup extends GameObject{
    public static final int WIDTH = 35;
    public static final int HEIGHT = 10;
    public static final int VEL_X = 20;

    private PowerupType myType;

    public Powerup(PowerupType type) {
        super(type.getImageName(), WIDTH, HEIGHT);
        myType = type;
    }
}
