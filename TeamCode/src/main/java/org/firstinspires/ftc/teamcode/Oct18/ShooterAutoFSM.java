package org.firstinspires.ftc.teamcode.Oct18;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


public class ShooterAutoFSM {

    public enum FSMState {
        IDLE,
        SPIN_SHOOTER,
        SPINDEXER_SET,
        ELEVATOR_RIGHT_DOWN,
        ELEVATOR_LEFT_DOWN,
        SHOOTER_RECOVER,
        ELEVATORS_UP
    }

    private FSMState fsmState = FSMState.IDLE;
    private final ElapsedTime fsmTimer = new ElapsedTime();
    private boolean fsmActive = false;
    private int fsmBall = 0;
    private boolean multiFSM = false;

    public final Servo elevatorLeft, elevatorRight, spindexer;
    private final DcMotor shooterLeft, shooterRight;

    public ShooterAutoFSM(Servo elevatorLeft, Servo elevatorRight, Servo spindexer,
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
                shooterLeft.setPower(RobotHardware.SHOOTER_ON);
                shooterRight.setPower(RobotHardware.SHOOTER_ON);

                // Allow spool-up time before advancing
                if (fsmTimer.milliseconds() > 150) {
                    fsmState = FSMState.SPINDEXER_SET;
                    fsmTimer.reset();
                }
                break;

            case SPINDEXER_SET:
                if (fsmBall == 1) spindexer.setPosition(RobotHardware.SPINDEXER_ONE);
                if (fsmBall == 2) spindexer.setPosition(RobotHardware.SPINDEXER_TWO);
                if (fsmBall == 3) spindexer.setPosition(RobotHardware.SPINDEXER_THREE);

                // INCREASED THE WAIT TIME HERE
                if (multiFSM && fsmBall == 1) {
                    if (fsmTimer.milliseconds() > 950) {
                        fsmState = FSMState.ELEVATOR_RIGHT_DOWN;
                        fsmTimer.reset();
                    }
                } else {
                    if (fsmTimer.milliseconds() > 600) {
                        fsmState = FSMState.ELEVATOR_RIGHT_DOWN;
                        fsmTimer.reset();
                    }
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
                if (fsmTimer.milliseconds() > 400) {
                    fsmState = FSMState.SHOOTER_RECOVER;
                    fsmTimer.reset();
                }
                break;

            case SHOOTER_RECOVER:
                if (fsmTimer.milliseconds() > 100) {
                    fsmState = FSMState.ELEVATORS_UP;
                    fsmTimer.reset();
                }
                break;

            case ELEVATORS_UP:
                elevatorLeft.setPosition(RobotHardware.ELEVATOR_LEFT_UP);
                elevatorRight.setPosition(RobotHardware.ELEVATOR_RIGHT_UP);

                if (fsmTimer.milliseconds() > 350) {
                    if (multiFSM && fsmBall < 3) {
                        fsmBall++;
                        fsmState = FSMState.SPINDEXER_SET;
                        fsmTimer.reset();
                    } else {
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

    public Action runFSMAction(int ball, boolean multi) {
        return new Action() {
            private boolean started = false;

            @Override
            public boolean run(TelemetryPacket packet) {
                if (!started) {
                    startFSM(ball, multi);
                    started = true;
                }
                updateFSM();
                return fsmActive; // continue running until FSM completes
            }
        };
    }
}
