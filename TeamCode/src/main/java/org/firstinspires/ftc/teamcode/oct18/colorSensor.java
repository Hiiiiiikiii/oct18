package org.firstinspires.ftc.teamcode.oct18;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class colorSensor extends LinearOpMode {

    // Sensors
    public NormalizedColorSensor colorLeft;
    public NormalizedColorSensor colorCenter;
    public NormalizedColorSensor colorRight;
    NormalizedRGBA detectedLeft;
    NormalizedRGBA detectedCenter;
    NormalizedRGBA detectedRight;
    boolean leftPurple = false;
    boolean centerPurple = false;
    boolean rightPurple = false;
    boolean leftGreen = false;
    boolean centerGreen = false;
    boolean rightGreen = false;

    //RGB Values
    //TODO: NEED TO TUNE THESE
    private final Color kGreenTarget = new Color(0.070, 0.350, 0.090);
    private final Color kPurpleTarget = new Color(0.300, 0.050, 0.400);

    public void runOpMode() {

        //Sensors
        colorLeft = hardwareMap.get(NormalizedColorSensor.class, "cl");
        colorCenter = hardwareMap.get(NormalizedColorSensor.class, "cc");
        colorRight = hardwareMap.get(NormalizedColorSensor.class, "cr");

        waitForStart();

        while (opModeIsActive()) {
            detectedLeft = colorLeft.getNormalizedColors();
            detectedCenter = colorCenter.getNormalizedColors();
            detectedRight = colorRight.getNormalizedColors();

            //Comparing Left Color Sensor
            double leftGreenDistance = colorDistance(detectedLeft, kGreenTarget);
            double leftPurpleDistance = colorDistance(detectedLeft, kPurpleTarget);

            if (leftGreenDistance < leftPurpleDistance) {
                leftPurple = false;
                leftGreen = true;
            } else {
                leftPurple = true;
                leftGreen = false;
            }

            //Comparing Center Color Sensor
            double centerGreenDistance = colorDistance(detectedCenter, kGreenTarget);
            double centerPurpleDistance = colorDistance(detectedCenter, kPurpleTarget);

            if (centerGreenDistance < centerPurpleDistance) {
                centerPurple = false;
                centerGreen = true;
            } else {
                centerPurple = true;
                centerGreen = false;
            }

            //Comparing Right Color Sensor
            double rightGreenDistance = colorDistance(detectedRight, kGreenTarget);
            double rightPurpleDistance = colorDistance(detectedRight, kPurpleTarget);

            if (rightGreenDistance < rightPurpleDistance) {
                rightPurple = false;
                rightGreen = true;
            } else {
                rightPurple = true;
                rightGreen = false;
            }

        }
    }

    private double colorDistance(NormalizedRGBA a, Color b) {
        return Math.sqrt(
                Math.pow(a.red - b.red, 2)
                        + Math.pow(a.green - b.green, 2)
                        + Math.pow(a.blue - b.blue, 2));
    }

    public class Color {
        public double red, green, blue;

        public Color(double r, double g, double b) {
            this.red = r;
            this.green = g;
            this.blue = b;
        }
    }
}