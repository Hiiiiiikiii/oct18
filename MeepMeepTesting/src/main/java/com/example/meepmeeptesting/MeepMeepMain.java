package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepMain {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(500);

        // Start position = ROBOT 1 (right side)
        Pose2d startPose = new Pose2d(58.7, 12.1, Math.toRadians(180)); // facing up along Y

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(
                myBot.getDrive().actionBuilder(startPose)
                        .waitSeconds(5)
                        .splineToConstantHeading(new Vector2d(9.9, 50), Math.toRadians(90))
                        .waitSeconds(5)
                        .splineToConstantHeading(new Vector2d(0, 56), Math.toRadians(90))
                        .waitSeconds(5)
                        .strafeTo(new Vector2d(0,36.4))
                        .strafeTo(new Vector2d(58.7,12.1))
                        .waitSeconds(5)

                        .build()
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
