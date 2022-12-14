package org.firstinspires.ftc.teamcode.s2021;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

//@Disabled
@TeleOp(name = "StrafingTestWorking2021", group = "Opmode RamEater")
public class StrafingTestWorking extends OpMode {

    //private final ElapsedTime runtime = new ElapsedTime();

    // Declare Hardware
    private DcMotor leftWheelF = null;               //Left Wheel Front
    private DcMotor leftWheelR = null;               //Left Wheel Back
    private DcMotor rightWheelF = null;              //Right Wheel Front
    private DcMotor rightWheelR = null;
    private DcMotor armWheel = null;
    private DcMotor intakeWheel1 = null;
    private DcMotor intakeWheel2 = null;
    //private DcMotor intakeWheel3 = null;
    private Servo wobbleServoHand = null;
    private Servo ringPush = null;
    private DcMotor outtakeWheel1 = null;
    private double voltageFactor = 1;

    private int shootThreadRunning = 0;
    private int dropThreadRunning = 0;
    private int lowshootThreadRunning = 0;
    private int powershotThreadRunning = 0;

    @Override
    public void init() {

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftWheelF = hardwareMap.get(DcMotor.class, "D1");
        rightWheelF = hardwareMap.get(DcMotor.class, "D2");
        leftWheelR = hardwareMap.get(DcMotor.class, "D3");
        rightWheelR = hardwareMap.get(DcMotor.class, "D4");
        armWheel = hardwareMap.get(DcMotor.class, "A1");
        intakeWheel1 = hardwareMap.get(DcMotor.class, "I1");
        intakeWheel2 = hardwareMap.get(DcMotor.class, "I2");
        wobbleServoHand = hardwareMap.get(Servo.class, "S2");
        ringPush = hardwareMap.get(Servo.class, "P1");
        outtakeWheel1 = hardwareMap.get(DcMotor.class, "O1");
        //scoopRight.setDirection(Servo.Direction.REVERSE);


        //armWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //armWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        telemetry.addData("Status", "Initialized");

        voltageFactor = getFactorOfVoltage();
        telemetry.addData("Multiplier", voltageFactor);

        telemetry.update();
    }

    @Override
    public void loop() {
        //runtime.reset();
        controllerMove();
    }

    @Override
    public void start() {
        //runtime.reset();
    }

