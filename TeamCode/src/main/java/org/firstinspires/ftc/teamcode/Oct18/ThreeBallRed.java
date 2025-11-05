package org.firstinspires.ftc.teamcode.Oct18;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "ThreeBallRed", group = "Auto")
public class ThreeBallRed extends LinearOpMode {

    // ===== PRESET POSITIONS (commented out for now) =====
    private static final double RED_POS1_HOOD = 0.4483;
    // private static final double RED_POS1_TURRET = 0.15;
    // private static final double RED_POS2_HOOD = 130.0 / 355.0;
    // private static final double RED_POS2_TURRET = 0.25;
    // private static final double RED_POS3_HOOD = 140.0 / 355.0;
    // private static final double RED_POS3_TURRET = 0.35;

    // ===== MOVEMENT HELPERS =====
    private void drive(DcMotor fl, DcMotor bl, DcMotor fr, DcMotor br,
                       double flp, double blp, double frp, double brp, long timeMs) {
        fl.setPower(flp);
        bl.setPower(blp);
        fr.setPower(frp);
        br.setPower(brp);
        sleep(timeMs);
        fl.setPower(0);
        bl.setPower(0);
        fr.setPower(0);
        br.setPower(0);
    }

    @Override
    public void runOpMode() throws InterruptedException {

        // ===== DRIVETRAIN =====
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("fl");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("bl");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("fr");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("br");

        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // ===== MECHANISMS =====
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

        // ===== INITIAL POSITIONS =====
        elevatorLeft.setPosition(RobotHardware.ELEVATOR_LEFT_UP);
        elevatorRight.setPosition(RobotHardware.ELEVATOR_RIGHT_UP);
        spindexer.setPosition(RobotHardware.SPINDEXER_ONE);
        // turret.setPosition(RED_POS1_TURRET);
        hoodLeft.setPosition(RED_POS1_HOOD);
        hoodRight.setPosition(RED_POS1_HOOD);
        intake.setPower(RobotHardware.INTAKE_OFF);
        intake2.setPower(RobotHardware.INTAKE_OFF);
        shooterLeft.setPower(RobotHardware.SHOOTER_OFF);
        shooterRight.setPower(RobotHardware.SHOOTER_OFF);

        ShooterFSM shooterFSM = new ShooterFSM(elevatorLeft, elevatorRight, spindexer,
                shooterLeft, shooterRight);

        waitForStart();
        if (isStopRequested()) return;
// ===== Turn on intake after 100 ms of match start =====
        sleep(100);
        intake.setPower(RobotHardware.INTAKE_ON);
        intake2.setPower(RobotHardware.INTAKE_ON);
        // ==== STAGE 1: Move back & shoot 3 balls ====
        drive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor,
                -0.7, -0.7, -0.7, -0.7, 900);

        shooterFSM.startFSM(1, true);
        while (opModeIsActive() && shooterFSM.isActive()) {
            shooterFSM.updateFSM();
        }
        spindexer.setPosition(RobotHardware.SPINDEXER_ONE);

        sleep(200);

        // ==== STAGE 2: Rotate & move for next set ====
        drive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor,
                0.5, 0.5, -0.5, -0.5, 220); // rotate
        drive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor,
                0.6, 0.6, 0.6, 0.6, 300); // forward


//        // Turn ON intake before first forward 800
//        intake.setPower(RobotHardware.INTAKE_ON);
//        intake2.setPower(RobotHardware.INTAKE_ON);
//        drive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor,
//                0.5, -0.5, -0.5, 0.5, 550); // move right
//        drive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor,
//                0.6, 0.6, 0.6, 0.6, 1700); // forward
//        intake.setPower(RobotHardware.INTAKE_ON);
//        intake2.setPower(RobotHardware.INTAKE_ON);
//        sleep(600);
//
//        drive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor,
//                -0.9, -0.9, -0.9, -0.9, 600); // backward
//        sleep(600);
//        // Turn OFF intake after move=
////        intake.setPower(RobotHardware.INTAKE_OFF);
////        intake2.setPower(RobotHardware.INTAKE_OFF);
//        drive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor,
//                -0.5, -0.5, 0.5, 0.5, 230); // rotate
//
//        // ==== STAGE 3: Set to position 2 & shoot 3 balls ====
//        // turret.setPosition(RED_POS2_TURRET);
//        // hoodLeft.setPosition(RED_POS2_HOOD);
//        // hoodRight.setPosition(RED_POS2_HOOD);
//
//        shooterFSM.startFSM(1, true);
//        while (opModeIsActive() && shooterFSM.isActive()) {
//            shooterFSM.updateFSM();
//        }
////        spindexer.setPosition(RobotHardware.SPINDEXER_ONE);
////
////        // ==== STAGE 4: Move to next position ====
////        drive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor,
////                0.5, -0.5, -0.5, 0.5, 500); // move right
////
////        // Turn ON intake before second forward 800
////        intake.setPower(RobotHardware.INTAKE_ON);
////        intake2.setPower(RobotHardware.INTAKE_ON);
////        drive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor,
////                0.5, 0.5, 0.5, 0.5, 800); // forward
////        // Turn OFF intake after move
////        intake.setPower(RobotHardware.INTAKE_OFF);
////        intake2.setPower(RobotHardware.INTAKE_OFF);
////
////        drive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor,
////                -0.5, -0.5, -0.5, -0.5, 200); // backward
////        drive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor,
////                -0.5, 0.5, 0.5, -0.5, 100); // move left
////        drive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor,
////                0.5, 0.5, 0.5, 0.5, 200); // forward
////
////        sleep(3000); // wait 3 seconds
////
////        drive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor,
////                -0.5, -0.5, -0.5, -0.5, 1000); // backward
////        drive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor,
////                -0.5, 0.5, 0.5, -0.5, 200); // move left
////
////        // ==== STAGE 5: Set to position 3 & shoot 3 balls ====
////        // turret.setPosition(RED_POS3_TURRET);
////        // hoodLeft.setPosition(RED_POS3_HOOD);
////        // hoodRight.setPosition(RED_POS3_HOOD);
////
////        shooterFSM.startFSM(1, true);
////        while (opModeIsActive() && shooterFSM.isActive()) {
////            shooterFSM.updateFSM();
////        }
//      spindexer.setPosition(RobotHardware.SPINDEXER_ONE);

        // ==== Done ====
        shooterLeft.setPower(RobotHardware.SHOOTER_OFF);
        shooterRight.setPower(RobotHardware.SHOOTER_OFF);
        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backRightMotor.setPower(0);
    }
}
