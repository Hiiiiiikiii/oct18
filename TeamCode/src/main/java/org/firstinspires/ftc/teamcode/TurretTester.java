package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "TurretTester", group = "Testing")
public class TurretTester extends LinearOpMode {

    // ===== CONSTANTS =====
    private static final double TURRET_INITIAL = 177.5 / 355.0;
    private static final double TURRET_STEP = 5.0 / 355.0;

    @Override
    public void runOpMode() throws InterruptedException {

        Servo turret = hardwareMap.get(Servo.class, "tur");

        // Set to initial position
        double turretPos = TURRET_INITIAL;
        turret.setPosition(turretPos);

        telemetry.addLine("Turret Tester Ready");
        telemetry.addLine("D-Pad Left/Right = Move turret");
        telemetry.addData("Initial Position", turretPos);
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            // Increase turret position (Right)
            if (gamepad1.dpad_right) {
                turretPos += TURRET_STEP;
                if (turretPos > 1.0) turretPos = 1.0; // Clamp
                turret.setPosition(turretPos);
                sleep(150); // prevent overshooting
            }

            // Decrease turret position (Left)
            if (gamepad1.dpad_left) {
                turretPos -= TURRET_STEP;
                if (turretPos < 0.0) turretPos = 0.0; // Clamp
                turret.setPosition(turretPos);
                sleep(150);
            }

            telemetry.addData("Turret Position", turretPos);
            telemetry.update();
        }
    }
}
