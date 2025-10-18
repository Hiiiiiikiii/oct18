package org.firstinspires.ftc.teamcode.Autos;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Config
@Autonomous(name = "MovementTest", group = "Auto")
public class AUTO extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        // ===== Initialize Drive =====
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(58.7, 12.1, Math.toRadians(90)));

        // ===== Build Movement-Only Path =====
        SequentialAction movementPath = (SequentialAction) drive.actionBuilder(new Pose2d(58.7, 12.1, Math.toRadians(180)))
                .waitSeconds(5.0)  // simulate pause for start
                .splineToConstantHeading(new Vector2d(35, 58), Math.toRadians(90))
                .waitSeconds(5.0)
                .strafeTo(new Vector2d(58.7, 12.1))
                .waitSeconds(5.0)
                .build();

        waitForStart();
        if (isStopRequested()) return;

        // ===== Run Movement Path =====
        Actions.runBlocking(
                new SequentialAction(movementPath)
        );
    }
}
