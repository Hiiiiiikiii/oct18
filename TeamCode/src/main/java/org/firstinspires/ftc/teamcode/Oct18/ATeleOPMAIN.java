package org.firstinspires.ftc.teamcode.Oct18;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ATeleOPMAIN", group = "Main")
public class ATeleOPMAIN extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        // ===== DRIVETRAIN =====
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("fl");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("bl");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("fr");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("br");

        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // ===== CONSTANTS =====
        double hoodStepFine = 1.0 / 355.0;
        double turretStepFine = 5.0 / 355.0;     // Turret tester step
        double TURRET_INITIAL = 177.5 / 355.0;   // Updated initial

        // ===== MECHANISM =====
        Servo kicker = hardwareMap.get(Servo.class, "kick");
        Servo elevatorLeft = hardwareMap.get(Servo.class, "el");
        Servo elevatorRight = hardwareMap.get(Servo.class, "er");
        Servo turret = hardwareMap.get(Servo.class, "tur");
        Servo hoodLeft = hardwareMap.get(Servo.class, "hl");
        Servo hoodRight = hardwareMap.get(Servo.class, "hr");
        Servo spindexer = hardwareMap.get(Servo.class, "spin");

        DcMotor intake = hardwareMap.get(DcMotor.class, "in");
        DcMotor shooterLeft = hardwareMap.get(DcMotor.class, "sl");
        DcMotor shooterRight = hardwareMap.get(DcMotor.class, "sr");

        // ===== INITIAL POSITIONS =====
        elevatorLeft.setPosition(RobotConstants.ELEVATOR_LEFT_UP);
        elevatorRight.setPosition(RobotConstants.ELEVATOR_RIGHT_UP);
        spindexer.setPosition(RobotConstants.SPINDEXER_INTAKE);
        kicker.setPosition(RobotConstants.KICKER_OUT);
        turret.setPosition(TURRET_INITIAL);
        hoodLeft.setPosition(RobotConstants.HOOD_INITIAL);
        hoodRight.setPosition(RobotConstants.HOOD_INITIAL);
        intake.setPower(RobotConstants.INTAKE_OFF);
        shooterLeft.setPower(RobotConstants.SHOOTER_OFF);
        shooterRight.setPower(RobotConstants.SHOOTER_OFF);

        double turretPos = TURRET_INITIAL;  // Track turret position

        // ===== CONTROLLERS =====
        HoodTurretController hoodTurretController = new HoodTurretController(turret, hoodLeft, hoodRight);
        ShooterFSM shooterFSM = new ShooterFSM(elevatorLeft, elevatorRight, spindexer, kicker,
                shooterLeft, shooterRight);

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {

            // ===== DRIVE =====
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x * 1.1;
            double rx = gamepad1.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);

            // ===== HOOD & TURRET CONTROLLER =====
            hoodTurretController.update(gamepad1, gamepad2);    

            // ===== SPINDEXER SECONDARY POSITIONS =====
            if (gamepad2.a) spindexer.setPosition(245.0 / 355.0); // Intake2
            if (gamepad2.b) spindexer.setPosition(RobotConstants.SPINDEXER_TWO);
            if (gamepad2.x) spindexer.setPosition(RobotConstants.SPINDEXER_THREE);

            // ===== INTAKE =====
            if (gamepad1.dpad_left) intake.setPower(RobotConstants.INTAKE_ON);
            if (gamepad1.dpad_right) intake.setPower(RobotConstants.INTAKE_OFF);
            if (gamepad1.dpad_up) intake.setPower(RobotConstants.INTAKE_REVERSE);

            // ===== RESET =====
            if (gamepad1.dpad_down && !shooterFSM.isActive()) {
                elevatorLeft.setPosition(RobotConstants.ELEVATOR_LEFT_UP);
                elevatorRight.setPosition(RobotConstants.ELEVATOR_RIGHT_UP);
                spindexer.setPosition(RobotConstants.SPINDEXER_INTAKE);
                kicker.setPosition(RobotConstants.KICKER_OUT);
                turret.setPosition(TURRET_INITIAL);
                hoodLeft.setPosition(RobotConstants.HOOD_INITIAL);
                hoodRight.setPosition(RobotConstants.HOOD_INITIAL);
                intake.setPower(RobotConstants.INTAKE_OFF);
                shooterLeft.setPower(RobotConstants.SHOOTER_OFF);
                shooterRight.setPower(RobotConstants.SHOOTER_OFF);
                turretPos = TURRET_INITIAL;
            }

            // ===== FSM BUTTONS =====
            if (!shooterFSM.isActive()) {
                if (gamepad1.a) shooterFSM.startFSM(1, false);
                if (gamepad1.b) shooterFSM.startFSM(2, false);
                if (gamepad1.x) shooterFSM.startFSM(3, false);
                if (gamepad1.y) shooterFSM.startFSM(1, true);
            }

            // ===== FSM UPDATE =====
            shooterFSM.updateFSM();

            // ===== TELEMETRY =====
            telemetry.addData("Turret Position", turretPos);
            telemetry.addData("Hood L", hoodLeft.getPosition());
            telemetry.addData("Hood R", hoodRight.getPosition());
            telemetry.addData("FSM Active", shooterFSM.isActive());
            telemetry.addData("FSM State", shooterFSM.getState());
            telemetry.addData("FSM Ball", shooterFSM.getBall());
            telemetry.addData("Spindexer", spindexer.getPosition());
            telemetry.addData("Intake", intake.getPower());
            telemetry.addData("Shooter L", shooterLeft.getPower());
            telemetry.addData("Shooter R", shooterRight.getPower());
            telemetry.update();
        }
    }
}
