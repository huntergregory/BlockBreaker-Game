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
        if (getNoPaddleIsAiming())
            return;
        for (Paddle paddle : myLevel.getPaddles()) {
            if (code == KeyCode.RIGHT) {
                paddle.getAimer().rotateClockwise();
            }
            if (code == KeyCode.LEFT) {
                paddle.getAimer().rotateCounterClockwise();
            }
            if (code == KeyCode.SPACE) {
                paddle.getAimer().fire();
            }
        }
    }

    private boolean getNoPaddleIsAiming() {
        for (Paddle paddle : myLevel.getPaddles()) {
            if (paddle.getAimer().getIsCurrentlyAiming())
                return false;
        }
        return true;
    }

    private void handleMovement(KeyCode code) {
        for (Paddle paddle : myLevel.getPaddles()) {
            if (!paddle.getIsSidePaddle() && getNoPaddleIsAiming()) {
                if (code == KeyCode.RIGHT && paddle.getX() + paddle.getWidth() < myLevel.getAssignedWidth())
                    paddle.setX(paddle.getX() + Paddle.SPEED); //currently overshoots boundary a little due to large SPEED
                if (code == KeyCode.LEFT && paddle.getX() > 0)
                    paddle.setX(paddle.getX() - Paddle.SPEED); //same as above
            }
            else if (getNoPaddleIsAiming()) {
                if (code == KeyCode.DOWN && paddle.getY() + Paddle.DEFAULT_WIDTH/2 < myLevel.getAssignedHeight()) {
                    paddle.setY(paddle.getY() + Paddle.SPEED); //currently overshoots boundary a little due to large SPEED
                }

                if (code == KeyCode.UP && paddle.getY() + Paddle.DEFAULT_WIDTH/2 > StatusBar.HEIGHT) {
                    paddle.setY(paddle.getY() - Paddle.SPEED); //same as above
                    if (paddle.getY() < StatusBar.HEIGHT)
                        paddle.setY(StatusBar.HEIGHT);
                }
            }
        }
    }

    private void handleAddingLaser(KeyCode code) {
        for (Paddle paddle : myLevel.getPaddles()) {
            if (code == KeyCode.SPACE) {
                if (paddle.getCanShootLasers()) {
                    Laser laser = new Laser(paddle.getX() + paddle.getWidth() / 2, paddle.getY());
                    myLevel.getLasers().add(laser);
                    myLevel.addGameObjectToRoot(laser);
                }
            }
        }
    }

    private void handleCheatCodes(KeyCode code) {
        if (code == KeyCode.S) {
            myLevel.splitBalls();
            myLevel.getStatusBar().setPowerupType(PowerupType.SPLIT);
        }
        if (code == KeyCode.B) {
            for (Paddle paddle : myLevel.getPaddles()) {
                paddle.makeBig();
                myLevel.getStatusBar().setPowerupType(PowerupType.BIG_PADDLE);
            }
        }
        if (code == KeyCode.L) {
            for (Paddle paddle : myLevel.getPaddles()) {
                paddle.setCanShootLasers(true);
                myLevel.getStatusBar().setPowerupType(PowerupType.LASER);
            }
        }
        if (code == KeyCode.P) {
            for (Paddle paddle : myLevel.getPaddles()) {
                paddle.initPowerShot();
                myLevel.getStatusBar().setPowerupType(PowerupType.POWER_SHOT);
            }
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
