
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
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;


public class Robot extends IterativeRobot {
	static final int TALON_BACK_LEFT_CAN_ID 	= 1;
    static final int TALON_FRONT_LEFT_CAN_ID	= 2;
    static final int TALON_BACK_RIGHT_CAN_ID 	= 3;
    static final int TALON_FRONT_RIGHT_CAN_ID 	= 4;
    static final int TALON_ROLLER_CAN_ID 		= 5;
    
    static final int TALON_WEDGE_ARM_PWM 	= 0;
    static final int SPARK_ROLLER_ARM_PWM	= 1;

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
    private final int XBOX_LEFT_Y_AXIS = 1;
    private final int XBOX_LEFT_TRIGGER = 2;
    private final int XBOX_RIGHT_TRIGGER = 3;
    private final int XBOX_RIGHT_Y_AXIS = 5;

    Encoder leftEncoder; //Encoder to help us measure our distance traveled
    Encoder rightEncoder; //Encoder on the right side

    DigitalInput rollerArmRetracted; //Limit switch to prevent the roller arm from trying to go to far back
    DigitalInput rollerArmExtended; //Limit switch to stop the roller's arm from over-extending
    Talon rollerArm; //Motor to move the roller's arm
    CANTalon roller; //Motor to spin the roller

    DigitalInput wedgeArmRetracted; //Limit switch to prevent the wedge's arm from trying to go to far back
    DigitalInput wedgeArmExtended; //Limit switch to prevent the wedge's arm from over-extending
    Talon wedgeArm; //Motor to control the wedge

    private int rollerArmMin; //The int to store our debounce count
    private int rollerArmMax;
    private int wedgeArmMin;
    private int wedgeArmMax;

    CameraServer server;

    //private double correctionNumber = .2;

    public void robotInit() {
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
        rollerArm = new Talon(SPARK_ROLLER_ARM_PWM);
        roller = new CANTalon(TALON_ROLLER_CAN_ID);

        wedgeArmRetracted = new DigitalInput(WEDGE_ARM_RETRACTED_DIGITAL_INPUT);
        wedgeArmExtended = new DigitalInput(WEDGE_ARM_EXTENDED_DIGITAL_INPUT);
        wedgeArm = new Talon(TALON_WEDGE_ARM_PWM);

        server = CameraServer.getInstance();
        server.setQuality(50);
        server.startAutomaticCapture("cam0");
       // server.startAutomaticCapture("cam1");
    }
    ////END robotInit()////

    public void autonomousPeriodic() {
    	//runCamera();

        Timer.delay(0.005);
    }
    ////END autonomousPeriodic()////
    
    public void teleopInit() {
    	server.startAutomaticCapture("cam0");
    }
    ////END teleopInit()////
    
    public void teleopPeriodic() {
        runDrive(); //Run the main drive control

        if (leftStick.getRawButton(2)){
            wedgeArm.set(1);
        } else wedgeArm.set(0);

        if (rightStick.getRawButton(2)){
            rollerArm.set(1);
        } else rollerArm.set(0);
        
        double rollerInput = xbox.getRawAxis(XBOX_LEFT_Y_AXIS);
        rollerArmMin = getState(rollerArmRetracted, rollerArmMin);
        rollerArmMax = getState(rollerArmExtended, rollerArmMax);

        double rollerArmSet = (rollerArmMin >= 30) ? ((rollerInput * rollerInput)/2) : 0;
        rollerArmSet = (rollerArmMax >= 30) ? (rollerInput * rollerInput)/2 : rollerArmSet;
        rollerArm.set(rollerArmSet);
        
        double rollerSet = (xbox.getRawAxis(XBOX_LEFT_TRIGGER) >= 0.1) ? xbox.getRawAxis(2) : 0;
        rollerSet = (xbox.getRawAxis(XBOX_RIGHT_TRIGGER)>= 0.1) ? xbox.getRawAxis(3) * -1 : rollerSet;
        roller.set(rollerSet);

        double wedgeInput = xbox.getRawAxis(XBOX_RIGHT_Y_AXIS);
        wedgeArmMin = getState(wedgeArmRetracted, wedgeArmMin);
        wedgeArmMax = getState(wedgeArmExtended, wedgeArmMax);
        
        double wedgeSet = (wedgeArmMin >= 30) ? ((wedgeInput * wedgeInput))/2 : 0;
        wedgeSet = (wedgeArmMax >= 30) ? (wedgeInput * wedgeInput)/2 : wedgeSet;
        wedgeArm.set(wedgeSet);
        
        Timer.delay(0.005);
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
	    ////END straightMode()////

	    public void safteyMode() {
	        leftInput /= 2;
	        rightInput /= 2;
	    }
	    ////END safteyMode()////

	////Stop DriveFunctions////

	////Start LimitSwitchControl////
	    public int getState(DigitalInput limit, int madeCheck) {

	        if (limit.get()) {
	            ++madeCheck;
	        } else {
	            madeCheck = 0;
	        }

	        return madeCheck;
	    }
	    ////END getState()////
	////Stop LimitSwitchControl////
	    
	////Start AutonomousController////
/*	    private double deltaTime = 0; //This helps measure the time for rotations
	    private final double ninetyDegreeTime = 10; //In milliseconds //TODO test this
	    private final int magicAngleTime = 10; //Angle to turn to low goal in milliseconds
	    
	    private void autonomousControl(int session) {
	    	
	    }

	    private final String[] lowBarActions = {
	    		"straight", "turnLeft", "straight", "done" //Order of actions
	    };

	    */
	////Stop AutonomousController////
}
////END Robot class////





