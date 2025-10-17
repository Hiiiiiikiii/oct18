package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;

public class HoodTurretController {

    private final Servo turret, hoodLeft, hoodRight;
    private double turretPos, hoodLeftPos, hoodRightPos;

    // Steps for manual movement
    private final double hoodStep = 5.0 / 355.0;
    private final double turretStep = 0.05;

    public HoodTurretController(Servo turret, Servo hoodLeft, Servo hoodRight) {
        this.turret = turret;
        this.hoodLeft = hoodLeft;
        this.hoodRight = hoodRight;
        this.turretPos = RobotConstants.TURRET_INITIAL;
        this.hoodLeftPos = RobotConstants.HOOD_INITIAL;
        this.hoodRightPos = RobotConstants.HOOD_INITIAL;
    }

    public void update(Gamepad gamepad) {
        // Hood
        if (gamepad.left_trigger > 0.2) {
            hoodLeftPos -= hoodStep;
            hoodRightPos -= hoodStep;
        }
        if (gamepad.right_trigger > 0.2) {
            hoodLeftPos += hoodStep;
            hoodRightPos += hoodStep;
        }
        hoodLeftPos = clamp(hoodLeftPos);
        hoodRightPos = clamp(hoodRightPos);
        hoodLeft.setPosition(hoodLeftPos);
        hoodRight.setPosition(hoodRightPos);

        // Turret
        if (gamepad.left_bumper) turretPos -= turretStep;
        if (gamepad.right_bumper) turretPos += turretStep;
        turretPos = clamp(turretPos);
        turret.setPosition(turretPos);
    }

    private double clamp(double val) {
        return Math.max(0, Math.min(1, val));
    }
}
