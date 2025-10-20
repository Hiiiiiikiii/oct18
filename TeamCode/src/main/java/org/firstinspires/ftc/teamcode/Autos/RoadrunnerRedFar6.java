package org.firstinspires.ftc.teamcode.Autos;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Oct18.RobotHardware;
import org.firstinspires.ftc.teamcode.tuning.MecanumDrive;
import org.firstinspires.ftc.teamcode.ShooterFSMAction;

@Config
@Autonomous(name = "RedFar6", group = "Auto")
public class RoadrunnerRedFar6 extends LinearOpMode {

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
                .stopAndAdd(shootPreload)  // shoot preload
                .waitSeconds(1.0)
                .splineToConstantHeading(new Vector2d(9.9, 50), Math.toRadians(90)) // collect balls
                .waitSeconds(0.5)
                .splineToConstantHeading(new Vector2d(0, 56), Math.toRadians(90)) // move to gate
                .waitSeconds(0.5)
                .strafeTo(new Vector2d(0, 36.4))
                .strafeTo(new Vector2d(58.7, 12.1)) // back to start
                .waitSeconds(1.0)
                .stopAndAdd(new ShooterFSMAction(robot, 1, true)) // shoot all balls
                .build();

        // ===== Initialize HuskyLens =====
        if (!robot.huskyLens.knock()) {
            telemetry.addData("HuskyLens", "Communication problem!");
        } else {
            telemetry.addData("HuskyLens", "Ready - Press Play");
        }

        robot.huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        // ===== Read Tag Once =====
        int detectedID = 1; // default
        HuskyLens.Block[] blocks = robot.huskyLens.blocks();
        if (blocks != null && blocks.length > 0) {
            detectedID = blocks[0].id; // pick first detected block
            telemetry.addData("Detected Tag ID", detectedID);
        } else {
            telemetry.addData("Detected Tag ID", "None, using default ID=1");
        }
        telemetry.update();

        // ===== Run Auto Sequence Based on Tag =====
        Actions.runBlocking(fullAuto);

        telemetry.addLine("Autonomous Finished");
        telemetry.update();
    }
}
