package org.usfirst.frc.team3926.robot;

/**
 * Created by wkluge17 on 2/9/16.
 */
public class AutonomousFunctions {

    private int eventIndex = 0;
    private int nextEvent = 0;
    private String command;
    private int commandUnit;

    public AutonomousFunctions(String command, int commandUnit) {
        //this.eventIndex = eventIndex;
        //this.nextEvent = nextEvent;
        this.command = command;
        this.commandUnit = commandUnit;
    }
    ////END AutonomousFunctions constructor////

    public int getEventIndex() {
        return eventIndex;
    }

    public int getNextEvent() {
        return nextEvent;
    }

    public String getCommand() {
        return command;
    }

    public int getCommandUnit() {
        return commandUnit;
    }

    public void setEventIndex(int index) {
        eventIndex = index;
    }
}
////END AutonomousFunctions class////
