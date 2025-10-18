package org.firstinspires.ftc.teamcode.Autos;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RobotHardware;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ShooterFSMAction;

@Config
@Autonomous(name = "RedFar6", group = "Auto")
public class RedFar6 extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        // ===== Initialize Robot Hardware =====
        RobotHardware robot = new RobotHardware();
        robot.init(hardwareMap);

        // ===== Initialize Drive =====
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(58.7, 12.1, Math.toRadians(180)));

        // ===== FSM Shooter Action =====
        ShooterFSMAction shootPreload = new ShooterFSMAction(robot, 1, false);

        // ===== Build Full Auto Sequence =====
        SequentialAction fullAuto = (SequentialAction) drive.actionBuilder(new Pose2d(58.7, 12.1, Math.toRadians(180)))
                // --- Shoot preload ---
                .stopAndAdd(shootPreload)  // runs FSM action
                .waitSeconds(1.0)

                // --- Move to collect balls ---
                .splineToConstantHeading(new Vector2d(9.9, 50), Math.toRadians(90))
                .waitSeconds(0.5)

                // --- Move to gate ---
                .splineToConstantHeading(new Vector2d(0, 56), Math.toRadians(90))
                .waitSeconds(0.5)

                // --- Move back to start ---
                .strafeTo(new Vector2d(0, 36.4))
                .strafeTo(new Vector2d(58.7, 12.1))
                .waitSeconds(1.0)

                // --- Shoot all balls ---
                .stopAndAdd(new ShooterFSMAction(robot, 1, true))
                .build();


        telemetry.addLine("Ready - Press Play");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        // ===== Run the entire auto path + FSMs =====
        Actions.runBlocking(
                new SequentialAction(fullAuto)
        );
    }
}
