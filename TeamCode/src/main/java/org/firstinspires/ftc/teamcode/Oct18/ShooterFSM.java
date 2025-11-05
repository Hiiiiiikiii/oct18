package org.firstinspires.ftc.teamcode.Oct18;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class ShooterFSM {

    public enum FSMState {
        IDLE,
        SPIN_SHOOTER,
        SPINDEXER_SET,
        ELEVATOR_RIGHT_DOWN,
        ELEVATOR_LEFT_DOWN,
        SHOOTER_RECOVER,      // new state to let shooter spool up again
        ELEVATORS_UP
    }

    private FSMState fsmState = FSMState.IDLE;
    private final ElapsedTime fsmTimer = new ElapsedTime();
    private boolean fsmActive = false;
    private int fsmBall = 0;
    private boolean multiFSM = false;

    private final Servo elevatorLeft, elevatorRight, spindexer;
    private final DcMotor shooterLeft, shooterRight;

    public ShooterFSM(Servo elevatorLeft, Servo elevatorRight, Servo spindexer,
                      DcMotor shooterLeft, DcMotor shooterRight) {
        this.elevatorLeft = elevatorLeft;
        this.elevatorRight = elevatorRight;
        this.spindexer = spindexer;
        this.shooterLeft = shooterLeft;
        this.shooterRight = shooterRight;
    }

    public void startFSM(int ball, boolean multi) {
        fsmBall = ball;
        multiFSM = multi;
        fsmActive = true;
        fsmState = FSMState.SPIN_SHOOTER;
        fsmTimer.reset();
    }

    public void updateFSM() {
        if (!fsmActive) return;

        switch (fsmState) {

            case SPIN_SHOOTER:
                // Turn on shooter once at start
                shooterLeft.setPower(RobotHardware.SHOOTER_ON);
                shooterRight.setPower(RobotHardware.SHOOTER_ON);

                // Give time to spool up
                if (fsmTimer.milliseconds() > 100) {
                    fsmState = FSMState.SPINDEXER_SET;
                    fsmTimer.reset();
                }
                break;

            case SPINDEXER_SET:
                if (fsmBall == 1) spindexer.setPosition(RobotHardware.SPINDEXER_ONE);
                if (fsmBall == 2) spindexer.setPosition(RobotHardware.SPINDEXER_TWO);
                if (fsmBall == 3) spindexer.setPosition(RobotHardware.SPINDEXER_THREE);

                if (fsmTimer.milliseconds() > 100) {
                    fsmState = FSMState.ELEVATOR_RIGHT_DOWN;
                    fsmTimer.reset();
                }
                break;

            case ELEVATOR_RIGHT_DOWN:
                elevatorRight.setPosition(RobotHardware.ELEVATOR_RIGHT_DOWN);
                if (fsmTimer.milliseconds() > 100) {
                    fsmState = FSMState.ELEVATOR_LEFT_DOWN;
                    fsmTimer.reset();
                }
                break;

            case ELEVATOR_LEFT_DOWN:
                elevatorLeft.setPosition(RobotHardware.ELEVATOR_LEFT_DOWN);
                // After both elevators are down, let shooter recover speed
                if (fsmTimer.milliseconds() > 400) {
                    fsmState = FSMState.SHOOTER_RECOVER;
                    fsmTimer.reset();
                }
                break;

            case SHOOTER_RECOVER:
                // Do nothing, just wait to let shooter spool up again
                if (fsmTimer.milliseconds() > 100) {
                    fsmState = FSMState.ELEVATORS_UP;
                    fsmTimer.reset();
                }
                break;

            case ELEVATORS_UP:
                elevatorLeft.setPosition(RobotHardware.ELEVATOR_LEFT_UP);
                elevatorRight.setPosition(RobotHardware.ELEVATOR_RIGHT_UP);

                if (fsmTimer.milliseconds() > 300) {
                    if (multiFSM && fsmBall < 3) {
                        fsmBall++;
                        fsmState = FSMState.SPINDEXER_SET;
                        fsmTimer.reset();
                    } else {
                        // Done shooting all balls
                        shooterLeft.setPower(RobotHardware.SHOOTER_OFF);
                        shooterRight.setPower(RobotHardware.SHOOTER_OFF);
                        spindexer.setPosition(RobotHardware.SPINDEXER_ONE);
                        fsmState = FSMState.IDLE;
                        fsmActive = false;
                    }
                }
                break;
        }
    }

    public boolean isActive() { return fsmActive; }
    public FSMState getState() { return fsmState; }
    public int getBall() { return fsmBall; }
}
