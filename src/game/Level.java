package game;

import java.util.ArrayList;

/**
 * Creates a template for any level in this game.
 * @author Hunter Gregory
 */
public interface Level {
    /**
     * Initialize the level
     * @param blockHeight
     * @param blockWidth
     * @return ArrayList of Block objects representing the configuration of a level
     */
    ArrayList<Block> initialize(double blockWidth, double blockHeight);
}
