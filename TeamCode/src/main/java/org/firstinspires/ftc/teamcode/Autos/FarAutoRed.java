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
@Autonomous(name = "FarAutoRED", group = "Autonomous")
public class FarAutoRed extends AutonFunctions {

    @Override
    public void runOpMode() {

        Pose2d initPose = new Pose2d(58.7, 12.1, Math.toRadians(180));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initPose);

        Vector2d START = new Vector2d(58.7, 12.1);


        Action goToFirstPosition = drive.actionBuilder(initPose)
                .waitSeconds(6)
                .splineToConstantHeading(new Vector2d(35, 58), Math.toRadians(90))
                .waitSeconds(3)
                .build();

        Action returnToStart1 = drive.actionBuilder(new Pose2d(35, 58, Math.toRadians(180)))
                .strafeTo(START)
                .waitSeconds(6)
                .build();

        Action goToSecondPosition = drive.actionBuilder(new Pose2d(START, Math.toRadians(180)))
                .splineToConstantHeading(new Vector2d(11.3, 53), Math.toRadians(90))
                .waitSeconds(2)
                .build();

        Action smallStrafe = drive.actionBuilder(
                        new Pose2d(new Vector2d(11.3, 53), Math.toRadians(180)))
                .strafeTo(new Vector2d(2, 56))
                .waitSeconds(2)
                .build();

        Action returnToStart2 = drive.actionBuilder(
                        new Pose2d(new Vector2d(2, 56), Math.toRadians(180)))
                .strafeTo(START)
                .build();


        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        goToFirstPosition,
                        returnToStart1,
                        goToSecondPosition,
                        smallStrafe,
                        returnToStart2
                )
        );
    }
}
