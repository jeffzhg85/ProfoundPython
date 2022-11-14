package org.firstinspires.ftc.teamcode.s2021;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ClawOperation", group = "Exercises")
public class ClawOperation extends LinearOpMode {
    Servo leftServo;
    Servo rightServo;

    @Override
    public void runOpMode() throws InterruptedException {
        leftServo = hardwareMap.servo.get("s1");
        rightServo = hardwareMap.servo.get("s2");

        double leftServoOpenPosition = 0;
        double leftServoClosePosition = 0.9;
        double rightServoOpenPosition = 0;
        double rightServoClosePosition = 0.9;

        waitForStart();

        while (opModeIsActive()) {
            // open the claw
            if (gamepad1.left_bumper == true) {
                leftServo.setPosition(leftServoOpenPosition);
                rightServo.setPosition(1 - leftServoOpenPosition);
            }

            // close the claw
            else if (gamepad1.right_bumper == true) {
                leftServo.setPosition(leftServoClosePosition);
                rightServo.setPosition(1 - leftServoClosePosition);
            }

        }

    }
}