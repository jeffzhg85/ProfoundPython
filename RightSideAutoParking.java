/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the Afollowing conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.List;

/**
 * This 2020-2021 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the Ultimate Goal game elements.
 * <p>
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 * <p>
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Autonomous(name = "RightSideAutoParking", group = "Profound Pythons")
//@Disabled
public class RightSideAutoParking extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "PowerPlay.tflite";
    private int random = 10;

    private static final String VUFORIA_KEY =
            "AXiCpJb/////AAABmUeqLpvfjkywirbDoSbnyFYKMf7uB24PIfaJZtIqcZO3L7rZVbsKVlz/fovHxEI6VgkUt3PBpXnp+YmHyLrWimMt2AKMFMYsYeZNRmz0p8jFT8DfQC7mmUgswQuPIm64qc8rxwV7vSb0et6Za96tPoDHYNHzhdiaxbI0UHpe4jCkqNTiRDFz8EVNds9kO7bCIXzxBfYfgTDdtjC5JRJ/drtM6DZnTXOqz3pdM85JEVgQqL9wBxUePSjbzyMo9e/FgxluCuWtxHraRJeeuvAlFwAb8wVAoV1cm02qIew0Vh0pDVJqy04gu62CJPhv/wwnXCKywUIEzVMbOLe7muycyHoT6ltpAn4O4s4Z82liWs9x";
    
    private DcMotor leftWheelF = null;               //Left Wheel Front
    private DcMotor leftWheelR = null;               //Left Wheel Back
    private DcMotor rightWheelF = null;              //Right Wheel Front
    private DcMotor rightWheelR = null;
    private Servo clawLeft = null;
    private Servo clawRight = null;
    private DcMotor slideMotor = null;
    private DcMotor armWheel = null;
    private DcMotor intakeWheel1 = null;
    private DcMotor intakeWheel2 = null;
    //private DcMotor intakeWheel3 = null;
    private Servo wobbleServoHand = null;
    private Servo ringPush = null;
    private DcMotor outtakeWheel1 = null;
    
    private VuforiaLocalizer vuforia;

    private TFObjectDetector tfod;
    
    @Override
    public void runOpMode() {


        leftWheelF = hardwareMap.get(DcMotor.class, "lf");
        rightWheelF = hardwareMap.get(DcMotor.class, "rf");
        leftWheelR = hardwareMap.get(DcMotor.class, "lb");
        rightWheelR = hardwareMap.get(DcMotor.class, "rb");
        clawLeft = hardwareMap.get(Servo.class, "s0");
        clawRight = hardwareMap.get(Servo.class, "s1");
         slideMotor = hardwareMap.get(DcMotor.class, "m1");

        
        slideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //armWheel = hardwareMap.get(DcMotor.class, "A1");
        //intakeWheel1 = hardwareMap.get(DcMotor.class, "I1");
        //intakeWheel2 = hardwareMap.get(DcMotor.class, "I2");
        //wobbleServoHand = hardwareMap.get(Servo.class, "S2");
        //ringPush = hardwareMap.get(Servo.class, "P1");
        //outtakeWheel1 = hardwareMap.get(DcMotor.class, "O1");
        
        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can increase the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            tfod.setZoom(1, 16.0/9.0);
        }

        waitForStart();
		
		int return_value = 2;
	
		return_value = detectCone();
		
        park(return_value);
    }

    private void move(double drive,
                      double strafe,
                      double rotate) {

        double powerLeftF;
        double powerRightF;
        double powerLeftR;
        double powerRightR;

        powerLeftF = drive + strafe + rotate;
        powerLeftR = drive - strafe + rotate;

        powerRightF = drive - strafe - rotate;
        powerRightR = drive + strafe - rotate;

        leftWheelF.setPower(-powerLeftF);
        leftWheelR.setPower(-powerLeftR);

        rightWheelF.setPower(powerRightF);
        rightWheelR.setPower(powerRightR);
        
                      }
    
    private void slideHigh() {
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
    private void slideMedium() {
                       
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
    private void slideLow() {
                                  
            if (random % 2 == 0) {
                slideMotor.setDirection(DcMotor.Direction.FORWARD);
            } else {
                slideMotor.setDirection(DcMotor.Direction.REVERSE);
            }
            int target = 2000;

            slideMotor.setTargetPosition(target);
            slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideMotor.setPower(1); 
    }
    private void slideReset() {
            
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
    
        
    private void clawRight() {
            
        clawLeft.setPosition(0);
        clawRight.setPosition(0.51);
    }
            
    private void clawLeft(){
            
            clawLeft.setPosition(0.14);
            clawRight.setPosition(0.33);
    }
	
	private int detectCone() {

        //private static String[] LABELS = {      "1 Bolt",      "2 Bulb",      "3 Panel"};
        int iTimeOut = 5;
        int j = 0;

         while (opModeIsActive() && j < iTimeOut ) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Objects Detected", updatedRecognitions.size());

                        // step through the list of recognitions and display image position/size information for each one
                        // Note: "Image number" refers to the randomized image orientation/number
                        for (Recognition recognition : updatedRecognitions) {
                            double col = (recognition.getLeft() + recognition.getRight()) / 2 ;
                            double row = (recognition.getTop()  + recognition.getBottom()) / 2 ;
                            double width  = Math.abs(recognition.getRight() - recognition.getLeft()) ;
                            double height = Math.abs(recognition.getTop()  - recognition.getBottom()) ;

                            telemetry.addData(""," ");
                            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100 );
                            telemetry.addData("- Position (Row/Col)","%.0f / %.0f", row, col);
                            telemetry.addData("- Size (Width/Height)","%.0f / %.0f", width, height);
                        
							if (recognition.getLabel().equals("1 Bolt")) {
									return 1;
								} else if (recognition.getLabel().equals("2 Bulb")) 
								{
									return 2;
								} else 
								{
									return 3;
								}
						
						}
                        telemetry.update();
                    }
                }
                sleep(500);
				j++;
        }
        return 2;
    }
			

	private void park(int where_to_go) {
        sleep(2000);
        clawLeft();
        sleep(300);
        move(0.25, 0,0);
        sleep(600);
        move(0,0,0);
        slideLow();
        sleep(450);
        move(0, -0.25, 0.025);
        sleep(2100);
        move(0,0,0);
        sleep(1000);
        move(0.15,0,0);
        sleep(450);
        move(0,0,0);
        clawRight();
        sleep(1000);
         move(-0.15,0,0);
        sleep(650);
        slideReset();
        move(0,0,0);
        move(0,0.25,0);
        sleep(1500);
        move(0,0,0);
        sleep(3000);
        move(0,0,-0.15);
        sleep(500);
        move(0.25,0,0);
        sleep(1800);
        move(0,0,0);
        sleep(500);
     
        if (where_to_go ==1)
        {
        //Park robot at location 1
        move(0,-0.5,0);
        sleep(1000);
        move(0,0,0);
        }
        
        else if (where_to_go ==3)
        {
        //park robot at position 3
        move(0,0.5,0);
        sleep(1000);
        move(0,0,0);
        }
        
        else
        {
        //Park robot at position 2
        //Do nothing
        move(0,0,0);
        }
    }
	
	
}
