package org.firstinspires.ftc.teamcode.Autos;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ATeleOPMAIN;
import org.firstinspires.ftc.teamcode.AteleOpTest;

@Config
@Autonomous
public class RedFar6 extends ATeleOPMAIN {

    @Override
    public void runOpMode() {
        ATeleOPMAIN robot = new ATeleOPMAIN();
        Pose2d startPose = new Pose2d(58.7, 12.1, Math.toRadians(180));
        double timeAfterScore = 0.25;

        // vision here that outputs position

        Action scorePreload = drive.actionBuilder(initialPose)
                .stopAndAdd(new SequentialAction(robot.runFSM());
                .waitSeconds(5)
                .splineToConstantHeading(new Vector2d(9.9, 50), Math.toRadians(90))// this will pick up all three balls
                .waitSeconds(5)
                .splineToConstantHeading(new Vector2d(0, 56), Math.toRadians(90))// this moves to the gate
                .waitSeconds(5)
                .strafeTo(new Vector2d(0,36.4))
                .strafeTo(new Vector2d(58.7,12.1)) //this line will go to the start position again, and after it reaches wait 1 second and shoot the three balls that it intook
                .waitSeconds(5)
                .build();


        waitForStart();

        if (isStopRequested()) return;


        Actions.runBlocking(
                new SequentialAction(
                        scorePreload
                )
        );

    }
}