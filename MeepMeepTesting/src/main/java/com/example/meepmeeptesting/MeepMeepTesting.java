package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(500);

        // Start position = ROBOT 1 (right side)
        Pose2d startPose = new Pose2d(-52, 52, Math.toRadians(135)); // facing up along Y

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(
                myBot.getDrive().actionBuilder(startPose)
                        .setTangent(Math.toRadians(-45))
                        .splineToConstantHeading(new Vector2d(-20, 20), Math.toRadians(-45))// this will pick up all three balls
                        .waitSeconds(5)
                        .splineToLinearHeading(new Pose2d(-12, 30, Math.toRadians(90)), Math.toRadians(90))
                        .setTangent(Math.toRadians(90))
                        .splineToSplineHeading(new Pose2d(-12, 58, Math.toRadians(90)), Math.toRadians(90))
                        .waitSeconds(0.1)
                        .setTangent(Math.toRadians(-90))
                        .splineToSplineHeading(new Pose2d(-12, 20, Math.toRadians(135)), Math.toRadians(-90))
                        .splineToLinearHeading(new Pose2d(-20, 20, Math.toRadians(135)), Math.toRadians(135))
                        .waitSeconds(0.1)
                        .splineToLinearHeading(new Pose2d(12, 30, Math.toRadians(90)), Math.toRadians(90))
                        .setTangent(Math.toRadians(90))
                        .splineToSplineHeading(new Pose2d(12, 58, Math.toRadians(90)), Math.toRadians(90))
                        .waitSeconds(0.1)
                        .setTangent(Math.toRadians(-90))
                        .splineToLinearHeading(new Pose2d(-20, 20, Math.toRadians(135)), Math.toRadians(135))
                        .build()
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
