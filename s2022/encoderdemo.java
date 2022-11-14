package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class encoderdemo extends LinearOpMode {

    RobotConfig robotConfig = new RobotConfig();

    public void runOpMode() {
        waitForStart();
        // robot move forward
        RobotMoveEncoderPositions(20,
                robotConfig.leftWheelF);
        // robot move backward
        RobotMoveEncoderPositions(-20,
                robotConfig.leftWheelF);
    }

    public void RobotMoveEncoderPositions(double inches,
                                          double RobotStraightMovePower,
                                          DcMotor leftWheelF) {
        final double EncoderPositionsPerInch = 89.17;

        leftWheelF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftWheelF.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int encoderPositionsNeeded =
                (int) (EncoderPositionsPerInch * inches);

        leftWheelF.setTargetPosition(encoderPositionsNeeded);

       leftWheelF.setPower(RobotStraightMovePower);
        while (this.opModeIsActive() && leftWheelF.isBusy()) {
            // Do nothing. We are waiting for the motors to report that they are done
        }
        leftWheelF.setPower(0);
    }
}
