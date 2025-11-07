package org.firstinspires.ftc.teamcode.Autos;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Oct18.ShooterFSM;
import org.firstinspires.ftc.teamcode.tuning.MecanumDrive;

@Config
@Autonomous(name = "auton", group = "Autonomous")
public class auton extends AutonFunctions{

    @Override
    public void runOpMode() {
        Pose2d initPose = new Pose2d(-52, 52, Math.toRadians(135));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initPose);
        simplifiedColorSensor color = new simplifiedColorSensor();
        int greenLocation = color.colorSense();
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

        ShooterFSM shooterFSM = new ShooterFSM(elevatorLeft, elevatorRight, spindexer, shooterLeft, shooterRight);

        Action scorePreload = drive.actionBuilder(initPose)
                .setTangent(Math.toRadians(-45))
                .splineToConstantHeading(new Vector2d(-20, 20), Math.toRadians(-45))
                // shoot
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
                //shoot
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
                //shoot
                .waitSeconds(2)
                .build();

        Action park = drive.actionBuilder(new Pose2d(-20, 20, Math.toRadians(135)))
                .strafeTo(new Vector2d(-60, 30))
                .build();


        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        shooterFSM.runFSMAction(greenLocation, false),
                        shooterFSM.runFSMAction(purpleLocation1, false),
                        shooterFSM.runFSMAction(purpleLocation2, false),
                        scorePreload,
                        intakeFirstSet,
                        goToScoreFirstSet,
                        intakeSecondSet,
                        goToScoreSecondSet,
                        park
                )
        );
    }
}
