package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;


//@Disabled
@TeleOp(name = "Slide_test", group = "Opmode RamEaters")

public class Slide_test extends OpMode {

    private final ElapsedTime runtime = new ElapsedTime();
	private int random = 10;

    private DcMotor slideMotor = null;
    

    
    @Override
    public void init() {


        slideMotor = hardwareMap.get(DcMotor.class, "m1");

        
        slideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
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
        
     
        
        if (gamepad2.y) {
            if (random % 2 == 0) {
				slideMotor.setDirection(DcMotor.Direction.FORWARD);
			} else {
				slideMotor.setDirection(DcMotor.Direction.REVERSE);
			}
			int target = 3550;

			slideMotor.setTargetPosition(target);
			slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
			slideMotor.setPower(1);
        }
        else if (gamepad2.b) {
                       
			if (random % 2 == 0) {
				slideMotor.setDirection(DcMotor.Direction.FORWARD);
			} else {
				slideMotor.setDirection(DcMotor.Direction.REVERSE);
			}
			int target = 2600;

			slideMotor.setTargetPosition(target);
			slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
			slideMotor.setPower(1);       
		}
        else if (gamepad2.a) {
                                  
			if (random % 2 == 0) {
				slideMotor.setDirection(DcMotor.Direction.FORWARD);
			} else {
				slideMotor.setDirection(DcMotor.Direction.REVERSE);
			}
			int target = 1600;

			slideMotor.setTargetPosition(target);
			slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
			slideMotor.setPower(1); 
        }
        else if (gamepad2.x) {
            
        if (random % 2 == 1) {
            slideMotor.setDirection(DcMotor.Direction.FORWARD);
        } else {
            slideMotor.setDirection(DcMotor.Direction.REVERSE);
        }
        slideMotor.setTargetPosition(0);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideMotor.setPower(1);
        random++;
        }

    }
    
     

}