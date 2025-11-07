package org.firstinspires.ftc.teamcode.Autos;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class simplifiedColorSensor {

    // Sensors
    public RevColorSensorV3 colorLeft;
    public RevColorSensorV3 colorCenter;
    public RevColorSensorV3 colorRight;
    NormalizedRGBA detectedLeft;
    NormalizedRGBA detectedCenter;
    NormalizedRGBA detectedRight;

    public int greenLocation = 1;

    public boolean leftGreen = false;
    public boolean leftPurple = false;
    public boolean centerGreen = false;
    public boolean centerPurple = false;
    public boolean rightGreen = false;
    public boolean rightPurple = false;

    public int colorSense() {

        //Sensors
        colorLeft = hardwareMap.get(RevColorSensorV3.class, "cl");
        colorCenter = hardwareMap.get(RevColorSensorV3.class, "cc");
        colorRight = hardwareMap.get(RevColorSensorV3.class, "cr");

        detectedLeft = colorLeft.getNormalizedColors();
        detectedCenter = colorCenter.getNormalizedColors();
        detectedRight = colorRight.getNormalizedColors();

        if (detectedLeft.blue > detectedLeft.green) {
            leftGreen = false;
            leftPurple = true;
        } else if (detectedLeft.blue < detectedLeft.green) {
            leftGreen = true;
            leftPurple = false;
            greenLocation = 3;
        } else {
            leftGreen = false;
            leftPurple = false;
        }

        if (detectedCenter.blue > detectedCenter.green) {
            centerGreen = false;
            centerPurple = true;
        } else if (detectedCenter.blue < detectedCenter.green) {
            centerGreen = true;
            centerPurple = false;
            greenLocation = 1;
        } else {
            centerGreen = false;
            centerPurple = false;
        }

        if (detectedRight.blue > detectedRight.green) {
            rightGreen = false;
            rightPurple = true;
        } else if (detectedRight.blue < detectedRight.green) {
            rightGreen = true;
            rightPurple = false;
            greenLocation = 2;
        } else {
            rightGreen = false;
            rightPurple = false;
        }

        return greenLocation;
    }
}