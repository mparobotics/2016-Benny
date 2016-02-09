package org.usfirst.frc.team3926.robot;


import edu.wpi.first.wpilibj.CANTalon;

public class ArmControl {

    CANTalon motor;

    /**
     *
     * @param motor The motor controller for the wedge arm
     */
    public ArmControl(CANTalon motor) {
        this.motor = motor;
    }

    /**
     *
     * @param retractLimitState The state of the retracted limit switch returned by the LimitSwitchControl
     * @param extendLimitState The state of the extended limit switch returned by LimitSwitchControl
     * @param speed The speed that the arm should move, returned by a XBox joystick value
     */
    public void runMotor(boolean retractLimitState, boolean extendLimitState, double speed) {
        if (!retractLimitState && speed < 0) { //It must be less than zero so that it can move away from the limit
            motor.set(speed);
        } else if (!extendLimitState && speed > 0) {
            motor.set(speed);
        } else {
            motor.set(0);
        }
    }
}
