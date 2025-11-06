package org.firstinspires.ftc.teamcode.Autos;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.tuning.MecanumDrive;

@Config
@Autonomous(name = "auton", group = "Autonomous")
public class auton extends AutonFunctions{

    @Override
    public void runOpMode() {
        Pose2d initPose = new Pose2d(-52, 52, Math.toRadians(135));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initPose);

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
