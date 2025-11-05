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

        // ===== MECHANISM =====
        Servo elevatorLeft = hardwareMap.get(Servo.class, "el");
        Servo elevatorRight = hardwareMap.get(Servo.class, "er");
        Servo turret = hardwareMap.get(Servo.class, "tur");
        Servo hoodLeft = hardwareMap.get(Servo.class, "hl");
        Servo hoodRight = hardwareMap.get(Servo.class, "hr");
        Servo spindexer = hardwareMap.get(Servo.class, "spin");

        DcMotor intake = hardwareMap.get(DcMotor.class, "in");
        DcMotor intake2 = hardwareMap.get(DcMotor.class, "in2");
        DcMotor shooterLeft = hardwareMap.get(DcMotor.class, "sl");
        DcMotor shooterRight = hardwareMap.get(DcMotor.class, "sr");

        intake2.setDirection(DcMotorSimple.Direction.REVERSE);

        // ===== INITIAL POSITIONS =====
        elevatorLeft.setPosition(RobotHardware.ELEVATOR_LEFT_UP);
        elevatorRight.setPosition(RobotHardware.ELEVATOR_RIGHT_UP);
        spindexer.setPosition(RobotHardware.SPINDEXER_ONE);
        turret.setPosition(RobotHardware.TURRET_INITIAL);
        hoodLeft.setPosition(RobotHardware.HOOD_INITIAL);
        hoodRight.setPosition(RobotHardware.HOOD_INITIAL);
        intake.setPower(RobotHardware.INTAKE_OFF);
        intake2.setPower(RobotHardware.INTAKE_OFF);
        shooterLeft.setPower(RobotHardware.SHOOTER_OFF);
        shooterRight.setPower(RobotHardware.SHOOTER_OFF);



        ShooterFSM shooterFSM = new ShooterFSM(elevatorLeft, elevatorRight, spindexer,
                shooterLeft, shooterRight);

        double hoodPos = RobotHardware.HOOD_INITIAL;
        double turretPos = RobotHardware.TURRET_INITIAL;

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

            // ===== HOOD & TURRET (D-Pad 2) =====
            if (gamepad1.dpad_up) hoodPos += (1.0 / 355.0);
            else if (gamepad1.dpad_down) hoodPos -= (1.0 / 355.0);

            if (gamepad1.dpad_left) turretPos -= (2.0 / 355.0);
            else if (gamepad1.dpad_right) turretPos += (2.0 / 355.0);

            hoodPos = Math.max(0.0, Math.min(1.0, hoodPos));
            turretPos = Math.max(0.0, Math.min(1.0, turretPos));

            hoodLeft.setPosition(hoodPos);
            hoodRight.setPosition(hoodPos);
            turret.setPosition(turretPos);

            // ===== INTAKE =====
            if (gamepad1.right_bumper) {
                intake.setPower(RobotHardware.INTAKE_ON);
                intake2.setPower(RobotHardware.INTAKE_ON);
            } else if (gamepad1.left_bumper) {
                intake.setPower(RobotHardware.INTAKE_OFF);
                intake2.setPower(RobotHardware.INTAKE_OFF);
            } else if (gamepad1.right_trigger > 0) {
                intake.setPower(RobotHardware.INTAKE_REVERSE);
                intake2.setPower(RobotHardware.INTAKE_REVERSE);
            }

            // ===== FSM CONTROL =====
            if (!shooterFSM.isActive()) {
                if (gamepad1.a) shooterFSM.startFSM(1, false);
                if (gamepad1.b) shooterFSM.startFSM(2, false);
                if (gamepad1.x) shooterFSM.startFSM(3, false);
                if (gamepad1.y) shooterFSM.startFSM(1, true); // Multi-ball, turns shooter on/off
            }

            shooterFSM.updateFSM();

            // ===== TELEMETRY =====
            telemetry.addData("FSM Active", shooterFSM.isActive());
            telemetry.addData("FSM State", shooterFSM.getState());
            telemetry.addData("FSM Ball", shooterFSM.getBall());
            telemetry.addData("Spindexer", spindexer.getPosition());
            telemetry.addData("Turret", turretPos);
            telemetry.addData("Hood", hoodPos);
            telemetry.update();
        }
    }
}
