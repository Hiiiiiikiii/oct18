package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "MANUAL", group = "Competition")
public class Manual extends LinearOpMode {

    // ================== CONSTANTS ==================

    // Elevator
    public static final double ELEVATOR_LEFT_UP = 222.0 / 355.0;
    public static final double ELEVATOR_LEFT_DOWN = 330.0 / 355.0;
    public static final double ELEVATOR_RIGHT_UP = 220.0 / 355.0;
    public static final double ELEVATOR_RIGHT_DOWN = 330.0 / 355.0;

    // Spindexer
    public static final double SPINDEXER_THREE = 270.0 / 355.0;
    public static final double SPINDEXER_TWO = 130.0 / 355.0;
    public static final double SPINDEXER_ONE = 0.0 / 355.0;
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

    // Step for servo adjustments
    private final double step = 5.0 / 355.0;

    // ================== HARDWARE ==================
    private Servo kicker;
    private Servo elevatorLeft;
    private Servo elevatorRight;
    private Servo turret;
    private Servo hoodLeft;
    private Servo hoodRight;
    private Servo spindexer;

    private DcMotor intake;
    private DcMotor shooterLeft;
    private DcMotor shooterRight;

    private double turretPos = TURRET_INITIAL;
    private double hoodLeftPos = HOOD_INITIAL;
    private double hoodRightPos = HOOD_INITIAL;

    // ================== MAIN CODE ==================
    @Override
    public void runOpMode() {

        // --- Hardware Mapping ---
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

        // --- Initial Positions ---
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

        while (opModeIsActive()) {

            // ===== Gamepad 1 =====

            // --- Kicker ---
            if (gamepad1.a) {
                kicker.setPosition(KICKER_IN);
            }

            if (gamepad1.b) {
                kicker.setPosition(KICKER_OUT);
            }

            // --- Elevator ---
            if (gamepad1.x) {
                elevatorLeft.setPosition(ELEVATOR_LEFT_UP);
                elevatorRight.setPosition(ELEVATOR_RIGHT_UP);
            }

            if (gamepad1.y) {
                elevatorLeft.setPosition(ELEVATOR_LEFT_DOWN);
            }

            if (gamepad1.dpad_down) {
                elevatorRight.setPosition(ELEVATOR_RIGHT_DOWN);
            }

            // TURRET MOVEMENT - per click
            boolean prevDpadLeft = false;
            boolean prevDpadRight = false;
            if (gamepad1.dpad_left && !prevDpadLeft) {
                turretPos = turretPos - step;
            }
            if (gamepad1.dpad_right && !prevDpadRight) {
                turretPos = turretPos + step;
            }

// clamp
            if (turretPos < 0) turretPos = 0;
            if (turretPos > 1) turretPos = 1;

            turret.setPosition(turretPos);

// update previous state
            prevDpadLeft = gamepad1.dpad_left;
            prevDpadRight = gamepad1.dpad_right;

            // --- Intake controls ---
            if (gamepad1.left_bumper) {
                intake.setPower(INTAKE_REVERSE);
            }

            if (gamepad1.right_bumper) {
                intake.setPower(INTAKE_ON);
            }

            if (gamepad1.start) {
                intake.setPower(INTAKE_OFF);
            }

            // --- Shooter controls ---
            if (gamepad1.left_trigger > 0.2) {
                shooterLeft.setPower(SHOOTER_ON);
                shooterRight.setPower(SHOOTER_ON);
            }

            if (gamepad1.right_trigger > 0.2) {
                shooterLeft.setPower(SHOOTER_OFF);
                shooterRight.setPower(SHOOTER_OFF);
            }

            // ===== Gamepad 2 =====

            // --- Hood movement ---
            if (gamepad2.dpad_up) {
                hoodLeftPos = hoodLeftPos + step;
                hoodRightPos = hoodRightPos + step;
            }

            if (gamepad2.dpad_down) {
                hoodLeftPos = hoodLeftPos - step;
                hoodRightPos = hoodRightPos - step;
            }

            if (hoodLeftPos < 0) {
                hoodLeftPos = 0;
            }

            if (hoodLeftPos > 1) {
                hoodLeftPos = 1;
            }

            if (hoodRightPos < 0) {
                hoodRightPos = 0;
            }

            if (hoodRightPos > 1) {
                hoodRightPos = 1;
            }

            hoodLeft.setPosition(hoodLeftPos);
            hoodRight.setPosition(hoodRightPos);

            // --- Spindexer positions ---
            if (gamepad2.a) {
                spindexer.setPosition(SPINDEXER_ONE);
            }

            if (gamepad2.b) {
                spindexer.setPosition(SPINDEXER_TWO);
            }

            if (gamepad2.x) {
                spindexer.setPosition(SPINDEXER_THREE);
            }

            if (gamepad2.y) {
                spindexer.setPosition(SPINDEXER_INTAKE);
            }

            // ===== Telemetry =====
            telemetry.addLine("=== Servos ===");
            telemetry.addData("Kicker", kicker.getPosition());
            telemetry.addData("Elevator L", elevatorLeft.getPosition());
            telemetry.addData("Elevator R", elevatorRight.getPosition());
            telemetry.addData("Turret", turret.getPosition());
            telemetry.addData("Hood L", hoodLeft.getPosition());
            telemetry.addData("Hood R", hoodRight.getPosition());
            telemetry.addData("Spindexer", spindexer.getPosition());

            telemetry.addLine("=== Motors ===");
            telemetry.addData("Intake", intake.getPower());
            telemetry.addData("Shooter L", shooterLeft.getPower());
            telemetry.addData("Shooter R", shooterRight.getPower());
            telemetry.update();
        }
    }
}
