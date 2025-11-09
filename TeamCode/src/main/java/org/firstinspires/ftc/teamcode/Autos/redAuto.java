package org.firstinspires.ftc.teamcode.Autos;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Oct18.RobotHardware;
import org.firstinspires.ftc.teamcode.Oct18.ShooterAutoFSM;
import org.firstinspires.ftc.teamcode.tuning.MecanumDrive;

@Config
@Autonomous(name = "red lmao", group = "Autonomous")
public class redAuto extends AutonFunctions {

    @Override
    public void runOpMode() {
        Pose2d initPose = new Pose2d(-52, 52, Math.toRadians(135));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initPose);

        // ===== MECHANISM =====
        Servo elevatorLeft = hardwareMap.get(Servo.class, "el");
        Servo elevatorRight = hardwareMap.get(Servo.class, "er");
        Servo spindexer = hardwareMap.get(Servo.class, "spin");

        DcMotor intake = hardwareMap.get(DcMotor.class, "in");
        DcMotor intake2 = hardwareMap.get(DcMotor.class, "in2");
        DcMotor shooterLeft = hardwareMap.get(DcMotor.class, "sl");
        DcMotor shooterRight = hardwareMap.get(DcMotor.class, "sr");

        Servo hoodLeft = hardwareMap.get(Servo.class, "hl");
        Servo hoodRight = hardwareMap.get(Servo.class, "hr");

        intake2.setDirection(DcMotorSimple.Direction.REVERSE);

        ShooterAutoFSM shooterFSM = new ShooterAutoFSM(elevatorLeft, elevatorRight, spindexer, shooterLeft, shooterRight);
        elevatorLeft.setPosition(RobotHardware.ELEVATOR_LEFT_UP);
        elevatorRight.setPosition(RobotHardware.ELEVATOR_RIGHT_UP);
        spindexer.setPosition(RobotHardware.SPINDEXER_ONE);
        hoodLeft.setPosition(RobotHardware.HOOD_INITIAL);
        hoodRight.setPosition(RobotHardware.HOOD_INITIAL);


        Action intakeOn = (telemetryPacket) -> {
            intake.setPower(RobotHardware.INTAKE_ON);
            intake2.setPower(RobotHardware.INTAKE_ON);
            return false; // false = keep running continuously
        };

        Action scorePreload = drive.actionBuilder(initPose)
                .setTangent(Math.toRadians(-45))
                .splineToConstantHeading(new Vector2d(-20, 20), Math.toRadians(-45))
                .build();

        Action intakeFirstSet = drive.actionBuilder(new Pose2d(new Vector2d(-20, 20), initPose.heading))
                .splineToLinearHeading(new Pose2d(-15, 15, Math.toRadians(90)), Math.toRadians(90))
                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(-15, 58), Math.toRadians(90))
                .build();

        Action goToScoreFirstSet = drive.actionBuilder(new Pose2d(-15, 58, Math.toRadians(90)))
                .setTangent(Math.toRadians(-90))
                .splineToSplineHeading(new Pose2d(-15, 20, Math.toRadians(135)), Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(-20, 20, Math.toRadians(135)), Math.toRadians(135))
                .build();

        Action intakeSecondSet = drive.actionBuilder(new Pose2d(new Vector2d(-20, 20), Math.toRadians(135)))
                .setTangent(-45)
                .splineToLinearHeading(new Pose2d(12, 15, Math.toRadians(90)), Math.toRadians(90))
                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(12, 70), Math.toRadians(90), new TranslationalVelConstraint(40))
                .waitSeconds(0.1)
                .build();

        Action goToScoreSecondSet = drive.actionBuilder(new Pose2d(12, 70, Math.toRadians(90)))
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(-20, 20, Math.toRadians(135)), Math.toRadians(135))
                .build();

        Action park = drive.actionBuilder(new Pose2d(-20, 20, Math.toRadians(135)))
                .strafeTo(new Vector2d(-60, 30))
                .build();

        waitForStart();

        Actions.runBlocking(
                    new SequentialAction(

                        scorePreload,
                        shooterFSM.runFSMAction(1, true),

                        new ParallelAction(
                                intakeOn,
                                new SequentialAction(
                                        intakeFirstSet,
                                        goToScoreFirstSet
                                )
                        ),

                        shooterFSM.runFSMAction(1, true),

                        new ParallelAction(
                            intakeOn,
                            new SequentialAction(
                                intakeSecondSet,
                                goToScoreSecondSet
                            )
                        ),

                        shooterFSM.runFSMAction(1, true),
                        park
                    )
            );
        }
    }
