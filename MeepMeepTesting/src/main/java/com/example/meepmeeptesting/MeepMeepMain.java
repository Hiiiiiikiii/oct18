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
        Vector2d START = new Vector2d(58.7,12.1);
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();
    
        myBot.runAction(
                myBot.getDrive().actionBuilder(startPose)
                        .waitSeconds(10)
                        .splineToConstantHeading(new Vector2d(35, 58), Math.toRadians(90))
                        .waitSeconds(3)
                        .strafeTo(START)
                        .waitSeconds(10)
//
                        .build()
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
