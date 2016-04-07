
package org.usfirst.frc.team3926.robot;

import edu.wpi.first.wpilibj.*;


public class Robot extends IterativeRobot {

	static final int TALON_BACK_LEFT_CAN_ID                = 1;
    static final int TALON_FRONT_LEFT_CAN_ID               = 2;
    static final int TALON_BACK_RIGHT_CAN_ID               = 3;
    static final int TALON_FRONT_RIGHT_CAN_ID              = 4;
    static final int TALON_ROLLER_CAN_ID                   = 5;

    static final int SPARK_WEDGE_ARM_PWM                   = 0;
    static final int SPARK_ROLLER_ARM_PWM                  = 1;

    static final int ROLLER_ARM_EXTENDED_DIGITAL_INPUT     = 0;
    static final int ROLLER_ARM_RETRACTED_DIGITAL_INPUT    = 1;
    static final int WEDGE_ARM_EXTENDED_DIGITAL_INPUT      = 2;
    static final int WEDGE_ARM_RETRACTED_DIGITAL_INPUT     = 3;
    /*static final int LEFT_ENCODER_A_DIGITAL_INPUT 	   = 4;
    static final int LEFT_ENCODER_B_DIGITAL_INPUT          = 5;
    static final int LEFT_ENCODER_INDEX_DIGITAL_INPUT      = 6;
    static final int RIGHT_ENCODER_A_DIGITAL_INPUT         = 7;
    static final int RIGHT_ENCODER_B_DIGITAL_INPUT         = 8;
    static final int RIGHT_ENCODER_INDEX_DIGITAL_INPUT     = 9;*/

    static final int LEFT_JOYSTICK_ID                      = 0;
    static final int RIGHT_JOYSTICK_ID                     = 1;
    static final int XBOX_JOYSTICK_ID                      = 2;
	CANTalon talonSRX_FL; //Front Left
	CANTalon talonSRX_BL; //Back Left
	CANTalon talonSRX_FR; //Front Right
    CANTalon talonSRX_BR; //Back Right
    RobotDrive driveSystem; //Controller class for operating TankDrive

    Joystick leftStick;  //Joystick object for the left hand
    double leftInput;    //Double so that we can manipulate speed on the left stick
    Joystick rightStick; //Joystick object for the right hand
    double rightInput;   //Double so that we can manipulate speed on the right stick

    Joystick xbox; //The joystick for the helmsman
    private final int XBOX_LEFT_Y_AXIS      = 1;
    private final int XBOX_LEFT_TRIGGER     = 2;
    private final int XBOX_RIGHT_TRIGGER    = 3;
    private final int XBOX_RIGHT_Y_AXIS     = 5;

    //Encoder leftEncoder; //Encoder to help us measure our distance traveled (not being used)
    //Encoder rightEncoder; //Encoder on the right side(not being used)

    DigitalInput rollerArmRetracted; //Limit switch to prevent the roller arm from trying to go to far back
    DigitalInput rollerArmExtended;  //Limit switch to stop the roller's arm from over-extending
    Talon rollerArm; //Motor to move the roller's arm
    CANTalon roller; //Motor to spin the roller

    DigitalInput wedgeArmRetracted; //Limit switch to prevent the wedge's arm from trying to go to far back
    DigitalInput wedgeArmExtended;  //Limit switch to prevent the wedge's arm from over-extending
    Talon wedgeArm; //Motor to control the wedge

    private int rollerArmMin; //The int to store our debounce count
    private int rollerArmMax;
    private int wedgeArmMin;
    private int wedgeArmMax;

    double deltaTime      = 0;       //Autonomous time controller
    boolean stopSystem    = false; //Boolean to tell if we have finished our task
    CameraServer server; //Server to controll cameras

