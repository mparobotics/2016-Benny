package org.usfirst.frc.team3926.robot;

/**
 * Created by wkluge17 on 2/9/16.
 */
public class AutonomousFunctions {

    private String command;
    private double commandUnit;

    public AutonomousFunctions(String command, double commandUnit) {
        this.command = command;
        this.commandUnit = commandUnit;
    }
    ////END AutonomousFunctions constructor////

    public String getCommand() { //Get the command of the selected index in the selected array
        return command;
    }

    public double getCommandUnit() { //Get the unit (seconds or feet)
        return commandUnit;
    }
}
////END AutonomousFunctions class////
