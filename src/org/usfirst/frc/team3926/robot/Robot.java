
package org.usfirst.frc.team3926.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;

import java.util.LinkedHashMap;


public class Robot extends IterativeRobot {
	
	static final int TALON_BACK_LEFT_CAN_ID 	= 1;
    static final int TALON_FRONT_LEFT_CAN_ID	= 2;
    static final int TALON_BACK_RIGHT_CAN_ID 	= 3;
    static final int TALON_FRONT_RIGHT_CAN_ID 	= 4;
    static final int TALON_ROLLER_CAN_ID 		= 5;
    static final int TALON_ROLLER_ARM_CAN_ID	= 6;
    static final int TALON_WEDGE_ARM_CAN_ID 	= 7;

    static final int ROLLER_ARM_EXTENDED_DIGITAL_INPUT 	= 0;
    static final int ROLLER_ARM_RETRACTED_DIGITAL_INPUT	= 1;
    static final int WEDGE_ARM_EXTENDED_DIGITAL_INPUT 	= 2;
    static final int WEDGE_ARM_RETRACTED_DIGITAL_INPUT	= 3;
    static final int LEFT_ENCODER_A_DIGITAL_INPUT 		= 4;
    static final int LEFT_ENCODER_B_DIGITAL_INPUT 		= 5;
    static final int LEFT_ENCODER_INDEX_DIGITAL_INPUT	= 6;
    static final int RIGHT_ENCODER_A_DIGITAL_INPUT 		= 7;
    static final int RIGHT_ENCODER_B_DIGITAL_INPUT 		= 8;
    static final int RIGHT_ENCODER_INDEX_DIGITAL_INPUT 	= 9;

    static final int LEFT_JOYSTICK_ID 	= 0;
    static final int RIGHT_JOYSTICK_ID  = 1;
    static final int XBOX_JOYSTICK_ID   = 2;


    // Store all the counter debounce counters in a single spot
    LinkedHashMap<String, Integer> limitSwitchDebounceCounterMap = new LinkedHashMap<>();

	CANTalon talonSRX_FL; //Front Left
	CANTalon talonSRX_BL; //Back Left
	CANTalon talonSRX_FR; //Front Right
    CANTalon talonSRX_BR; //Back Right
    RobotDrive driveSystem; //Controller class for operating TankDrive

    Joystick leftStick; //Joystick object for the left hand
    double leftInput;
    Joystick rightStick; //Joystick object for the right hand
    double rightInput;
    
    Joystick xbox; //The joystick for the supplimentary driver

    Encoder leftEncoder; //Encoder to help us measure our distance traveled
    Encoder rightEncoder; //Encoder on the right side

    DigitalInput rollerArmRetracted; //Limit switch to prevent the roller arm from trying to go to far back
    DigitalInput rollerArmExtended; //Limit switch to stop the roller's arm from over-extending
    CANTalon rollerArm; //Motor to move the roller's arm
    CANTalon roller; //Motor to spin the roller
    
    DigitalInput wedgeArmRetracted; //Limit switch to prevent the wedge's arm from trying to go to far back
    DigitalInput wedgeArmExtended; //Limit switch to prevent the wedge's arm from over-extending
    CANTalon wedgeArm; //Motor to control the wedge
	
    public void robotInit() {
        // Initialize the debounce hash map
        limitSwitchDebounceCounterMap.put("wedgeArmMin", 0);
        limitSwitchDebounceCounterMap.put("wedgeArmMax", 30);
        limitSwitchDebounceCounterMap.put("rollerArmMin", 0);
        limitSwitchDebounceCounterMap.put("rollerArmMax", 30);

		talonSRX_FL = new CANTalon(TALON_FRONT_LEFT_CAN_ID);
		talonSRX_BL = new CANTalon(TALON_BACK_LEFT_CAN_ID);
		talonSRX_FR = new CANTalon(TALON_FRONT_RIGHT_CAN_ID);
		talonSRX_BR = new CANTalon(TALON_BACK_RIGHT_CAN_ID);
		driveSystem = new RobotDrive(talonSRX_FL, talonSRX_BL, talonSRX_FR, talonSRX_BR);

        leftStick = new Joystick(LEFT_JOYSTICK_ID);
        rightStick = new Joystick(RIGHT_JOYSTICK_ID);

        xbox = new Joystick(XBOX_JOYSTICK_ID);

        leftEncoder = new Encoder(LEFT_ENCODER_A_DIGITAL_INPUT, LEFT_ENCODER_B_DIGITAL_INPUT,
                LEFT_ENCODER_INDEX_DIGITAL_INPUT);
        rightEncoder = new Encoder(RIGHT_ENCODER_A_DIGITAL_INPUT, RIGHT_ENCODER_B_DIGITAL_INPUT,
                RIGHT_ENCODER_INDEX_DIGITAL_INPUT);

        rollerArmExtended = new DigitalInput(ROLLER_ARM_EXTENDED_DIGITAL_INPUT);
        rollerArmRetracted = new DigitalInput(ROLLER_ARM_RETRACTED_DIGITAL_INPUT);
        rollerArm = new CANTalon(TALON_ROLLER_ARM_CAN_ID);
        roller = new CANTalon(TALON_ROLLER_CAN_ID);

        wedgeArmRetracted = new DigitalInput(WEDGE_ARM_RETRACTED_DIGITAL_INPUT);
        wedgeArmExtended = new DigitalInput(WEDGE_ARM_EXTENDED_DIGITAL_INPUT);
        wedgeArm = new CANTalon(TALON_WEDGE_ARM_CAN_ID);

    }
    ////END robotInit()////
    
