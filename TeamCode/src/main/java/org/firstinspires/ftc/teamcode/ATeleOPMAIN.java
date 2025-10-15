package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
public class ATeleOPMAIN extends LinearOpMode {

    // ================== CONSTANTS ==================
    // Elevator
    public static final double ELEVATOR_LEFT_UP = 222.0 / 355.0;
    public static final double ELEVATOR_LEFT_DOWN = 330.0 / 355.0;
    public static final double ELEVATOR_RIGHT_UP = 220.0 / 355.0;
    public static final double ELEVATOR_RIGHT_DOWN = 330.0 / 355.0;

    // Spindexer
    public static final double SPINDEXER_ONE = 0.0 / 355.0;
    public static final double SPINDEXER_TWO = 130.0 / 355.0;
    public static final double SPINDEXER_THREE = 270.0 / 355.0;
    public static final double SPINDEXER_INTAKE = 65.0 / 355.0;

    // Kicker
    public static final double KICKER_OUT = 286.0 / 355.0;
    public static final double KICKER_IN = 215.0 / 355.0;

    // Intake
    public static final double INTAKE_OFF = 0.0;
    public static final double INTAKE_ON = 1.0;
    public static final double INTAKE_REVERSE = -1.0;

    // Shooter
    public static final double SHOOTER_ON = 1.0;
    public static final double SHOOTER_OFF = 0.0;

    // Turret
    public static final double TURRET_INITIAL = 75.0 / 355.0;

    // Hood
    public static final double HOOD_INITIAL = 55.0 / 355.0;

    // Step
    private final double step = 5.0 / 355.0;

    // ================== HARDWARE ==================
    private Servo kicker, elevatorLeft, elevatorRight, turret, hoodLeft, hoodRight, spindexer;
    private DcMotor intake, shooterLeft, shooterRight;

    private double turretPos = TURRET_INITIAL;
    private double hoodLeftPos = HOOD_INITIAL;
    private double hoodRightPos = HOOD_INITIAL;

    // FSM
    private enum FSMState {
        IDLE, START, ELEVATOR_RIGHT_DOWN, ELEVATOR_LEFT_DOWN, ELEVATORS_UP, SHOOTER_ON, KICKER_IN, KICKER_OUT, SHOOTER_OFF
    }
    private FSMState fsmState = FSMState.IDLE;
    private ElapsedTime fsmTimer = new ElapsedTime();
    private boolean fsmActive = false;
    private int fsmBall = 0; // 1,2,3 for a,b,x

    @Override
    public void runOpMode() throws InterruptedException {

        // ===== DRIVETRAIN =====
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("backRightMotor");

        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);

        // ===== MECHANISM HARDWARE =====
        kicker = hardwareMap.get(Servo.class, "kick");
        elevatorLeft = hardwareMap.get(Servo.class, "el");
        elevatorRight = hardwareMap.get(Servo.class, "er");
        turret = hardwareMap.get(Servo.class, "tur");
        hoodLeft = hardwareMap.get(Servo.class, "hl");
        hoodRight = hardwareMap.get(Servo.class, "hr");
        spindexer = hardwareMap.get(Servo.class, "spin");

        intake = hardwareMap.get(DcMotor.class, "in");
        shooterLeft = hardwareMap.get(DcMotor.class, "sl");
        shooterRight = hardwareMap.get(DcMotor.class, "sr");

