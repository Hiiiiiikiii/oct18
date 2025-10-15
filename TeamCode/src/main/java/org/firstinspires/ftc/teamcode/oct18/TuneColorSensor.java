package org.firstinspires.ftc.teamcode.oct18;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

@TeleOp(name = "Color Sensor Tuner", group = "Utility")
public class TuneColorSensor extends LinearOpMode {

    @Override
    public void runOpMode() {
        // Get color sensor from configuration
        NormalizedColorSensor colorSensor = hardwareMap.get(NormalizedColorSensor.class, "cs");

        telemetry.addLine("Color Sensor Tuner Ready");
        telemetry.addLine("Place the object (ball) above the sensor and press PLAY.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            NormalizedRGBA colors = colorSensor.getNormalizedColors();

            // Display normalized RGB values (0.0–1.0)
            telemetry.addLine("=== NORMALIZED COLOR READING ===");
            telemetry.addData("Red:", "%.3f", colors.red);
            telemetry.addData("Green:", "%.3f", colors.green);
            telemetry.addData("Blue:", "%.3f", colors.blue);
            telemetry.addData("Color Constant:",
                    "new Color(%.3f, %.3f, %.3f)", colors.red, colors.green, colors.blue);

            telemetry.update();

            sleep(250); // update every 0.25 seconds
        }
    }
}