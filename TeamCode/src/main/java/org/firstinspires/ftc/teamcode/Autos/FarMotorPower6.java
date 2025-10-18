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
import org.firstinspires.ftc.teamcode.Autos.MotorPowerFunctions;
import org.firstinspires.ftc.teamcode.Oct18.RobotHardware;

import java.util.concurrent.TimeUnit;

@Autonomous
public class FarMotorPower6 extends LinearOpMode {

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

        // ===== HUSKY LENS INIT =====
        final int READ_PERIOD = 2;
        HuskyLens huskyLens;
        huskyLens = hardwareMap.get(HuskyLens.class, "huskylens");

        Deadline rateLimit = new Deadline(READ_PERIOD, TimeUnit.SECONDS);
        rateLimit.expire();
        if (!huskyLens.knock()) {
            telemetry.addData(">>", "Problem communicating with HUSKYLENS");
        } else {
            telemetry.addData(">>", "Press start to continue");
        }
        huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);
        int ID = 21;

        telemetry.update();


        // ===== INIT ROBOT =====
        MotorPowerFunctions robot = new MotorPowerFunctions();



        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {

            if (!rateLimit.hasExpired()) {
                continue;
            }
            rateLimit.reset();


            HuskyLens.Block[] blocks = huskyLens.blocks();
            telemetry.addData("Block count", blocks.length);
            for (int i = 0; i < blocks.length; i++) {
                telemetry.addData("Block", blocks[i].toString());
                ID = blocks[i].id;
                telemetry.update();


                if (ID == 21) {
                    shooterFSM.startFSM(1, false); //green
                    shooterFSM.startFSM(2, false); //purple
                    shooterFSM.startFSM(3, false); //purple
                    intake.setPower(1);
                    //run drive
                    intake.setPower(0);
                    //change this to based on color sensor detection
                    shooterFSM.startFSM(1, false); //green
                    shooterFSM.startFSM(2, false); //purple
                    shooterFSM.startFSM(3, false); //purple
                } else if (ID == 22) {
                    shooterFSM.startFSM(2, false); //purple
                    shooterFSM.startFSM(1, false); //green
                    shooterFSM.startFSM(3, false); //purple
                    intake.setPower(1);
                    //run drive
                    intake.setPower(0);
                    //change this to based on color sensor detection
                    shooterFSM.startFSM(2, false); //green
                    shooterFSM.startFSM(1, false); //purple
                    shooterFSM.startFSM(3, false); //purple
                } else {
                    shooterFSM.startFSM(2, false); //purple
                    shooterFSM.startFSM(3, false); //purple
                    shooterFSM.startFSM(1, false); //green
                    intake.setPower(1);
                    //run drive
                    intake.setPower(0);
                    //change this to based on color sensor detection
                    shooterFSM.startFSM(2, false); //green
                    shooterFSM.startFSM(3, false); //purple
                    shooterFSM.startFSM(1, false); //purple



                }
            }
        }
    }
}
