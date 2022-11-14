

package org.firstinspires.ftc.teamcode.s2022;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

//@Disabled
@TeleOp(name = "motortest", group = "Opmode RamEaters")

public class motortest extends OpMode {

    private final ElapsedTime runtime = new ElapsedTime();
    // Declare Hardware
    private DcMotor leftWheelF = null;               //Left Wheel Front

    @Override
    public void init() {

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftWheelF = hardwareMap.get(DcMotor.class, "m1");
        //intakeWheel1 = hardwareMap.get(DcMotor.class, "I1");
        //intakeWheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //intakeWheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        runtime.reset();

        //sleep(1000);
        move();
    }

    @Override
    public void start() {
        runtime.reset();
    }

    private void move() {
        double y;
        // Power for forward and back motion
        double x;  // Power for left and right motion
        double rotateLeft;
        //double rotateRight;// Power for rotating the robot
        //int intake;


        double drive2;
        double strafe2;

        y = gamepad1.left_stick_y;  // Negative because the gamepad is weird
        x = gamepad1.left_stick_x;
        //rotateLeft = gamepad1.left_trigger;
        //rotateRight = gamepad1.right_trigger;
        //intake = gamepad2.left_trigger;

        //drive2 = -gamepad1.right_stick_y;
        //strafe2 = gamepad1.right_stick_x;

        //double powerLeftF;
        //double powerRightF;
        //double powerLeftR;
        //double powerRightR;
        // double powerIntake;
        //intakeWheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftWheelF.setPower(1);
        //if full power on left stick
        if (x != 0) {
        //    powerLeftF = drive + strafe + rotateRight - rotateLeft;

        leftWheelF.setPower(x);

            //intakeWheel1.setPower(powerIntake);

        } else if (y != 0){
            // else half power
            //powerLeftF = drive2 + strafe2 + rotateRight;

            leftWheelF.setPower(y);
			
			leftWheelF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			
			leftWheelF.setTargetPosition(100);
			
			leftWheelF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }
}
