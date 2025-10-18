package org.firstinspires.ftc.teamcode.Autos;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class MotorPowerFunctions {
    DcMotor frontLeftMotor = hardwareMap.dcMotor.get("fl");
    DcMotor backLeftMotor = hardwareMap.dcMotor.get("bl");
    DcMotor frontRightMotor = hardwareMap.dcMotor.get("fr");
    DcMotor backRightMotor = hardwareMap.dcMotor.get("br");

    public void move(double p,int t) throws InterruptedException {
        frontLeftMotor.setPower(p);
        backLeftMotor.setPower(p);
        frontRightMotor.setPower(p);
        backRightMotor.setPower(p);
        sleep(t);
        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backRightMotor.setPower(0);
    }

    public void strafe(double p,int t) throws InterruptedException {
        frontLeftMotor.setPower(-p);
        backLeftMotor.setPower(p);
        frontRightMotor.setPower(-p);
        backRightMotor.setPower(p);
        sleep(t);
        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backRightMotor.setPower(0);
    }

    public void turn(double p,int t) throws InterruptedException {
        frontLeftMotor.setPower(-p);
        backLeftMotor.setPower(-p);
        frontRightMotor.setPower(p);
        backRightMotor.setPower(p);
        sleep(t);
        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backRightMotor.setPower(0);
    }

}
