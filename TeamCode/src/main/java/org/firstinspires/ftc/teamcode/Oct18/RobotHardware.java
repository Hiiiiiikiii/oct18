package org.firstinspires.ftc.teamcode.Oct18;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class RobotHardware {

    // ================= CONSTANTS =================
    // Kicker
    public static final double KICKER_IN = 215.0 / 355.0;
    public static final double KICKER_OUT = 286.0 / 355.0;

    // Elevator
    public static final double ELEVATOR_LEFT_UP = 222.0 / 355.0;
    public static final double ELEVATOR_LEFT_DOWN = 330.0 / 355.0;
    public static final double ELEVATOR_RIGHT_UP = 220.0 / 355.0;
    public static final double ELEVATOR_RIGHT_DOWN = 330.0 / 355.0;

    // Spindexer
    public static final double SPINDEXER_ONE = 0.0 / 355.0;
    public static final double SPINDEXER_TWO = 130.0 / 355.0;
    public static final double SPINDEXER_THREE = 270.0 / 355.0;
    public static final double SPINDEXER_INTAKE = 65.0 / 355.0;

    // Turret
    public static final double TURRET_INITIAL = 75.0 / 355.0;
    public static final double TURRET_STEP = 5.0 / 355.0;

    // Hood
    public static final double HOOD_INITIAL = 55.0 / 355.0; // 0.1549
    public static final double HOOD_STEP = 5.0 / 355.0;

    // Shooter
    public static final double SHOOTER_ON = 1.0;
    public static final double SHOOTER_OFF = 0.0;

    // Intake
    public static final double INTAKE_ON = 1.0;
    public static final double INTAKE_OFF = 0.0;
    public static final double INTAKE_REVERSE = -1.0;

    // ================= HARDWARE =================
    public Servo kicker, elevatorLeft, elevatorRight, turret, hoodLeft, hoodRight, spindexer;
    public DcMotor intake, shooterLeft, shooterRight;

    public HuskyLens huskyLens;

    // ================= INIT METHOD =================
    public void init(HardwareMap hardwareMap) {
        kicker = hardwareMap.get(Servo.class, "kick");
        elevatorLeft = hardwareMap.get(Servo.class, "el");
        elevatorRight = hardwareMap.get(Servo.class, "er");
        turret = hardwareMap.get(Servo.class, "tur");
        hoodLeft = hardwareMap.get(Servo.class, "hl");
        hoodRight = hardwareMap.get(Servo.class, "hr");
        spindexer = hardwareMap.get(Servo.class, "spin");

        intake = hardwareMap.get(DcMotor.class, "in");
        shooterLeft = hardwareMap.get(DcMotor.class, "sl");
        shooterRight = hardwareMap.get(DcMotor.class, "sr");

        huskyLens = hardwareMap.get(HuskyLens.class, "husky");



        // Set initial positions and powers
        elevatorLeft.setPosition(ELEVATOR_LEFT_UP);
        elevatorRight.setPosition(ELEVATOR_RIGHT_UP);
        spindexer.setPosition(SPINDEXER_INTAKE);
        kicker.setPosition(KICKER_OUT);
        turret.setPosition(TURRET_INITIAL);
        hoodLeft.setPosition(HOOD_INITIAL);
        hoodRight.setPosition(HOOD_INITIAL);
        intake.setPower(INTAKE_OFF);
        shooterLeft.setPower(SHOOTER_OFF);
        shooterRight.setPower(SHOOTER_OFF);


    }
}