    private void controllerMove() {
        double drive;
        // Power for forward and back motion
        double strafe;  // Power for left and right motion
        double rotateLeft;
        double rotateRight;// Power for rotating the robot
        double intake;
        double raise;
        double lower;
        double outtake;


        double drive2;
        double strafe2;

        drive = -gamepad1.left_stick_y;  // Negative because the gamepad is weird
        strafe = gamepad1.left_stick_x * 1.5;
        rotateLeft = gamepad1.left_trigger;
        rotateRight = gamepad1.right_trigger;
        intake = gamepad2.left_stick_y;
        raise = gamepad2.left_trigger;
        lower = gamepad2.right_trigger;
        drive2 = -gamepad1.right_stick_y;
        strafe2 = gamepad1.right_stick_x;
        outtake = gamepad2.right_stick_y;

        double powerLeftF;
        double powerRightF;
        double powerLeftR;
        double powerRightR;
        double powerIntake;
        double powerRaise;
        double powerLower;
        double powerOuttake;


        powerOuttake = outtake;
        powerRaise = raise;
        powerLower = -lower;


        outtakeWheel1.setPower(powerOuttake);


        if (dropThreadRunning == 0) {
            armWheel.setPower(powerRaise);
            armWheel.setPower(powerLower);
        }
        //powerIntake = intake;
        //intakeWheel1.setPower(powerIntake*voltageFactor);
        //intakeWheel2.setPower(-powerIntake*voltageFactor);

        //if full power on left stick
        if (drive != 0 || strafe != 0 || rotateRight != 0 || rotateLeft != 0) {
            powerLeftF = drive + strafe + rotateRight - rotateLeft;
            powerLeftR = drive - strafe + rotateRight - rotateLeft;

            powerRightF = drive - strafe - rotateRight + rotateLeft;
            powerRightR = drive + strafe - rotateRight + rotateLeft;

            if (dropThreadRunning == 0 && powershotThreadRunning == 0) {
                leftWheelF.setPower(-powerLeftF);
                leftWheelR.setPower(-powerLeftR);

                rightWheelF.setPower(powerRightF);
                rightWheelR.setPower(powerRightR);
            }


        } else {
            // else half power
            powerLeftF = drive2 + strafe2 + rotateRight / 2 - rotateLeft / 2;
            powerLeftR = drive2 - strafe2 + rotateRight / 2 - rotateLeft / 2;

            powerRightF = drive2 - strafe2 - rotateRight / 2 + rotateLeft / 2;
            powerRightR = drive2 + strafe2 - rotateRight / 2 + rotateLeft / 2;

            if (dropThreadRunning == 0 && powershotThreadRunning == 0) {
                leftWheelF.setPower(-powerLeftF * 0.5);
                leftWheelR.setPower(-powerLeftR * 0.5);

                rightWheelF.setPower(powerRightF * 0.5);
                rightWheelR.setPower(powerRightR * 0.5);
            }
        }
        if (gamepad2.x) {
            wobbleServoHand.setPosition(1);
        } else if (gamepad2.b) {
            wobbleServoHand.setPosition(0.5);
        } else if (gamepad2.a) {
            wobbleServoHand.setPosition(0);

        }
        if (gamepad2.left_bumper) {
            ringPush.setPosition(1);
        } else if (gamepad2.right_bumper) {
            ringPush.setPosition(0.7);
        }

        if (gamepad2.y) {
            if (shootThreadRunning == 0) {
                Thread shootThread = new ShootThread();
                shootThread.start();
            }
        }

        if (gamepad2.dpad_up) {
            if (lowshootThreadRunning == 0) {
                Thread lowshootThread = new LowShootThread();
                lowshootThread.start();
            }
        }
        if (gamepad2.dpad_down) {
            if (dropThreadRunning == 0) {
                Thread dropThread = new DropThread();
                dropThread.start();
            }
        }
        if (gamepad2.dpad_right) {
            if (powershotThreadRunning == 0) {
                Thread powershotThread = new PowershotThread();
                powershotThread.start();
            }
        }
    }

    private double getFactorOfVoltage() {
        double currentVoltage = getBatteryVoltage();
        double mult;
        if (currentVoltage >= 14.3) {
            mult = 0.95;
        } else if (currentVoltage >= 14.2) {
            mult = 0.96;
        } else if (currentVoltage >= 14.1) {
            mult = 0.97;
        } else if (currentVoltage >= 14.0) {
            mult = 0.98;
        } else if (currentVoltage >= 13.9) {
            mult = 0.99;
        } else if (currentVoltage >= 13.8) {
            mult = 0.99;
        } else if (currentVoltage <= 12.5) {
            telemetry.addLine("Change the battery!");
            mult = 1;
        } else {
            mult = 1;
        }
        return mult;
    }


    double getBatteryVoltage() {
        double result = Double.POSITIVE_INFINITY;
        for (VoltageSensor sensor : hardwareMap.voltageSensor) {
            double voltage = sensor.getVoltage();
            if (voltage > 0) {
                result = Math.min(result, voltage);
            }
        }
        return result;
    }

    private void shoot(double intake) {
        //drive = -gamepad1.left_stick_y;  // Negative because the gamepad is weird
        //strafe = gamepad1.left_stick_x;
        //rotate = gamepad1.right_stick_x;
        double voltageFactor = getFactorOfVoltage();
        double powerIntake;
        powerIntake = -intake;
        intakeWheel1.setPower(powerIntake * voltageFactor);
        intakeWheel2.setPower(-powerIntake * voltageFactor);
    }

    private void raiseArm(double raise) {
        double powerRaise;
        powerRaise = raise;
        armWheel.setPower(powerRaise);
    }

