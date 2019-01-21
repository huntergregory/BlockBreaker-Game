package game;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class LevelHandler {

    private Level myLevel;
    private Paddle myPaddle;

    /**
     * Create a handler for specified Level
     */
    public LevelHandler(Level level) {
        myLevel = level;
        myPaddle = myLevel.getPaddle();
        this.addEventListeners();
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
        if (code == KeyCode.S)
            myLevel.splitBalls();
        if (code == KeyCode.B)
            myPaddle.makeBig();
        if (code == KeyCode.L)
            myPaddle.setCanShootLasers(true);
        if (code == KeyCode.P)
            myPaddle.initPowerShot();
    }
}
