package game;

import java.util.ArrayList;

/**
 * Creates a template for any level in this game.
 * @author Hunter Gregory
 */
public interface Level {
    final double BLOCK_WIDTH = 45;
    final double BLOCK_HEIGHT = 15;
    final double SEPARATION_DISTANCE = 5;

    /**
     * Initialize the level
     * @param sceneHeight
     * @param sceneWidth
     * @return ArrayList of Block objects representing the configuration of a level
     */
    ArrayList<Block> initialize(double sceneWidth, double sceneHeight);
}
