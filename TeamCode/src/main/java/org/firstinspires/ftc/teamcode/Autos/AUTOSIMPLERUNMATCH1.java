package org.firstinspires.ftc.teamcode.Autos;


import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.teamcode.Oct18.HoodTurretController;
import org.firstinspires.ftc.teamcode.Oct18.RobotConstants;
import org.firstinspires.ftc.teamcode.Oct18.ShooterFSM;

import org.firstinspires.ftc.teamcode.Autos.simplifiedColorSensor;

import java.util.concurrent.TimeUnit;

@Autonomous
public class AUTOSIMPLERUNMATCH1 extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        // ===== DRIVETRAIN =====
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("fl");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("bl");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("fr");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("br");

        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);

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
        turret.setPosition(RobotConstants.TURRET_INITIAL);
        hoodLeft.setPosition(RobotConstants.HOOD_INITIAL);
        hoodRight.setPosition(RobotConstants.HOOD_INITIAL);
        intake.setPower(RobotConstants.INTAKE_OFF);
        shooterLeft.setPower(RobotConstants.SHOOTER_OFF);
        shooterRight.setPower(RobotConstants.SHOOTER_OFF);

        // ===== CONTROLLERS =====
        HoodTurretController hoodTurretController = new HoodTurretController(turret, hoodLeft, hoodRight);
        ShooterFSM shooterFSM = new ShooterFSM(elevatorLeft, elevatorRight, spindexer, kicker,
                shooterLeft, shooterRight);

        // ===== INIT ROBOT =====
        MotorPowerFunctions robot = new MotorPowerFunctions();

        // ===== COLOR SENSE DETECTION =====
        simplifiedColorSensor color = new simplifiedColorSensor();


        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {

           shooterFSM.startFSM(2, false); //purple (right)
           shooterFSM.startFSM(3, false); //purple (left)
           shooterFSM.startFSM(1, false); //green (center)

        }
    }
}
