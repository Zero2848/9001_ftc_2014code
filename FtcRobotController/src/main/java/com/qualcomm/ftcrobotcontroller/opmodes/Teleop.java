package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.TouchSensor;


public class Teleop extends OpMode{
    /*
	 * Note: the configuration of the servos is such that
	 * as the arm servo approaches 0, the arm position moves up (away from the floor).
	 * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
	 */
    // TETRIX VALUES.

    // position of the arm servo.


    // amount to change the arm servo position.


    // position of the claw servo


    // amount to change the claw servo position by

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor lift;

    TouchSensor bottomTouch;
    TouchSensor topTouch;
    Servo release;



    /**
     * Constructor
     */
    public Teleop() {

    }

    /*
     * Code to run when the op mode is initialized goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
     */
    @Override
    public void init() {


		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

		/*
		 * For the demo Tetrix K9 bot we assume the following,
		 *   There are two motors "motor_1" and "motor_2"
		 *   "motor_1" is on the right side of the bot.
		 *   "motor_2" is on the left side of the bot and reversed.
		 *
		 * We also assume that there are two servos "servo_1" and "servo_6"
		 *    "servo_1" controls the arm joint of the manipulator.
		 *    "servo_6" controls the claw joint of the manipulator.
		 */
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        topTouch=hardwareMap.touchSensor.get("topTouch");
        bottomTouch=hardwareMap.touchSensor.get("bottomTouch");
        lift=hardwareMap.dcMotor.get("lift");




        // assign the starting position of the wrist and claw

    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public  void start(){

    }

    public void loop() {

		/*
		 * Gamepad 1
		 *
		 * Gamepad 1 controls the motors via the left stick, and it controls the
		 * wrist/claw via the a,b, x, y buttons
		 */

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float throttle = -gamepad1.left_stick_y;
        float direction = gamepad1.left_stick_x;
        float right = throttle - direction;
        float left = throttle + direction;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float) scaleInput(right);
        left = (float) scaleInput(left);

        // write the values to the motors
        motorRight.setPower(right);
        motorLeft.setPower(left);


        // update the position of the arm.


        // update the position of the claw
        if (gamepad1.x) {

        }
        if (gamepad1.y) {
            while (!topTouch.isPressed()  ) {
                lift.setPower(1);
            }


        }
        if (gamepad1.a) {
            if (!bottomTouch.isPressed()) {
                lift.setPower(-1);

            }
        }

            if (gamepad1.b) {

            }

            // clip the position values so that they never exceed their allowed range.


            // write position values to the wrist and claw servo




		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
            telemetry.addData("Text", "*** Robot Data***");

            telemetry.addData("left tgt pwr", "left  pwr: " + String.format("%.2f", left));
            telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
            telemetry.addData("topTouch", "topTouch: " + String.valueOf(topTouch));
            telemetry.addData("bottomTouch", "bottomTouch: " + String.valueOf(bottomTouch));

        }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
        @Override
        public void stop(){

        }


    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }

}
