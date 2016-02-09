
package org.usfirst.frc.team3926.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;

public class Robot extends IterativeRobot {
    CameraController camera; //Allows us to control the camera

    CANTalon talonSRX_FR; //Front Right
    CANTalon talonSRX_FL; //Front Left
    CANTalon talonSRX_BR; //Back Right
    CANTalon talonSRX_BL; //Back Left
    RobotDrive driveSystem; //Controller class for operating TankDrive

    DigitalInput rollerArmRetracted; //Limit switch to prevent the roller arm from trying to go to far back
    DigitalInput rollerArmExtended; //Limit switch to stop the roller's arm from over-extending
    CANTalon rollerArm; //Motor to move the roller's arm
    CANTalon roller; //Motor to spin the roller


    DigitalInput wedgeArmLimitMin; //Limit switch to prevent the wedge's arm from trying to go to far back
    DigitalInput wedgeArmLimitMax; //Limit switch to prevent the wedge's arm from over-extending
    CANTalon wedgeArm; //Motor to control the wedge

    Joystick leftStick; //Joystick object for the left hand
    Joystick rightStick; //Joystick object for the right hand
    Joystick XBox;

    Encoder distanceEncoder; //Encoder to help us measure our distance traveled

    LimitSwitchControl wedgeExtendLimit;
    LimitSwitchControl wedgeRetractLimit;
    LimitSwitchControl rollerExtendLimit;
    LimitSwitchControl rollerRetractLimit;

    ArmControl wedgeArmController;
    ArmControl rollerArmController;


    /**
     * Initializes the variables we declared above with their physical ID
     * 	- CAN IDs are set in the roboRIO web interface
     */
    public void robotInit() {
        camera = new CameraController();

        talonSRX_BL = new CANTalon(VariableStore.TALON_BACK_LEFT_CAN_ID);
        talonSRX_FL = new CANTalon(VariableStore.TALON_FRONT_LEFT_CAN_ID);
        talonSRX_BR = new CANTalon(VariableStore.TALON_BACK_RIGHT_CAN_ID);
        talonSRX_FR = new CANTalon(VariableStore.TALON_FRONT_RIGHT_CAN_ID);

        driveSystem = new RobotDrive(talonSRX_BL, talonSRX_FL, talonSRX_BR, talonSRX_BR);

        rollerArmRetracted = new DigitalInput (VariableStore.ROLLER_ARM_RETRACTED_DIGITAL_INPUT);
        rollerArmExtended = new DigitalInput (VariableStore.ROLLER_ARM_EXTENDED_DIGITAL_INPUT);
        rollerArm = new CANTalon(VariableStore.TALON_ROLLER_ARM_CAN_ID);
        roller = new CANTalon(VariableStore.TALON_ROLLER_CAN_ID);

        wedgeArmLimitMin = new DigitalInput(VariableStore.WEDGE_ARM_RETRACTED_DIGITAL_INPUT);
        wedgeArmLimitMax = new DigitalInput(VariableStore.WEDGE_ARM_EXTENDED_DIGITAL_INPUT);
        wedgeArm = new CANTalon(VariableStore.TALON_WEDGE_ARM_CAN_ID);

        leftStick = new Joystick (VariableStore.LEFT_JOYSTICK_ID);
        rightStick = new Joystick (VariableStore.RIGHT_JOYSTICK_ID);
        XBox = new Joystick(VariableStore.XBOX_JOYSTICK_ID);

        wedgeExtendLimit = new LimitSwitchControl(wedgeArmLimitMax);
        wedgeRetractLimit = new LimitSwitchControl(wedgeArmLimitMin);
        rollerExtendLimit = new LimitSwitchControl(rollerArmExtended);
        rollerRetractLimit = new LimitSwitchControl(rollerArmRetracted);

        wedgeArmController = new ArmControl(wedgeArm);
        rollerArmController = new ArmControl(rollerArm);
    }
    ////END robotInit()////

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }
    ////END autonomousPeriodic()////

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        boolean wedgeRan;

        if (XBox.getRawButton(1)) { //A button
            wedgeArmController.runMotor(wedgeExtendLimit.getState(), wedgeRetractLimit.getState(), XBox.getY());
            wedgeRan = true;
        } else {
            wedgeArm.set(0);
            wedgeRan = false;
        }

        if (XBox.getRawButton(2) || !wedgeRan) {
            rollerArmController.runMotor(rollerExtendLimit.getState(), rollerRetractLimit.getState(), XBox.getY());
        } else {
            rollerArm.set(0);
        }

        camera.run();
    }
    ////END teleopPeriodic()////

    /**
     * This function is called periodically during test mode
     * We will use this to test individual functions of our robot
     */
    public void testPeriodic() {

    }
    ////END testPeriodic()////

}
////END Robot class////
