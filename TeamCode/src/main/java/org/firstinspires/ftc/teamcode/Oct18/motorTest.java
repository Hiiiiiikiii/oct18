package org.firstinspires.ftc.teamcode.Oct18;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class motorTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        ElapsedTime timer = new ElapsedTime();

        DcMotor left = hardwareMap.get(DcMotor.class, "motor1");
        DcMotor right = hardwareMap.get(DcMotor.class, "motor2");
        
        right.setDirection(DcMotor.Direction.REVERSE);



        double power = 0;


        waitForStart();


        if (isStopRequested()) return;

        while (opModeIsActive()) {

            if (gamepad1.a) {
                left.setPower(power);
                right.setPower(power);

            } else if (gamepad1.b) {
                left.setPower(-1 * power);
                right.setPower(-1 * power);

            } else {
                left.setPower(0);
                right.setPower(0);

            }
            if (gamepad1.dpad_right) {
                power += 0.1;
                timer.reset();
                while (timer.milliseconds() < 200) {

                }
            } else if (gamepad1.dpad_down) {
                power -= 0.1;
                timer.reset();
                while (timer.milliseconds() < 200) {

                }
            }

            telemetry.addData("power: ", power);
            telemetry.update();
        }
    }
}
