/** @file Robot.java
 *  This is the main file for the robot. This is the file that will actually run on the robot and contains all main
 *  functions.
 *  @brief Main file for the robot (Benny:2016:FRC Stronghold)
 *  @author William Kluge
 *  @author Joe Brooksbank
 *  @version 2.0.0
 */

package org.usfirst.frc.team3926.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Robot extends IterativeRobot {
    
    //!< The CAN ID for the back left talon
	static final int TALON_BACK_LEFT_CAN_ID                = 1;
    //!< The CAN ID for the front left talon
    static final int TALON_FRONT_LEFT_CAN_ID               = 2;
    //!< The CAN ID for the back right talon
    static final int TALON_BACK_RIGHT_CAN_ID               = 3;
    //!< The CAN ID for the front right talon
    static final int TALON_FRONT_RIGHT_CAN_ID              = 4;
    
    //!< The PWM for the wedge arm
    static final int SPARK_WEDGE_ARM_PWM                   = 0;
    //!< The PWM for the roller arm
    static final int SPARK_ROLLER_ARM_PWM                  = 1;
    //!< The PWM for the roller
    static final int TALON_ROLLER_PWM                      = 2;
    
    //@warning In order for all of the digital I/O to fit the addon must be on the roboRIO
    //!< The DIO for the roller arm's extended (out of the robot) limit switch
    static final int ROLLER_ARM_EXTENDED_DIGITAL_INPUT     = 0;
    //!< The DIO for the roller arm's retracted (inside the robot) limit switch
    static final int ROLLER_ARM_RETRACTED_DIGITAL_INPUT    = 1;
    //!< The DIO for the wedge arm's extended (out of the robot) limit switch
    static final int WEDGE_ARM_EXTENDED_DIGITAL_INPUT      = 2;
    //!< The DIO for the wedge arm's retracted (inside the robot) limit switch
    static final int WEDGE_ARM_RETRACTED_DIGITAL_INPUT     = 3;
    //!< The DIO for the limit switch between the wedge and roller arms to synchronize their lifting
    static final int ROLLER_WEDGE_SYNCRONIZED_INPUTS       = 4;
    //!< The DIO for the left encoder's A
    static final int LEFT_ENCODER_A_DIGITAL_INPUT 	       = 5;
    //!< The DIO for the left encoder's B
    static final int LEFT_ENCODER_B_DIGITAL_INPUT          = 6;
    //!< The DIO for the left encoder's index
    static final int LEFT_ENCODER_INDEX_DIGITAL_INPUT      = 7;
    //!< The DIO for the right encoder's A
    static final int RIGHT_ENCODER_A_DIGITAL_INPUT         = 8;
    //!< The DIO for the left encoder's B
    static final int RIGHT_ENCODER_B_DIGITAL_INPUT         = 9;
    //!< The DIO for the right encoder's index
    static final int RIGHT_ENCODER_INDEX_DIGITAL_INPUT     = 10;
    
    //!< The USB ID that the left joystick will be in
    static final int LEFT_JOYSTICK_ID                      = 0;
    //!< The USB ID that the right joystick will be in
    static final int RIGHT_JOYSTICK_ID                     = 1;
    //!< The USB ID that the XBox controller will be in
    static final int XBOX_JOYSTICK_ID                      = 2;
    
    //!< The front left Talon SRX
	CANTalon talonSRX_FL;
    //!< The back left Talon SRX
	CANTalon talonSRX_BL;
    //!< The front right Talon SRX
	CANTalon talonSRX_FR;
    //!< The back right Talon SRX
    CANTalon talonSRX_BR;
    //!< The drive system controller
    RobotDrive driveSystem;
    
    //!< The joystick for the left hand
    Joystick leftStick;
    //!< The variable to store and manipulate the input fo the left stick
    double leftInput;
    //!< The joystick for the right hand
    Joystick rightStick;
    //!< The variabke to store and manipulate the input for the right stick
    double rightInput;
    
    //!< The joystick (XBox controller) for the helmsman
    Joystick xbox;
    //!< The XBox controller's left Y axis
    private final int XBOX_LEFT_Y_AXIS   = 1;
    //!< The XBox controller's left trigger
    private final int XBOX_LEFT_TRIGGER  = 2;
    //!< The XBox controller's right Y axis
    private final int XBOX_RIGHT_Y_AXIS  = 5;
    //!< The XBox controller's right trigger
    private final int XBOX_RIGHT_TRIGGER = 3;
    
    //!< The encoder for the left side of the robot
    Encoder leftEncoder;
    //!< The encoder for the right side of the robot
    Encoder rightEncoder;
    
    //!< The limit switch for detecting if the roller arm is all the way in the robot
    DigitalInput rollerArmRetracted;
    //!< The limit switch for detecting if the roller arm is all the way out of the robot
    DigitalInput rollerArmExtended;
    //!< The Talon SR to control the roller arm
    Talon rollerArm;
    // The Talon SR to control the roller
    Talon roller;
    
    //!< The limit switch for detecting if the wedge arm is all the way in the robot
    DigitalInput wedgeArmRetracted;
    //!< The limit switch for detecting if the wedge arm is all the way out of the robot
    DigitalInput wedgeArmExtended;
    //!< The Talon SR to control the wedge arm
    Talon wedgeArm;
    
    //!< The limit switch to detect if the wedge and the roller arm are moving in sync
    DigitalInput rollerWedgeSync;
    
    //!< The integer to store how many cycles of code the roller arm has been fully inside the robot
    private int rollerArmIn;
    //!< The integer to store how many cycles of code the roller arm has been fully outside the robot
    private int rollerArmOut;
    //!< The integer to store how many cycles of code the wedge arm has been fully inside the robot
    private int wedgeArmIn;
    //!< The integer to store how many cycles of code the wedge arm has been fully outside the robot
    private int wedgeArmMax;
    //!< The integer to store how many cycles of code the wedge and roller arm have been together
    private int rollerWedgeMove;
    
    //!< The double to store how long the match has gone on
    private double deltaTime  = 0;
    //!< The boolean to say whether or not to stop the drive system in autonomous
    private boolean stopDrive = false;
    //!< The camera server to send images from the camera to the Smart Dashboard
    CameraServer server;
    
    //!< The integer to store which autonomous mode was selected
    private int autonomousSelection = 0;
    //!< The sendable chooser for autonomous modes
    private SendableChooser autoChooser;
    
    //!< Records if the wedge has finished moving
    private boolean wedgeDone;
    //!< Records if the robot has finished driving forward
    private boolean driveForwardDone = false;
    //!< Records if the robot should drive after the wedge
    private boolean driveAfterWedge  = false;
    
    //!< The speed to set the wedge when it should move
    private double autoWedgeSpeed = .4;
    //!< The value to set the wedge speed to
    private double wedgeSet   	  = 0;
    //!< Amount of time to use the wedge
    private int autoWedgeTime 	  = 1500;
    
    //!< Speed to run the drive
    private double autoDriveSpeed = .6;
    //!< Variable holding the speed for the drive's left side
    private double autonomousDriveLeft  = 0;
    //!< Variable holding the speed for the drive's right side
    private double autonomousDriveRight = 0;
    //!< Amount of time to drive in autonomous
    private int autoDriveTime 	  = 3500;
    
    //!< Holds the calculated max time that autonomous might run
    private int calcualtedMaxTime;
    
    /** @brief Initialize all the parts of the robot's system.
     *  @details This is where all parts of the robot are set up. To follow the format please declare the part above
     *  and than use a different variable for any of its IDs.
     *  @warning The encoders and limit switches are currently not being used on the robot, they should be implemented
     *  right away
     */
    public void robotInit() { //Code that runs when the robot starts, only runs once
		talonSRX_FL         = new CANTalon(TALON_FRONT_LEFT_CAN_ID); //Motor controllers
		talonSRX_BL         = new CANTalon(TALON_BACK_LEFT_CAN_ID);
		talonSRX_FR         = new CANTalon(TALON_FRONT_RIGHT_CAN_ID);
		talonSRX_BR         = new CANTalon(TALON_BACK_RIGHT_CAN_ID);
		driveSystem         = new RobotDrive(talonSRX_FR, talonSRX_BR, talonSRX_FL, talonSRX_BL); //Drive system, using tank drive this year
        leftStick           = new Joystick(LEFT_JOYSTICK_ID); //Joysticks
        rightStick          = new Joystick(RIGHT_JOYSTICK_ID);
        
        xbox = new Joystick(XBOX_JOYSTICK_ID);
        
        leftEncoder = new Encoder(LEFT_ENCODER_A_DIGITAL_INPUT, LEFT_ENCODER_B_DIGITAL_INPUT,
                                  LEFT_ENCODER_INDEX_DIGITAL_INPUT);
        rightEncoder = new Encoder(RIGHT_ENCODER_A_DIGITAL_INPUT, RIGHT_ENCODER_B_DIGITAL_INPUT,
                                   RIGHT_ENCODER_INDEX_DIGITAL_INPUT);
        
        rollerArmExtended     = new DigitalInput(ROLLER_ARM_EXTENDED_DIGITAL_INPUT); //Roller arm = top bar with rollers
        rollerArmRetracted    = new DigitalInput(ROLLER_ARM_RETRACTED_DIGITAL_INPUT);
        rollerArm             = new Talon(SPARK_ROLLER_ARM_PWM);
        roller                = new Talon(TALON_ROLLER_PWM);
        
        wedgeArmRetracted     = new DigitalInput(WEDGE_ARM_RETRACTED_DIGITAL_INPUT); //Wedge arm = bottom arm to scoop ball
        wedgeArmExtended      = new DigitalInput(WEDGE_ARM_EXTENDED_DIGITAL_INPUT);
        wedgeArm              = new Talon(SPARK_WEDGE_ARM_PWM);
        
        server = CameraServer.getInstance(); //Camera server, camera located directly below arms
        server.setQuality(50);
        server.startAutomaticCapture("cam0");
        
        autoChooser = new SendableChooser();
        autoChooser.addDefault("No autonomous", 0);
        autoChooser.addObject("Straight 3 seconds", 1);
        //autoChooser.addObject("Wedge down + straight !!!DO NOT SELECT!!!", 2);
    }
    
    /** @brief This is where autonomous is initialized.
     *  @details Any times set for counting the start of the match are kept here.
     */
    public void autonomousInit() {
        deltaTime = System.currentTimeMillis(); //Establish start time for autonomous
        autonomousSelection = (int) autoChooser.getSelected();
    }
    
    /** @brief This function runs periodically throughout autonomous
     *
     */
    public void autonomousPeriodic() {
        double currentTime = System.currentTimeMillis();
        
        autonomousSetBoth(0);
        
        if (autonomousSelection == 0) {
            autonomousSetBoth(0);
        } else if (autonomousSelection == 1) {
            if (currentTime - deltaTime < autoDriveTime) {
                autonomousSetBoth(autoDriveSpeed);
            }
        } else if (autonomousSelection == 2) {
        	if (getState(wedgeArmExtended, wedgeArmMax) <=15) { //In order for this to work a limit switch must be added
                wedgeSet = -autoWedgeSpeed;
        	} else {
                autonomousSelection = 1;
        		wedgeSet = 0;
        		wedgeDone = true;
        	}
        	wedgeArm.set(wedgeSet);
    	}
        
        driveSystem.tankDrive(autonomousDriveLeft, autonomousDriveRight); //Set drive speeds
        Timer.delay(0.005); //Motor delay
    }
    
    /** @brief Sets the speed of the drive in autonomous to be even.
     *  @param speed The speed to set the drive to
     */
    public void autonomousSetBoth(double speed) {
        autonomousDriveLeft  = speed;
        autonomousDriveRight = speed;
    }
    
    /** @brief This is where teleoperation is initialized.
     *
     */
    public void teleopInit() {
    	//Sorry, nothing is here right now
    }
    
    /** @brief This function is run periodically throughout teleoperation
     *  @details All functions and user control should be put here. If an operation can be condensed into its own
     *  function, make it its own function (this is so that we can separate controls and hopefully debug easier).
     */
    public void teleopPeriodic() {
        runDrive(); //Run the main drive control
        
        double rollerInput     = xbox.getRawAxis(XBOX_LEFT_Y_AXIS);
        rollerArmIn = getState(rollerArmRetracted, rollerArmIn);
        rollerArmOut = getState(rollerArmExtended, rollerArmOut);
        double rollerArmSet    = (rollerArmIn >= 30) ? ((rollerInput * Math.abs(rollerInput))/2) : 0;
        rollerArmSet           = (rollerArmOut >= 30) ? (rollerInput * Math.abs(rollerInput))/2 : rollerArmSet;
        rollerArm.set(rollerArmSet);
        double rollerSet       = (xbox.getRawAxis(XBOX_LEFT_TRIGGER) >= 0.1) ? xbox.getRawAxis(2) : 0;
        rollerSet              = (xbox.getRawAxis(XBOX_RIGHT_TRIGGER)>= 0.1) ? xbox.getRawAxis(3) * -1 : rollerSet;
        roller.set(rollerSet);
        
        double wedgeInput      = xbox.getRawAxis(XBOX_RIGHT_Y_AXIS);
        wedgeArmIn = getState(wedgeArmRetracted, wedgeArmIn);
        wedgeArmMax            = getState(wedgeArmExtended, wedgeArmMax);
        //Setting the speed of the wedge arm -- half of input wasn't enough power, so its 60% power
        double wedgeSet        = (wedgeArmIn >= 30) ? (wedgeInput*0.6) : 0;
        wedgeSet               = (wedgeArmMax >= 30) ? (wedgeInput*0.6) : wedgeSet;
        wedgeArm.set(wedgeSet);
        
        Timer.delay(0.005);
    }
    
    /** @brief This function controls the driver control of the robot.
     *  @details This function impliments any controls that relate to the main driver are here.
     *  @see straightMode()
     *  @see safeMode()
     */
    public void runDrive() {
        leftInput  = leftStick.getY()  * -1;
        rightInput = rightStick.getY() * -1;
        
        if (rightStick.getRawButton(1)) {
            straightMode();
        }
        
        if (leftStick.getRawButton(1)) {
            safeMode();
        }
        
        driveSystem.tankDrive(leftInput, rightInput);
    }
    
    /** @brief Makes the robot drive striahgt.
     *  @details This function takes the input from the left stick and makes the value to set the motors on the right
     *  equal to that, making the robot drive stright (or at least power the motors exactly the same).
     */
    public void straightMode() {
        leftInput = rightInput;
    }
    
    /** @brief Reduces the speed of the robot by half
     *  @details Redcues the speed of the right and left side of the robot by 1/2, this is done by dividing both of the
     *  inputs
     */
    public void safeMode() {
        leftInput /= 2;
        rightInput /= 2;
    }
    
    /** @brief Determine if the limit switch is actually made or not
     *  @details Determines if the limit switch has actually been made by putting the amount of cycles in the code into
     *  an integer which can be checked to see if an action should be taken or not
     *  @param limit The limit switch to monitor
     *  @param madeCheck The int to store the number of cycles that the switch has been made
     *  @return madeCheck, the number of cycles that the switch has been made
     */
    public int getState(DigitalInput limit, int madeCheck) {
        
        if (limit.get()) {
            ++madeCheck;
        } else {
            madeCheck = 0;
        }
        
        return madeCheck;
    }
}





