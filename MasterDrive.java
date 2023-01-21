package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;


//@Disabled
@TeleOp(name = "MasterDrive", group = "Opmode RamEaters")

public class MasterDrive extends OpMode {

    private final ElapsedTime runtime = new ElapsedTime();
    private int random = 10;

    private DcMotor slideMotor = null;
    private Servo clawLeft = null;
    private Servo clawRight = null;
    
    private DcMotor leftWheelF = null;               //Left Wheel Front
    private DcMotor leftWheelR = null;               //Left Wheel Back
    private DcMotor rightWheelF = null;              //Right Wheel Front
    private DcMotor rightWheelR = null;

    
    @Override
    public void init() {


        slideMotor = hardwareMap.get(DcMotor.class, "m1");

        
        slideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        clawLeft = hardwareMap.get(Servo.class, "s0");
        clawRight = hardwareMap.get(Servo.class, "s1");
        
        leftWheelF = hardwareMap.get(DcMotor.class, "lf");
        rightWheelF = hardwareMap.get(DcMotor.class, "rf");
        leftWheelR = hardwareMap.get(DcMotor.class, "lb");
        rightWheelR = hardwareMap.get(DcMotor.class, "rb");
        
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
            int target = 4700;

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
            int target = 3150;

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
            int target = 2150;

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
        else if (gamepad2.right_bumper) {
            
            clawLeft.setPosition(0);
            clawRight.setPosition(0.51);
            
        } else if (gamepad2.left_bumper) {
            
            clawLeft.setPosition(0.14);
            clawRight.setPosition(0.33);
        }
        
        double drive;
        double strafe; 
        double rotateLeft;
        double rotateRight;
        //int intake;


        double drive2;
        double strafe2;

        drive = -gamepad1.left_stick_y;  
        strafe = gamepad1.left_stick_x;
        rotateLeft = gamepad1.left_trigger;
        rotateRight = gamepad1.right_trigger;
        
        drive2 = -gamepad1.right_stick_y;
        strafe2 = gamepad1.right_stick_x;

        double powerLeftF;
        double powerRightF;
        double powerLeftR;
        double powerRightR;
        double factor = 0.4;
        if (drive != 0 || strafe != 0 || rotateRight != 0 || rotateLeft != 0) {
            powerLeftF = drive + strafe + rotateRight - rotateLeft;
            powerLeftR = drive - strafe + rotateRight - rotateLeft;
            powerRightF = drive - strafe - rotateRight + rotateLeft;
            powerRightR = drive + strafe - rotateRight + rotateLeft;

            leftWheelF.setPower(-powerLeftF*factor);
            leftWheelR.setPower(-powerLeftR*factor);

            rightWheelF.setPower(powerRightF*factor);
            rightWheelR.setPower(powerRightR*factor);

        } else {
            powerLeftF = drive2 + strafe2 + rotateRight;
            powerLeftR = drive2 - strafe2 + rotateRight;

            powerRightF = drive2 - strafe2 - rotateLeft;
            powerRightR = drive2 + strafe2 - rotateLeft;

            leftWheelF.setPower(-powerLeftF*factor);
            leftWheelR.setPower(-powerLeftR*factor);

            rightWheelF.setPower(powerRightF*factor);
            rightWheelR.setPower(powerRightR*factor);
        }

    }
    
     

}