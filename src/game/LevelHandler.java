package game;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class LevelHandler {

    private Level myLevel;
    private Paddle myPaddle; //made instance variable because used a lot
    private boolean myShouldSkipToNextLevel;
    private boolean myShouldSkipToPreviousLevel;


    /**
     * Create a handler for specified Level
     */
    public LevelHandler(Level level) {
        myLevel = level;
        myPaddle = myLevel.getPaddle();
        myShouldSkipToNextLevel = false;
        myShouldSkipToPreviousLevel = false;
        addEventListeners();
    }

    private void addEventListeners() {
        Scene currentScene = myLevel.getScene();
        currentScene.setOnMouseClicked(e -> handleMouseClick());
        currentScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    }

    private void handleMouseClick() {
        myLevel.togglePauseStatus();
    }

    private void handleKeyInput(KeyCode code) {
        handleOfficialCodes(code);
        handleCheatCodes(code);
    }

    private void handleOfficialCodes(KeyCode code) {
        handleAimer(code);
        handleMovement(code);
        handleAddingLaser(code);
    }

    private void handleAimer(KeyCode code) {
        if (!myPaddle.getAimer().getIsCurrentlyAiming())
            return;
        if (code == KeyCode.RIGHT) {
            myPaddle.getAimer().rotateClockwise();
        }
        if (code == KeyCode.LEFT) {
            myPaddle.getAimer().rotateCounterClockwise();
        }
        if (code == KeyCode.SPACE) {
            myPaddle.getAimer().fire();
        }
    }

    private void handleMovement(KeyCode code) {
        boolean isAiming = myPaddle.getAimer().getIsCurrentlyAiming();
        if (code == KeyCode.RIGHT && !isAiming && myPaddle.getX() + myPaddle.getWidth() < myLevel.getAssignedWidth())
            myPaddle.setX(myPaddle.getX() + Paddle.SPEED); //currently overshoots boundary a little due to large SPEED
        if (code == KeyCode.LEFT && !isAiming && myPaddle.getX() > 0)
            myPaddle.setX(myPaddle.getX() - Paddle.SPEED); //same as above
    }

    private void handleAddingLaser(KeyCode code) {
        if (code == KeyCode.SPACE) {
            if (myPaddle.getCanShootLasers()) {
                Laser laser = new Laser(myPaddle.getX() + myPaddle.getWidth() / 2, myPaddle.getY());
                myLevel.getLasers().add(laser);
                myLevel.addGameObjectToRoot(laser);
            }
        }
    }

    private void handleCheatCodes(KeyCode code) {
        if (code == KeyCode.S) {
            myLevel.splitBalls();
            myLevel.getStatusBar().setPowerupType(PowerupType.SPLIT);
        }
        if (code == KeyCode.B) {
            myPaddle.makeBig();
            myLevel.getStatusBar().setPowerupType(PowerupType.BIG_PADDLE);
        }
        if (code == KeyCode.L) {
            myPaddle.setCanShootLasers(true);
            myLevel.getStatusBar().setPowerupType(PowerupType.LASER);
        }
        if (code == KeyCode.P) {
            myPaddle.initPowerShot();
            myLevel.getStatusBar().setPowerupType(PowerupType.POWER_SHOT);
        }
        if (code == KeyCode.F) {
            multiplyAllBalls(1.25); // 25% faster
        }
        if (code == KeyCode.D) {
            multiplyAllBalls(0.75); // 25% slower
        }
        if (code == KeyCode.R) {
            myLevel.setLives(myLevel.getLives() + 1);
        }
        if (code == KeyCode.Z) {
            myShouldSkipToPreviousLevel = true;
        }
        if (code == KeyCode.X) {
            myShouldSkipToNextLevel = true;
        }
    }

    private void multiplyAllBalls(double multiplier) {
        for (Ball ball : myLevel.getBalls()) {
            ball.multiplyVelX(multiplier);
            ball.multiplyVelY(multiplier);
        }
    }

    /**
     * @return true if GameMain should go back to prior Level
     */
    public boolean getShouldSkipToPrevious() { return myShouldSkipToPreviousLevel; }

    /**
     * @return true if GameMain should go ahead to the next Level
     */
    public boolean getShouldSkipToNext() { return myShouldSkipToNextLevel; }
}
