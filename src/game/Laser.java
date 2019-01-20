package game;

public class Laser extends GameObject implements Movable {
    public static final int VEL_Y = -80;

    /**
     * Create a Laser
     * @param imageName
     * @param width
     * @param height
     */
    public Laser(String imageName, int width, int height) {
        super(imageName,width,height);
    }

    @Override
    public void updatePosition(double elapsedTime) {
        this.setY(this.getY() + elapsedTime * VEL_Y);
    }
}
