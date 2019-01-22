package game;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class LevelHandler {

    private Level myLevel;
    private boolean myShouldSkipToNextLevel;
    private boolean myShouldSkipToPreviousLevel;


    /**
     * Create a handler for specified Level
     */
    public LevelHandler(Level level) {
        myLevel = level;
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
        if (myLevel.getPauser().getIsPaused())
            return;
        handleOfficialCodes(code);
        handleCheatCodes(code);
    }

    private void handleOfficialCodes(KeyCode code) {
        handleAimer(code);
        handleMovement(code);
        handleAddingLaser(code);
    }

    private void handleAimer(KeyCode code) {
        if (!myLevel.getPaddle().getAimer().getIsCurrentlyAiming())
            return;
        if (code == KeyCode.RIGHT) {
            myLevel.getPaddle().getAimer().rotateClockwise();
        }
        if (code == KeyCode.LEFT) {
            myLevel.getPaddle().getAimer().rotateCounterClockwise();
        }
        if (code == KeyCode.SPACE) {
            myLevel.getPaddle().getAimer().fire();
        }
    }

    private void handleMovement(KeyCode code) {
        Paddle paddle = myLevel.getPaddle();
        boolean isAiming = paddle.getAimer().getIsCurrentlyAiming();
        if (code == KeyCode.RIGHT && !isAiming && paddle.getX() + paddle.getWidth() < myLevel.getAssignedWidth())
            paddle.setX(paddle.getX() + Paddle.SPEED); //currently overshoots boundary a little due to large SPEED
        if (code == KeyCode.LEFT && !isAiming && paddle.getX() > 0)
            paddle.setX(paddle.getX() - Paddle.SPEED); //same as above
    }

    private void handleAddingLaser(KeyCode code) {
        Paddle paddle = myLevel.getPaddle();
        if (code == KeyCode.SPACE) {
            if (paddle.getCanShootLasers()) {
                Laser laser = new Laser(paddle.getX() + paddle.getWidth() / 2, paddle.getY());
                myLevel.getLasers().add(laser);
                myLevel.addGameObjectToRoot(laser);
            }
        }
    }

    private void handleCheatCodes(KeyCode code) {
        Paddle paddle = myLevel.getPaddle();
        if (code == KeyCode.S) {
            myLevel.splitBalls();
            myLevel.getStatusBar().setPowerupType(PowerupType.SPLIT);
            System.out.println("setting type from handler");
        }
        if (code == KeyCode.B) {
            paddle.makeBig();
            myLevel.getStatusBar().setPowerupType(PowerupType.BIG_PADDLE);
            System.out.println("setting type from handler");
        }
        if (code == KeyCode.L) {
            paddle.setCanShootLasers(true);
            myLevel.getStatusBar().setPowerupType(PowerupType.LASER);
            System.out.println("setting type from handler");
        }
        if (code == KeyCode.P) {
            paddle.initPowerShot();
            myLevel.getStatusBar().setPowerupType(PowerupType.POWER_SHOT);
            System.out.println("setting type from handler");
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
