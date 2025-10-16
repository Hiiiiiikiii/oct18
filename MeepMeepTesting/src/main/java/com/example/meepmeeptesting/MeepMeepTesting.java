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
        Pose2d startPose = new Pose2d(58.7, 12.1, Math.toRadians(180)); // facing up along Y

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(
                myBot.getDrive().actionBuilder(startPose)
                            //THE ROBOT SHOULD SHOOT THE THREE BALLS IN THIS 5 SECONDS
                        .waitSeconds(5)
                        //THE ROBOT SHOULD HAVE INTAKE ACTIVE AND EVERYTHING ELSE SHOULD BE IN INIT POSITION
                        .splineToLinearHeading(new Pose2d(38, 57.2, Math.toRadians(90)), Math.toRadians(90))
                        //IN THESE 5 SECONDS THE INTAKE SHOULD TURN OFF
                        .waitSeconds(5)
//                      //NO ACTION JUST MOVE
                        .lineToY(16)
                        .splineToConstantHeading(new Vector2d(0, 52), Math.toRadians(90))
                        .waitSeconds(10)
//                      ROBOT SHOULD GO TO THE Y POSITION THEN SHOOT THE THREE BALLS THAT IT INTOOK
                        .lineToY(10)
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