    public void autonomousPeriodic() {
    	runCamera();


    }
    ////END autonomousPeriodic()////

    public void teleopPeriodic() {
        runCamera();

        boolean wedgeRan;

        runDrive(); //Run the main drive control

        if (xbox.getRawButton(1)) { //A button
            runMotor(getState(wedgeArmRetracted, "wedgeArmMin"), getState(wedgeArmRetracted, "wedgeArmMin"), wedgeArm,
                    xbox.getRawAxis(2));
            wedgeRan = true;
        } else {
            wedgeArm.set(0);
            wedgeRan = false;
        }

        if (xbox.getRawButton(2) && !wedgeRan) { //B button
            runMotor(getState(rollerArmRetracted, "rollerArmMin"), getState(rollerArmExtended, "rollerArmMax"), rollerArm,
                    xbox.getRawAxis(2));
        } else {
            rollerArm.set(0);
        }

        if (xbox.getRawButton(6)) {
            roller.set(1);
        } else {
            roller.set(0);
        }

    }
    ////END teleopPeriodic()////   
    
    ////Start DriveFunctions////
	    public void runDrive() {
	        leftInput = leftStick.getY();
	        rightInput = rightStick.getY();
	
	        if (rightStick.getRawButton(1)) {
	            straightMode();
	        }
	
	        if (leftStick.getRawButton(1)) {
	            safteyMode();
	        }
	
	        driveSystem.tankDrive(leftInput, rightInput);
	    }
	    ////END runDrive()////
	
	    public void straightMode() {
	        leftInput = rightInput;
	    }
	    ////END strightMode()////
	
	    public void safteyMode() {
	        leftInput /= 2;
	        rightInput /= 2;
	    }
	    ////END safteyMode()////
	////Stop DriveFunctions////
	    
	////Start LimitSwitchControl////
	    //private int rollerArmMin; //The int to store our debounce count
	    //private int rollerArmMax;
	    //private int wedgeArmMin;
	    //private int wedgeArmMax;

        // XXX What are we doing with madeCheck ?
	    public boolean getState(DigitalInput limit, String limitSwitchCounter) {
	        boolean made;

	        if (limit.get()) {
	            int currentValue = limitSwitchDebounceCounterMap.get(limitSwitchCounter);
                limitSwitchDebounceCounterMap.replace(limitSwitchCounter, currentValue++);


                ++currentValue; //A limit switch is "made" when it is activated

	            if (currentValue >= 30) {
	                made = true;
	            } else {
	                made = false;
	            }
	        } else {
	            made = false;
                limitSwitchDebounceCounterMap.replace(limitSwitchCounter, 0);
	        }

	        return made;
	    }
	////Stop LimitSwitchControl////
	    
	////Start ArmControl////
	    /**
	     * 
	     * @param retractLimitState The boolean of the retracted switch for the motor
	     * @param extendLimitState The boolean of the extended switch for the motor
	     * @param motor The motor that we are controlling
	     * @param speed The speed to set that motor to
	     */
	    public void runMotor(boolean retractLimitState, boolean extendLimitState, CANTalon motor, double speed) {
	        if (!retractLimitState && speed < 0) { //It must be less than zero so that it can move away from the limit
	            motor.set(speed);
	        } else if (!extendLimitState && speed > 0) {
	            motor.set(speed);
	        } else {
	            motor.set(0);
	        }
	    }
	////Stop ArmControl////
	    
	////Start AutonomousController////
	    private double deltaTime = 0; //This helps measure the time for rotations
	    private final double ninetyDegreeTime = 10; //In milliseconds //TODO test this
	    private final int magicAngleTime = 10; //Angle to turn to low goal in milliseconds

        // XXX What are we doing here and with lowBarAct?
	    private final String[] lowBarActions = {
	    		"straight", "turnLeft", "straight", "done" //Order of actions
	    };


	    
	    public String lowbarAct(int stage) {
	    	String action = "";
	    	

	    	
	    	return action;
	    }
	////Stop AutonomousController////

    ////Start CameraController////
    int session;
    Image frame;
    CameraServer server;

    public void runCamera() {
        NIVision.Rect rect = new NIVision.Rect(200, 250, 100, 100);

        NIVision.IMAQdxGrab(session, frame, 1);
        NIVision.imaqDrawShapeOnImage(frame, frame, rect, DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);

        CameraServer.getInstance().setImage(frame);
        Timer.delay(0.005);
    }
    ////Stop CameraController////
}
////END Robot class////





