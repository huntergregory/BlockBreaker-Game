package game;

public interface Movable {
    /**
     * Internally updates x and y positions based on a specified time step and internal x and y velocities
     * @param elapsedTime
     */
    public void updatePosition(double elapsedTime);
}
