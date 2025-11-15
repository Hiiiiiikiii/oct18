package org.firstinspires.ftc.teamcode.UNESSESARY;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Oct18.RobotHardware;

@Autonomous(name = "LeaveZoneAuto", group = "Auto")
public class LeaveZoneAuto extends LinearOpMode {

    // ===== MOVEMENT HELPER =====
    private void drive(DcMotor fl, DcMotor bl, DcMotor fr, DcMotor br,
                       double flp, double blp, double frp, double brp, long timeMs) {
        fl.setPower(flp);
        bl.setPower(blp);
        fr.setPower(frp);
        br.setPower(brp);
        sleep(timeMs);
        fl.setPower(0);
        bl.setPower(0);
        fr.setPower(0);
        br.setPower(0);
    }

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

        // ===== MECHANISMS =====
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

        waitForStart();
        if (isStopRequested()) return;

        // ===== MOVE FORWARD TO LEAVE STARTING ZONE =====
        drive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor,
                0.5, 0.5, 0.5, 0.5, 600);

        // ===== STOP EVERYTHING =====
        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backRightMotor.setPower(0);
        shooterLeft.setPower(RobotHardware.SHOOTER_OFF);
        shooterRight.setPower(RobotHardware.SHOOTER_OFF);
        intake.setPower(RobotHardware.INTAKE_OFF);
        intake2.setPower(RobotHardware.INTAKE_OFF);
    }
}
