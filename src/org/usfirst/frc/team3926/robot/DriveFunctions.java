package org.usfirst.frc.team3926.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;

public class DriveFunctions {
    Joystick LeftStick; //The local Joystick variable
    double leftInput; //Holding variable to allow us to manipulate leftStick's input
    Joystick RightStick;
    double rightInput; //Holding variable to allow us to manipulate the rightStick's input

    RobotDrive driveControl;

    public DriveFunctions(Joystick leftStick, Joystick rightStick, RobotDrive driveSystem) {
        this.LeftStick = leftStick;
        this.RightStick = rightStick;
        driveControl = driveSystem;
    }

    public void masterFunction() {
        leftInput = LeftStick.getY();
        rightInput = RightStick.getY();

        if (RightStick.getRawButton(1)) {
            straightMode();
        }

        if (LeftStick.getRawButton(1)) {
            safteyMode();
        }

        driveControl.tankDrive(leftInput, rightInput);
    }

    public void straightMode() {
        leftInput = rightInput;
    }

    public void safteyMode() {
        leftInput /= 2;
        rightInput /= 2;
    }
}
