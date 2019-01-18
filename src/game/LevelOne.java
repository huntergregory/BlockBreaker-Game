package game;

import java.util.ArrayList;

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
public class LevelOne implements Level {
    private final int DISTANCE_FROM_SIDES = 1;  // in Block.WIDTH units
    private final int DISTANCE_FROM_TOP = 3;    // in Block.HEIGHT units
    private final int DISTANCE_FROM_BOTTOM = 10; // in Block.HEIGHT units

    private ArrayList<Block> configuration;
    private int numRows;
    private int numCols;

    @Override
    public ArrayList<Block> initialize(double sceneWidth, double sceneHeight) {
        configuration = new ArrayList<>();
        numCols = getNumColumns(sceneWidth);
        numRows = getNumRows(sceneHeight);
        double xStart = getXStart(sceneWidth);
        double yStart = getYStart(sceneHeight);
        System.out.println("columns: " + numCols + "\nrows: " + numRows + "\nStart: (" + xStart + ", " + yStart + ")");
        updateConfiguration(xStart, yStart);
        return configuration;
    }

    //based on the equation:
    // c * Block.WIDTH + (c-1) * SEPARATION_DISTANCE <= sceneWidth - (2 * DISTANCE_FROM_SIDES) * Block.WIDTH
    private int getNumColumns(double sceneWidth) {
        double numCols = (sceneWidth - (DISTANCE_FROM_SIDES * 2) * Block.WIDTH + SEPARATION_DISTANCE) /
                (Block.WIDTH + SEPARATION_DISTANCE);
        return (int) Math.floor(numCols);
    }

    //based on the equation:
    // r * Block.Height + (r-1) * SEPARATION_DISTANCE <= sceneHeight - (DISTANCE_FROM_TOP + DISTANCE_FROM_BOTTOM) * Block.Height
    private int getNumRows(double sceneWidth) {
        double numRows = (sceneWidth - (DISTANCE_FROM_TOP + DISTANCE_FROM_BOTTOM) * Block.HEIGHT + SEPARATION_DISTANCE) /
        (Block.HEIGHT + SEPARATION_DISTANCE);
        return (int) Math.floor(numRows);
    }

    private double getXStart(double sceneWidth) {
        double extraSpace = sceneWidth - numCols * Block.WIDTH - (numCols - 1) * SEPARATION_DISTANCE -
                (2 * DISTANCE_FROM_SIDES) * Block.WIDTH;
        return extraSpace / 2 + Block.WIDTH * DISTANCE_FROM_SIDES;
    }

    private double getYStart(double sceneHeight) {
        double extraSpace = sceneHeight - numRows * Block.HEIGHT - (numRows - 1) * SEPARATION_DISTANCE -
                (DISTANCE_FROM_TOP + DISTANCE_FROM_BOTTOM) * Block.HEIGHT;
        return extraSpace / 2 + Block.HEIGHT * DISTANCE_FROM_TOP;
    }

    private void updateConfiguration(double xStart, double yStart) {
        double xCurrent = xStart;
        double yCurrent = yStart;

        for (int r=0; r<numRows; r++) {
            for (int c=0; c<numCols; c++) {
                if (isAlphaBlockLocation(r,c)) {
                    appendBlock(xCurrent, yCurrent, BlockType.ALPHA);
                }
                else if (isBetaBlockLocation(r,c)) {
                    appendBlock(xCurrent, yCurrent, BlockType.BETA);
                }
                else {
                    appendBlock(xCurrent, yCurrent, BlockType.GAMMA);
                }
                xCurrent += Block.WIDTH + SEPARATION_DISTANCE;
            }
            yCurrent += Block.HEIGHT + SEPARATION_DISTANCE;
            xCurrent = xStart;
        }
    }

    //assuming numRows, numCols >= 1
    private boolean isAlphaBlockLocation(int row, int col) {
        return row == 0 || row == numRows - 1 || col == 0 || col == numCols - 1;
    }

    //assuming that this is called after isAlphaBlockLocation()
    //assuming numRows, numCols >= 2
    private boolean isBetaBlockLocation(int row, int col) {
        return row == 1 || row == numRows - 2 || col == 1 || col == numCols - 2;
    }

    private void appendBlock(double xCurrent, double yCurrent, BlockType type) {
        Block block = new Block(type);
        block.setPosition(xCurrent, yCurrent);
        block.getImageView().setPreserveRatio(false);
        block.getImageView().setFitHeight(Block.HEIGHT);
        block.getImageView().setFitWidth(Block.WIDTH);
        configuration.add(block);
    }


}
