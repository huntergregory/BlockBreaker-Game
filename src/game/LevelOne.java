package game;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.HashSet;
import java.util.Random;

/**
 * A basic level with destructible blocks clumped in the middle of the screen.
 *
 * Pattern: Rectangular clump of Block objects that is
 *              1 block-widths or more away from left and right side of scene,
 *              3 block-widths or more away from top of scene
 *              7 block-widths or more away from bottom of scene.
 *          Block objects separated by SEPARATION_DISTANCE.
 *
 * @author Hunter Gregory
 */
public class LevelOne extends Level {
    private final int DISTANCE_FROM_SIDES = 1;  // in Block.WIDTH units
    private final int DISTANCE_FROM_TOP = 5;    // in Block.HEIGHT units
    private final int DISTANCE_FROM_BOTTOM = 12; // in Block.HEIGHT units

    private int myNumRows = 0;
    private int myNumCols = 0;

    public LevelOne(int width, int height) { this(width, height, Color.WHITE, new Random()); }

    public LevelOne(int width, int height, Paint backgroundColor, Random rand) {
        super(width, height, backgroundColor, rand);
        initializeBlocks();
        initializePowerups();
        addAllToRoot();
    }

    private void initializeBlocks() {
        myNumCols = getNumColumns();
        myNumRows = getNumRows();
        double xStart = getXStart();
        double yStart = getYStart();
        updateBlockConfiguration(xStart, yStart);
    }

    private int getNumColumns() {
        double numCols = (myAssignedWidth - (DISTANCE_FROM_SIDES * 2) * Block.WIDTH + SEPARATION_DISTANCE)
                / (Block.WIDTH + SEPARATION_DISTANCE);
        return (int) Math.floor(numCols);
    }

    private int getNumRows() {
        double numRows = (myAssignedHeight - StatusBar.HEIGHT - (DISTANCE_FROM_TOP + DISTANCE_FROM_BOTTOM) * Block.HEIGHT + SEPARATION_DISTANCE)
                            / (Block.HEIGHT + SEPARATION_DISTANCE);
        return (int) Math.floor(numRows);
    }

    private double getXStart() {
        double extraSpace = myAssignedWidth - myNumCols * Block.WIDTH - (myNumCols - 1) * SEPARATION_DISTANCE -
                (2 * DISTANCE_FROM_SIDES) * Block.WIDTH;
        return extraSpace / 2 + Block.WIDTH * DISTANCE_FROM_SIDES;
    }

    private double getYStart() {
        double extraSpace = myAssignedHeight - myNumRows * Block.HEIGHT - (myNumRows - 1) * SEPARATION_DISTANCE -
                (DISTANCE_FROM_TOP + DISTANCE_FROM_BOTTOM) * Block.HEIGHT;
        return extraSpace / 2 + Block.HEIGHT * DISTANCE_FROM_TOP;
    }

    private void updateBlockConfiguration(double xStart, double yStart) {
        double xCurrent = xStart;
        double yCurrent = yStart;

        for (int r = 0; r< myNumRows; r++) {
            for (int c = 0; c< myNumCols; c++) {
                if (isAlphaBlockLocation(r,c)) {
                    getBlockConfiguration().add(new Block(xCurrent, yCurrent, BlockType.ALPHA));
                }
                else if (isBetaBlockLocation(r,c)) {
                    getBlockConfiguration().add(new Block(xCurrent, yCurrent, BlockType.BETA));
                }
                else {
                    getBlockConfiguration().add(new Block(xCurrent, yCurrent, BlockType.GAMMA));
                }
                xCurrent += Block.WIDTH + SEPARATION_DISTANCE;
            }
            yCurrent += Block.HEIGHT + SEPARATION_DISTANCE;
            xCurrent = xStart;
        }
    }

    //assuming myNumRows, myNumCols >= 1
    private boolean isAlphaBlockLocation(int row, int col) {
        return row == 0 || row == myNumRows - 1 || col == 0 || col == myNumCols - 1;
    }

    //assuming that this is called after isAlphaBlockLocation()
    //assuming myNumRows, myNumCols >= 2
    private boolean isBetaBlockLocation(int row, int col) {
        return row == 1 || row == myNumRows - 2 || col == 1 || col == myNumCols - 2;
    }

    //assumes there are at least NUM_POWERUPS blocks in myBlockConfiguration
    private void initializePowerups() {
        HashSet<Integer> indices = new HashSet<>();
        while (getPowerupConfiguration().size() < NUM_POWERUPS) {
            var powerup = Powerup.getRandomPowerup();
            Integer randIndex = getRand().nextInt(getBlockConfiguration().size());
            if (indices.contains(randIndex))
                continue;
            Block correspondingBlock = getBlockConfiguration().get(randIndex);
            powerup.setX(correspondingBlock.getX() + ((Block.WIDTH - Powerup.WIDTH) / 2));
            powerup.setY(correspondingBlock.getY() + ((Block.HEIGHT - Powerup.HEIGHT) / 2));
            getPowerupConfiguration().add(powerup);
            indices.add(randIndex);
        }
    }

    private void addAllToRoot() {
        for (Block block : getBlockConfiguration()) {
            addGameObjectToRoot(block);
        }
        for (Powerup powerup : getPowerupConfiguration()) {
            addGameObjectToRoot(powerup);
        }
    }
}