    public void robotInit() { //Code that runs when the robot starts, only runs once
		talonSRX_FL         = new CANTalon(TALON_FRONT_LEFT_CAN_ID); //Motor controllers
		talonSRX_BL         = new CANTalon(TALON_BACK_LEFT_CAN_ID);
		talonSRX_FR         = new CANTalon(TALON_FRONT_RIGHT_CAN_ID);
		talonSRX_BR         = new CANTalon(TALON_BACK_RIGHT_CAN_ID);
		driveSystem         = new RobotDrive(talonSRX_FR, talonSRX_BR, talonSRX_FL, talonSRX_BL); //Drive system, using tank drive this year
        leftStick           = new Joystick(LEFT_JOYSTICK_ID); //Joysticks
        rightStick          = new Joystick(RIGHT_JOYSTICK_ID);

        xbox = new Joystick(XBOX_JOYSTICK_ID); //XBOX controller
                //Encoders, not currently being used
        /*leftEncoder = new Encoder(LEFT_ENCODER_A_DIGITAL_INPUT, LEFT_ENCODER_B_DIGITAL_INPUT,
                LEFT_ENCODER_INDEX_DIGITAL_INPUT);
        rightEncoder = new Encoder(RIGHT_ENCODER_A_DIGITAL_INPUT, RIGHT_ENCODER_B_DIGITAL_INPUT,
                RIGHT_ENCODER_INDEX_DIGITAL_INPUT);*/

        rollerArmExtended     = new DigitalInput(ROLLER_ARM_EXTENDED_DIGITAL_INPUT); //Roller arm = top bar with rollers
        rollerArmRetracted    = new DigitalInput(ROLLER_ARM_RETRACTED_DIGITAL_INPUT);
        rollerArm             = new Talon(SPARK_ROLLER_ARM_PWM);
        roller                = new CANTalon(TALON_ROLLER_CAN_ID);

        wedgeArmRetracted     = new DigitalInput(WEDGE_ARM_RETRACTED_DIGITAL_INPUT); //Wedge arm = bottom arm to scoop ball
        wedgeArmExtended      = new DigitalInput(WEDGE_ARM_EXTENDED_DIGITAL_INPUT);
        wedgeArm              = new Talon(SPARK_WEDGE_ARM_PWM);

        server = CameraServer.getInstance(); //Camera server, camera located directly below arms
        server.setQuality(50);
        server.startAutomaticCapture("cam0");


    }
    ////END robotInit()////

    public void autonomousInit() {
        deltaTime = System.currentTimeMillis(); //Establish start time for autonomous
    }
    ////END autonomousInit()////
    
    public void autonomousPeriodic() {
    	if (System.currentTimeMillis() - deltaTime < 4000) { //If less than 4 seconds have passed
            driveSystem.tankDrive(1, 1); //Drive at full speed forward
        } else if (!stopSystem) { //If the system has not been stopped
            driveSystem.tankDrive(0, 0); //Stop the drive
            stopSystem = true; //Say that we have stopped the drive
        } else {
            //No Else
        }
    	
        Timer.delay(0.005);
    }
    ////END autonomousPeriodic()////
    
    public void teleopInit() {
    	//server.startAutomaticCapture("cam0");
    }
    ////END teleopInit()////
    
    public void teleopPeriodic() {
        runDrive(); //Run the main drive control
        
        double rollerInput     = xbox.getRawAxis(XBOX_LEFT_Y_AXIS);
        rollerArmMin           = getState(rollerArmRetracted, rollerArmMin);
        rollerArmMax           = getState(rollerArmExtended, rollerArmMax);
        //Setting speed of the roller arm, half the value of the input
        double rollerArmSet    = (rollerArmMin >= 30) ? ((rollerInput * Math.abs(rollerInput))/2) : 0;
        rollerArmSet           = (rollerArmMax >= 30) ? (rollerInput * Math.abs(rollerInput))/2 : rollerArmSet;
        rollerArm.set(rollerArmSet);
        //Setting the speed of the rollers to bring the ball in
        double rollerSet       = (xbox.getRawAxis(XBOX_LEFT_TRIGGER) >= 0.1) ? xbox.getRawAxis(2) : 0;
        rollerSet              = (xbox.getRawAxis(XBOX_RIGHT_TRIGGER)>= 0.1) ? xbox.getRawAxis(3) * -1 : rollerSet;
        roller.set(rollerSet);

        double wedgeInput      = xbox.getRawAxis(XBOX_RIGHT_Y_AXIS);
        wedgeArmMin            = getState(wedgeArmRetracted, wedgeArmMin);
        wedgeArmMax            = getState(wedgeArmExtended, wedgeArmMax);
        //Setting the speed of the wedge arm -- half of input wasn't enough power, so its 60% power
        double wedgeSet        = (wedgeArmMin >= 30) ? (wedgeInput*0.6) : 0;
        wedgeSet               = (wedgeArmMax >= 30) ? (wedgeInput*0.6) : wedgeSet;
        wedgeArm.set(wedgeSet);
        
        Timer.delay(0.005);
    }
    ////END teleopPeriodic()////

    ////Start DriveFunctions////
	    public void runDrive() { //main drive function
	        leftInput = leftStick.getY() * -1; //inputs inverted to counteract the inversion of the joysticks
	        rightInput = rightStick.getY() * -1;

            if (rightStick.getRawButton(1)) {
                straightMode();
            }

            if (leftStick.getRawButton(1)) {
                safteyMode();
            }

	        driveSystem.tankDrive(leftInput, rightInput); //Drive system, using tank drive
	    }
	    ////END runDrive()////

	    public void straightMode() {
	        leftInput = rightInput;
	    } //sets both motors to same strength to move straight
	    ////END straightMode()////

	    public void safteyMode() { //cuts power to motors in half for precision driving
	        leftInput /= 2;
	        rightInput /= 2;
	    }
	    ////END safety Mode()////

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
	    
}
////END Robot class////





