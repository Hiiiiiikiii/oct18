package org.firstinspires.ftc.teamcode.Autos;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.teamcode.Oct18.RobotHardware;
import org.firstinspires.ftc.teamcode.Oct18.ShooterAutoFSM;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.tuning.MecanumDrive;

import java.util.concurrent.TimeUnit;

@Config
@Autonomous(name = "auton", group = "Autonomous")
public class auton extends AutonFunctions {

    @Override
    public void runOpMode() {
        Pose2d initPose = new Pose2d(-52, 52, Math.toRadians(135));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initPose);

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

        RevColorSensorV3 colorLeft = hardwareMap.get(RevColorSensorV3.class, "cl");
        RevColorSensorV3 colorCenter = hardwareMap.get(RevColorSensorV3.class, "cc");
        RevColorSensorV3 colorRight = hardwareMap.get(RevColorSensorV3.class, "cr");

        NormalizedRGBA detectedLeft = colorLeft.getNormalizedColors();
        NormalizedRGBA detectedCenter = colorCenter.getNormalizedColors();
        NormalizedRGBA detectedRight = colorRight.getNormalizedColors();

        HuskyLens huskyLens = hardwareMap.get(HuskyLens.class, "husky");

        int greenLocation = 1;
        final int READ_PERIOD = 1;
        int id = 21;

        if (detectedLeft.blue < detectedLeft.green) {
            greenLocation = 3;
        }
        if (detectedCenter.blue < detectedCenter.green) {
            greenLocation = 1;
        }
        if (detectedRight.blue < detectedRight.green) {
            greenLocation = 2;
        }

        int purpleLocation1;
        int purpleLocation2;
        if (greenLocation == 1) {
            purpleLocation1 = 2;
            purpleLocation2 = 3;
        } else if (greenLocation == 2) {
            purpleLocation1 = 1;
            purpleLocation2 = 3;
        } else {
            purpleLocation1 = 1;
            purpleLocation2 = 2;
        }

        ShooterAutoFSM shooterFSM = new ShooterAutoFSM(elevatorLeft, elevatorRight, spindexer, shooterLeft, shooterRight);

        Action intakeOn = (telemetryPacket) -> {
            intake.setPower(RobotHardware.INTAKE_ON);
            intake2.setPower(RobotHardware.INTAKE_ON);
            return false; // false = keep running continuously
        };

        Action scorePreload = drive.actionBuilder(initPose)
                .setTangent(Math.toRadians(-45))
                .splineToConstantHeading(new Vector2d(-20, 20), Math.toRadians(-45))
                .waitSeconds(2)
                .build();

        Action intakeFirstSet = drive.actionBuilder(new Pose2d(new Vector2d(-20, 20), initPose.heading))
                .splineToLinearHeading(new Pose2d(-12, 20, Math.toRadians(90)), Math.toRadians(90))
                .setTangent(Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(-12, 58, Math.toRadians(90)), Math.toRadians(90))
                .waitSeconds(0.1)
                .build();

        Action goToScoreFirstSet = drive.actionBuilder(new Pose2d(-12, 58, Math.toRadians(90)))
                .setTangent(Math.toRadians(-90))
                .splineToSplineHeading(new Pose2d(-12, 20, Math.toRadians(135)), Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(-20, 20, Math.toRadians(135)), Math.toRadians(135))
                .waitSeconds(2)
                .build();

        Action intakeSecondSet = drive.actionBuilder(new Pose2d(new Vector2d(-20, 20), Math.toRadians(135)))
                .splineToLinearHeading(new Pose2d(12, 20, Math.toRadians(90)), Math.toRadians(90))
                .setTangent(Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(12, 58, Math.toRadians(90)), Math.toRadians(90))
                .waitSeconds(0.1)
                .build();

        Action goToScoreSecondSet = drive.actionBuilder(new Pose2d(12, 58, Math.toRadians(90)))
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(-20, 20, Math.toRadians(135)), Math.toRadians(135))
                .waitSeconds(2)
                .build();

        Action park = drive.actionBuilder(new Pose2d(-20, 20, Math.toRadians(135)))
                .strafeTo(new Vector2d(-60, 30))
                .build();


        Deadline rateLimit = new Deadline(READ_PERIOD, TimeUnit.SECONDS);

        rateLimit.expire();

        if (!huskyLens.knock()) {
            telemetry.addData(">>", "Problem communicating with " + huskyLens.getDeviceName());
        } else {
            telemetry.addData(">>", "Press start to continue");
        }

        huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);

        telemetry.update();
        waitForStart();

        while(opModeIsActive()) {
            if (!rateLimit.hasExpired()) {
                continue;
            }
            rateLimit.reset();

            HuskyLens.Block[] blocks = huskyLens.blocks();
            telemetry.addData("Block count", blocks.length);
            for (int i = 0; i < blocks.length; i++) {
                telemetry.addData("Block", blocks[i].toString());
                id = blocks[i].id;
            }
        }

        if (id == 21) {
            // Green Purple Purple
            Actions.runBlocking(
                    new SequentialAction(
                            intakeOn,
                            shooterFSM.runFSMAction(greenLocation, false),
                            shooterFSM.runFSMAction(purpleLocation1, false),
                            shooterFSM.runFSMAction(purpleLocation2, false),
                            scorePreload,
                            intakeFirstSet,
                            goToScoreFirstSet,
                            shooterFSM.runFSMAction(greenLocation, false),
                            shooterFSM.runFSMAction(purpleLocation1, false),
                            shooterFSM.runFSMAction(purpleLocation2, false),
                            intakeSecondSet,
                            goToScoreSecondSet,
                            shooterFSM.runFSMAction(greenLocation, false),
                            shooterFSM.runFSMAction(purpleLocation1, false),
                            shooterFSM.runFSMAction(purpleLocation2, false),
                            park
                    )
            );
        } else if (id == 22) {
            // Purple Green Purple
            Actions.runBlocking(
                    new SequentialAction(
                            intakeOn,
                            shooterFSM.runFSMAction(purpleLocation1, false),
                            shooterFSM.runFSMAction(greenLocation, false),
                            shooterFSM.runFSMAction(purpleLocation2, false),
                            scorePreload,
                            intakeFirstSet,
                            goToScoreFirstSet,
                            shooterFSM.runFSMAction(purpleLocation1, false),
                            shooterFSM.runFSMAction(greenLocation, false),
                            shooterFSM.runFSMAction(purpleLocation2, false),
                            intakeSecondSet,
                            goToScoreSecondSet,
                            shooterFSM.runFSMAction(purpleLocation1, false),
                            shooterFSM.runFSMAction(greenLocation, false),
                            shooterFSM.runFSMAction(purpleLocation2, false),
                            park
                    )
            );
        } else {
            // Purple Purple Green
            Actions.runBlocking(
                    new SequentialAction(
                            intakeOn,
                            shooterFSM.runFSMAction(purpleLocation1, false),
                            shooterFSM.runFSMAction(purpleLocation2, false),
                            shooterFSM.runFSMAction(greenLocation, false),
                            scorePreload,
                            intakeFirstSet,
                            goToScoreFirstSet,
                            shooterFSM.runFSMAction(purpleLocation1, false),
                            shooterFSM.runFSMAction(purpleLocation2, false),
                            shooterFSM.runFSMAction(greenLocation, false),
                            intakeSecondSet,
                            goToScoreSecondSet,
                            shooterFSM.runFSMAction(purpleLocation1, false),
                            shooterFSM.runFSMAction(purpleLocation2, false),
                            shooterFSM.runFSMAction(greenLocation, false),
                            park
                    )
            );
        }
    }
}