        // ===== INITIAL POSITIONS =====
        elevatorLeft.setPosition(ELEVATOR_LEFT_UP);
        elevatorRight.setPosition(ELEVATOR_RIGHT_UP);
        spindexer.setPosition(SPINDEXER_INTAKE);
        kicker.setPosition(KICKER_OUT);
        turret.setPosition(TURRET_INITIAL);
        hoodLeft.setPosition(hoodLeftPos);
        hoodRight.setPosition(hoodRightPos);
        intake.setPower(INTAKE_OFF);
        shooterLeft.setPower(SHOOTER_OFF);
        shooterRight.setPower(SHOOTER_OFF);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            // ===== DRIVE CONTROLS (UNCHANGED) =====
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            if (gamepad1.options) imu.resetYaw();

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);
            rotX *= 1.1;

            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            frontLeftMotor.setPower((rotY + rotX + rx) / denominator);
            backLeftMotor.setPower((rotY - rotX + rx) / denominator);
            frontRightMotor.setPower((rotY - rotX - rx) / denominator);
            backRightMotor.setPower((rotY + rotX - rx) / denominator);

            // ===== MANUAL CONTROLS =====
            // Hood triggers
            if (gamepad1.left_trigger > 0.2) {
                hoodLeftPos -= step;
                hoodRightPos -= step;
            }
            if (gamepad1.right_trigger > 0.2) {
                hoodLeftPos += step;
                hoodRightPos += step;
            }
            hoodLeftPos = Math.max(0, Math.min(1, hoodLeftPos));
            hoodRightPos = Math.max(0, Math.min(1, hoodRightPos));
            hoodLeft.setPosition(hoodLeftPos);
            hoodRight.setPosition(hoodRightPos);

            // Turret bumpers
            if (gamepad1.left_bumper) turretPos -= step;
            if (gamepad1.right_bumper) turretPos += step;
            turretPos = Math.max(0, Math.min(1, turretPos));
            turret.setPosition(turretPos);

            // Intake on/off/reverse (gamepad1 left/right/up)
            if (gamepad1.dpad_left) intake.setPower(INTAKE_ON);
            if (gamepad1.dpad_right) intake.setPower(INTAKE_OFF);
            if (gamepad1.dpad_up) intake.setPower(INTAKE_REVERSE);

            // Reset everything if gp2 up
            if (gamepad2.dpad_up && !fsmActive) {
                elevatorLeft.setPosition(ELEVATOR_LEFT_UP);
                elevatorRight.setPosition(ELEVATOR_RIGHT_UP);
                spindexer.setPosition(SPINDEXER_INTAKE);
                kicker.setPosition(KICKER_OUT);
                turret.setPosition(TURRET_INITIAL);
                hoodLeft.setPosition(HOOD_INITIAL);
                hoodRight.setPosition(HOOD_INITIAL);
                intake.setPower(INTAKE_OFF);
                shooterLeft.setPower(SHOOTER_OFF);
                shooterRight.setPower(SHOOTER_OFF);
            }

            // ===== FSM CONTROL =====
            if (!fsmActive) {
                if (gamepad1.a) { fsmActive = true; fsmState = FSMState.START; fsmBall = 1; fsmTimer.reset(); }
                if (gamepad1.b) { fsmActive = true; fsmState = FSMState.START; fsmBall = 2; fsmTimer.reset(); }
                if (gamepad1.x) { fsmActive = true; fsmState = FSMState.START; fsmBall = 3; fsmTimer.reset(); }
            }

            if (fsmActive) {
                switch (fsmState) {
                    case START:
                        if (fsmBall == 1) spindexer.setPosition(SPINDEXER_ONE);
                        if (fsmBall == 2) spindexer.setPosition(SPINDEXER_TWO);
                        if (fsmBall == 3) spindexer.setPosition(SPINDEXER_THREE);
                        if (fsmTimer.milliseconds() > 500) { fsmState = FSMState.ELEVATOR_RIGHT_DOWN; fsmTimer.reset(); }
                        break;
                    case ELEVATOR_RIGHT_DOWN:
                        elevatorRight.setPosition(ELEVATOR_RIGHT_DOWN);
                        if (fsmTimer.milliseconds() > 500) { fsmState = FSMState.ELEVATOR_LEFT_DOWN; fsmTimer.reset(); }
                        break;
                    case ELEVATOR_LEFT_DOWN:
                        elevatorLeft.setPosition(ELEVATOR_LEFT_DOWN);
                        if (fsmTimer.milliseconds() > 500) { fsmState = FSMState.ELEVATORS_UP; fsmTimer.reset(); }
                        break;
                    case ELEVATORS_UP:
                        elevatorLeft.setPosition(ELEVATOR_LEFT_UP);
                        elevatorRight.setPosition(ELEVATOR_RIGHT_UP);
                        if (fsmTimer.milliseconds() > 500) { fsmState = FSMState.SHOOTER_ON; fsmTimer.reset(); }
                        break;
                    case SHOOTER_ON:
                        shooterLeft.setPower(SHOOTER_ON);
                        shooterRight.setPower(SHOOTER_ON);
                        if (fsmTimer.milliseconds() > 500) { fsmState = FSMState.KICKER_IN; fsmTimer.reset(); }
                        break;
                    case KICKER_IN:
                        kicker.setPosition(KICKER_IN);
                        if (fsmTimer.milliseconds() > 500) { fsmState = FSMState.KICKER_OUT; fsmTimer.reset(); }
                        break;
                    case KICKER_OUT:
                        kicker.setPosition(KICKER_OUT);
                        if (fsmTimer.milliseconds() > 500 ) { fsmState = FSMState.SHOOTER_OFF; fsmTimer.reset(); }
                        break;
                    case SHOOTER_OFF:
                        shooterLeft.setPower(SHOOTER_OFF);
                        shooterRight.setPower(SHOOTER_OFF);
                        fsmActive = false;
                        fsmState = FSMState.IDLE;
                        break;
                }
            }

            // ===== TELEMETRY =====
            telemetry.addData("FSM Active", fsmActive);
            telemetry.addData("FSM State", fsmState);
            telemetry.addData("Turret", turret.getPosition());
            telemetry.addData("Hood L", hoodLeft.getPosition());
            telemetry.addData("Hood R", hoodRight.getPosition());
            telemetry.addData("Elevator L", elevatorLeft.getPosition());
            telemetry.addData("Elevator R", elevatorRight.getPosition());
            telemetry.addData("Kicker", kicker.getPosition());
            telemetry.addData("Spindexer", spindexer.getPosition());
            telemetry.addData("Intake", intake.getPower());
            telemetry.addData("Shooter L", shooterLeft.getPower());
            telemetry.addData("Shooter R", shooterRight.getPower());
            telemetry.update();
        }
    }
}
