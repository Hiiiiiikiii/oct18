package org.firstinspires.ftc.teamcode.Autos;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class simplifiedColorSensor {

    // Sensors
    public NormalizedColorSensor colorLeft;
    public NormalizedColorSensor colorCenter;
    public NormalizedColorSensor colorRight;
    NormalizedRGBA detectedLeft;
    NormalizedRGBA detectedCenter;
    NormalizedRGBA detectedRight;
    int greenLocation = 1;

    //RGB Values
    //TODO: NEED TO TUNE THESE
    private final Color kGreenTarget = new Color(0.145, 0.588, 0.745);

    int colorSense() {

        //Sensors
        colorLeft = hardwareMap.get(NormalizedColorSensor.class, "cl");
        colorCenter = hardwareMap.get(NormalizedColorSensor.class, "cc");
        colorRight = hardwareMap.get(NormalizedColorSensor.class, "cr");

        detectedLeft = colorLeft.getNormalizedColors();
        detectedCenter = colorCenter.getNormalizedColors();
        detectedRight = colorRight.getNormalizedColors();

        //Comparing Left Color Sensor
        double leftGreenDistance = colorDistance(detectedLeft, kGreenTarget);
        double centerGreenDistance = colorDistance(detectedCenter, kGreenTarget);
        double rightGreenDistance = colorDistance(detectedRight, kGreenTarget);

        if (leftGreenDistance > centerGreenDistance && leftGreenDistance > rightGreenDistance) {
            greenLocation = 1;
        } else if (centerGreenDistance > leftGreenDistance && centerGreenDistance > rightGreenDistance) {
            greenLocation = 2;
        } else {
            greenLocation = 3;
        }

        return greenLocation;
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