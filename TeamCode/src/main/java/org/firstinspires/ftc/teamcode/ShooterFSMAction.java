package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Oct18.ShooterFSM;

public class ShooterFSMAction implements Action {

    private final ShooterFSM shooterFSM;
    private final ElapsedTime timer = new ElapsedTime();

    public ShooterFSMAction(RobotHardware robot, int ballCount, boolean multi) {
        shooterFSM = new ShooterFSM(
                robot.elevatorLeft, robot.elevatorRight,
                robot.spindexer, robot.kicker,
                robot.shooterLeft, robot.shooterRight
        );
        shooterFSM.startFSM(ballCount, multi);
        timer.reset();
    }

    @Override
    public boolean run(TelemetryPacket packet) {
        shooterFSM.updateFSM();

        // You can add telemetry updates for dashboard if you want
        packet.put("FSM Active", shooterFSM.isActive());
        packet.put("Elapsed", timer.seconds());

        // Return true while still running
        return shooterFSM.isActive();
    }
}