    private void lowerArm(double lower) {
        double powerLower;
        powerLower = lower;
        armWheel.setPower(-powerLower);
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


    private class ShootThread extends Thread {
        public ShootThread() {
            this.setName("ShootThread");
        }

        // called when tread.start is called. thread stays in loop to do what it does until exit is
        // signaled by main code calling thread.interrupt.
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    shootThreadRunning = 1;
                    //ringPush.setPosition(0.7);
                    //sleep(100);
                    //ringPush.setPosition(1);
                    //sleep(300);
                    shoot(1);
                    sleep(500);
                    ringPush.setPosition(0.7);
                    sleep(300);
                    ringPush.setPosition(1);
                    sleep(300);
                    ringPush.setPosition(0.7);
                    sleep(300);
                    ringPush.setPosition(1);
                    sleep(100);
                    shoot(0);
                    Thread.currentThread().interrupt();
                    shootThreadRunning = 0;
                    return;
                }
            }
            // interrupted means time to shutdown. note we can stop by detecting isInterrupted = true
            // or by the interrupted exception thrown from the sleep function.
            catch (InterruptedException e) {
            }
            // an error occurred in the run loop.
            catch (Exception e) {
            }
        }
    }


    private class PowershotThread extends Thread {
        public PowershotThread() {
            this.setName("PowershotThread");
        }

        // called when tread.start is called. thread stays in loop to do what it does until exit is
        // signaled by main code calling thread.interrupt.
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    powershotThreadRunning = 1;
                    //ringPush.setPosition(0.7);
                    //sleep(100);
                    //ringPush.setPosition(1);
                    //sleep(300);
                    shoot(0.95);
                    sleep(500);
                    ringPush.setPosition(0.7);
                    sleep(300);
                    ringPush.setPosition(1);
                    sleep(300);
                    move(0, 0.75, 0);
                    sleep(500);
                    move(0, 0, 0);
                    sleep(300);
                    ringPush.setPosition(0.7);
                    sleep(300);
                    ringPush.setPosition(1);
                    sleep(100);
                    shoot(0);
                    Thread.currentThread().interrupt();
                    powershotThreadRunning = 0;
                    return;
                }
            }
            // interrupted means time to shutdown. note we can stop by detecting isInterrupted = true
            // or by the interrupted exception thrown from the sleep function.
            catch (InterruptedException e) {
            }
            // an error occurred in the run loop.
            catch (Exception e) {
            }
        }
    }


    private class LowShootThread extends Thread {
        public LowShootThread() {
            this.setName("LowShootThread");

        }

        // called when tread.start is called. thread stays in loop to do what it does until exit is
        // signaled by main code calling thread.interrupt.
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    lowshootThreadRunning = 1;
                    ringPush.setPosition(0.7);
                    sleep(100);
                    ringPush.setPosition(1);
                    sleep(300);
                    shoot(0.8);
                    sleep(500);
                    ringPush.setPosition(0.7);
                    sleep(300);
                    ringPush.setPosition(1);
                    sleep(500);
                    shoot(0);
                    Thread.currentThread().interrupt();
                    lowshootThreadRunning = 0;
                    return;
                }
            }
            // interrupted means time to shutdown. note we can stop by detecting isInterrupted = true
            // or by the interrupted exception thrown from the sleep function.
            catch (InterruptedException e) {
            }
            // an error occurred in the run loop.
            catch (Exception e) {

            }

        }
    }

    private class DropThread extends Thread {
        public DropThread() {
            this.setName("DropThread");
        }

        // called when tread.start is called. thread stays in loop to do what it does until exit is
        // signaled by main code calling thread.interrupt.
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    dropThreadRunning = 1;
                    lowerArm(0.5);
                    sleep(100);
                    wobbleServoHand.setPosition(0.5);
                    sleep(300);
                    move(0, 0, 0);
                    raiseArm(0.25);
                    sleep(300);
                    Thread.currentThread().interrupt();
                    dropThreadRunning = 0;
                    return;
                }
            }
            // interrupted means time to shutdown. note we can stop by detecting isInterrupted = true
            // or by the interrupted exception thrown from the sleep function.
            catch (InterruptedException e) {
            }
            // an error occurred in the run loop.
            catch (Exception e) {

            }

        }
    }

}

