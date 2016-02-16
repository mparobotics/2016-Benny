
package org.usfirst.frc.team3926.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;


public class Robot extends IterativeRobot {
	CANTalon talonSRX_FR; //Front Right
    CANTalon talonSRX_FL; //Front Left
    CANTalon talonSRX_BR; //Back Right
    CANTalon talonSRX_BL; //Back Left
    RobotDrive driveSystem; //Controller class for operating TankDrive
    Joystick leftStick; //Joystick object for the left hand
    double leftInput;
    Joystick rightStick; //Joystick object for the right hand
    double rightInput;
    
    Joystick XBox; //The joystick for the supplimentary driver

    Encoder leftEncoder; //Encoder to help us measure our distance traveled
    Encoder rightEncoder; //Encoder on the right side

    DigitalInput rollerArmRetracted; //Limit switch to prevent the roller arm from trying to go to far back
    DigitalInput rollerArmExtended; //Limit switch to stop the roller's arm from over-extending
    CANTalon rollerArm; //Motor to move the roller's arm
    CANTalon roller; //Motor to spin the roller
    
    DigitalInput wedgeArmLimitRetracted; //Limit switch to prevent the wedge's arm from trying to go to far back
    DigitalInput wedgeArmLimitExtended; //Limit switch to prevent the wedge's arm from over-extending
    CANTalon wedgeArm; //Motor to control the wedge
	
    public void robotInit() {
        
    }
    ////END robotInit()////
    
    public void autonomousInit() {
    	
    }
    ////END autonomousInit()////

    public void teleopPeriodic() {
        
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
	    private int rollerArmMin; //The int to store our debounce count
	    private int rollerArmMax;
	    private int wedgeArmMin;
	    private int wedgeArmMax;
	    
	    public boolean getState(DigitalInput limit, int madeCheck) {
	        boolean made;

	        if (limit.get()) {
	            ++madeCheck; //A limit switch is "made" when it is activated

	            if (madeCheck >= 30) {
	                made = true;
	            } else {
	                made = false;
	            }
	        } else {
	            made = false;
	            madeCheck = 0;
	        }

	        return made;
	    }
	    ////END limitState()////
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
	    
	    private String convertAction(int action) {
	    	String actionString = "";
	    	
	    	switch(action) {
	    	case 1:
	    		actionString = "straight";
	    		break;
	    	case 2:
	    		actionString = "turnLeft";
	    		break;
	    	case 3:
	    		actionString = "turnRight";
	    		break;
	    	case 4:
	    		actionString = "done";
	    		break;
	    	default:
	    		actionString = "broken";
	    	}
	    	
	    	return actionString;
	    }
	    
	    private final int[] lowBarActions = {
	    		1, 2, 1, 4, //Order of actions
	    		116, magicAngleTime, 
	    		
	    };
	    
	    public String lowbarAct(int stage) {
	    	String action = "";
	    	
	    	
	    	
	    	return action;
	    }
	////Stop AutonomousController////
}
////END Robot class////





