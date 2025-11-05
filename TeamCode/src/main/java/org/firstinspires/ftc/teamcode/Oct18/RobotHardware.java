package org.firstinspires.ftc.teamcode.Oct18;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class
RobotHardware {

    // ================= HARDWARE =================
    public Servo elevatorLeft, elevatorRight, turret, hoodLeft, hoodRight, spindexer;
    public DcMotor intakeLeft, intakeRight, shooterLeft, shooterRight;

    public HuskyLens huskyLens;

    // ===== ELEVATOR =====
    public static final double ELEVATOR_LEFT_UP = 214 / 355.0;
    public static final double ELEVATOR_LEFT_DOWN = 324 /355.0;
    public static final double ELEVATOR_RIGHT_UP = 214/ 355.0;
    public static final double ELEVATOR_RIGHT_DOWN = 324 / 355.0;

    // ===== SPINDEXER =====
        public static final double SPINDEXER_ONE = 1 / 355.0;
    public static final double SPINDEXER_TWO = 137.0 / 355.0;
    public static final double SPINDEXER_THREE = 267.0 / 355.0;

    // ===== INTAKE =====
    public static final double INTAKE_OFF = 0.0;
    public static final double INTAKE_ON = 1.0;
    public static final double INTAKE_REVERSE = -1.0;

    // ===== SHOOTER =====
    public static final double SHOOTER_ON = 1.0;
    public static final double SHOOTER_OFF = 0.0;

    // ===== TURRET =====
    public static final double TURRET_INITIAL = 0.6943;
    public static final double TURRET_STEP = 5.0 / 355.0;
    public static final double TURRET_RED_AUTO = 0.15;
    public static final double TURRET_BLUE_AUTO = 0.85;

    // ===== HOOD =====
    public static final double HOOD_INITIAL = 0.4483;
    public static final double HOOD_STEP = 1.0 / 355.0;
    public static final double HOOD_FAR = 120 / 355.0;
    public static final double HOOD_NEAR = 99.4 / 355.0;

    // ================= INIT METHOD =================
    public void init(HardwareMap hardwareMap) {
        elevatorLeft = hardwareMap.get(Servo.class, "el");
        elevatorRight = hardwareMap.get(Servo.class, "er");
        turret = hardwareMap.get(Servo.class, "tur");
        hoodLeft = hardwareMap.get(Servo.class, "hl");
        hoodRight = hardwareMap.get(Servo.class, "hr");
        spindexer = hardwareMap.get(Servo.class, "spin");

        intakeLeft = hardwareMap.get(DcMotor.class, "inl");
        intakeRight = hardwareMap.get(DcMotor.class, "inr");
        shooterLeft = hardwareMap.get(DcMotor.class, "sl");
        shooterRight = hardwareMap.get(DcMotor.class, "sr");

        shooterLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        huskyLens = hardwareMap.get(HuskyLens.class, "husky");

        // Set initial positions and powers
        elevatorLeft.setPosition(ELEVATOR_LEFT_UP);
        elevatorRight.setPosition(ELEVATOR_RIGHT_UP);
        spindexer.setPosition(SPINDEXER_ONE);
        turret.setPosition(TURRET_INITIAL);
        hoodLeft.setPosition(HOOD_INITIAL);
        hoodRight.setPosition(HOOD_INITIAL);
        intakeLeft.setPower(INTAKE_OFF);
        intakeRight.setPower(INTAKE_OFF);
        shooterLeft.setPower(SHOOTER_OFF);
        shooterRight.setPower(SHOOTER_OFF);
    }
}
