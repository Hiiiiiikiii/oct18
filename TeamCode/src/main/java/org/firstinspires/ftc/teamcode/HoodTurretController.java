package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Oct18.RobotHardware;

public class HoodTurretController {

    private final Servo turret, hoodLeft, hoodRight;
    private double turretPos;

    private final double turretStep = 5.0 / 355.0;
    private final double hoodStepFine = 1.0 / 355.0;

    public HoodTurretController(Servo turret, Servo hoodLeft, Servo hoodRight) {
        this.turret = turret;
        this.hoodLeft = hoodLeft;
        this.hoodRight = hoodRight;
        this.turretPos = RobotHardware.TURRET_INITIAL;

        // Set initial positions
        this.turret.setPosition(turretPos);
        this.hoodLeft.setPosition(RobotHardware.HOOD_INITIAL);
        this.hoodRight.setPosition(RobotHardware.HOOD_INITIAL);
    }

    /**
     * Updates the hood and turret based on inputs from both gamepads.
     * @param gamepad1 The primary driver gamepad.
     * @param gamepad2 The secondary operator gamepad.
     */
    public void update(Gamepad gamepad1, Gamepad gamepad2) {
        // ===== HOOD PRESETS =====
       // if (gamepad1.left_bumper) {
            // Near position
           // hoodLeft.setPosition(RobotConstants.HOOD_NEAR);
            //hoodRight.setPosition(RobotConstants.HOOD_NEAR);
        //} else if (gamepad1.right_bumper) {
            // Far position
          //  hoodLeft.setPosition(RobotConstants.HOOD_FAR);
            //hoodRight.setPosition(RobotConstants.HOOD_FAR);
       // }
        //

        // ===== TURRET MANUAL (bumpers on gamepad1) =====
        if (gamepad1.left_trigger > .2) turretPos -= turretStep;
        if (gamepad1.right_trigger > .2) turretPos += turretStep;
        turretPos = clamp(turretPos);
        turret.setPosition(turretPos);


        // ===== HOOD TESTER (gamepad2 D-pad up/down) =====
        if (gamepad2.right_bumper) {
            double newHoodPos = hoodLeft.getPosition() + hoodStepFine;
            hoodLeft.setPosition(clamp(newHoodPos));
            hoodRight.setPosition(clamp(newHoodPos));

        } else if (gamepad2.left_bumper) {
            double newHoodPos = hoodLeft.getPosition() - hoodStepFine;
            hoodLeft.setPosition(clamp(newHoodPos));
            hoodRight.setPosition(clamp(newHoodPos));
        }

        // ===== TURRET TESTER (gamepad2 D-pad left/right) =====
        if (gamepad2.dpad_left) {
            turretPos -= turretStep;
            turretPos = clamp(turretPos);
            turret.setPosition(turretPos);
        } else if (gamepad2.dpad_right) {
            turretPos += turretStep;
            turretPos = clamp(turretPos);
            turret.setPosition(turretPos);
        }
    }

    private double clamp(double val) {
        return Math.max(0, Math.min(1, val));
    }

    public double getTurretPos() {
        return turretPos;
    }

    public double getHoodPos() {
        return hoodLeft.getPosition();
    }
}
