package org.firstinspires.ftc.teamcode.s2021;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;


//@Disabled
@TeleOp(name = "Claw_test", group = "Opmode RamEaters")

public class Claw_test extends OpMode {

    private final ElapsedTime runtime = new ElapsedTime();

    
    private Servo clawLeft = null;
    private Servo clawRight = null;
    
 

    @Override
    public void init() {

        clawLeft = hardwareMap.get(Servo.class, "s0");
        clawRight = hardwareMap.get(Servo.class, "s1");
  
        telemetry.update();
    }

    @Override
    public void loop() {
        runtime.reset();

        move();
    }

    @Override
    public void start() {
        runtime.reset();
    }

    private void move() {

 
        
        if (gamepad2.right_bumper) {
            
            clawLeft.setPosition(0);
            clawRight.setPosition(0.51);
        } else if (gamepad2.left_bumper) {
            
            clawLeft.setPosition(0.14);
            clawRight.setPosition(0.33);
        }
        
    } 

 
}