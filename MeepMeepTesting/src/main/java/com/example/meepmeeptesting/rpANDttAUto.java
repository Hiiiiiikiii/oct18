package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class rpANDttAUto {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(500);

        // Start position = ROBOT 1 (right side)
        Pose2d startPoseTT = new Pose2d(58.7, 12.1, Math.toRadians(180)); // facing up along Y
        Pose2d startPoseRP = new Pose2d(-53, 47, Math.toRadians(125)); // facing up along Y
        Vector2d START = new Vector2d(58.7,12.1);

        //TECHTITANS
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setColorScheme(new ColorSchemeBlueDark())
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();
        //ROBOPLAYERS
        RoadRunnerBotEntity myBot2 = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(
                myBot.getDrive().actionBuilder(startPoseTT)
                        .waitSeconds(10)
                        .splineToConstantHeading(new Vector2d(35, 58), Math.toRadians(90))
                        .waitSeconds(3)
                        .strafeTo(START)
                        .waitSeconds(10)
//
                        .build()
        );
        myBot2.runAction(
                myBot2.getDrive().actionBuilder(startPoseRP)
                        .waitSeconds(12)
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
                .addEntity(myBot2)
                .start();
    }
}
