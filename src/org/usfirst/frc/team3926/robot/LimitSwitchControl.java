package org.usfirst.frc.team3926.robot;


import edu.wpi.first.wpilibj.DigitalInput;

public class LimitSwitchControl {
    private final DigitalInput limit; //The limit switch that we are monitoring
    private int madeCheck; //The int to store our debounce count

    public LimitSwitchControl(DigitalInput Limit) {
        this.limit = Limit;
    }
    ////END LimitSwitchControl constructor////

    public boolean getState() {
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

}
////END LimitSwitchControl class////
