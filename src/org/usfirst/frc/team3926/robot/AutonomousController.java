package org.usfirst.frc.team3926.robot;


import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.RobotDrive;

public class AutonomousController {

    Encoder distanceEncoder;
    RobotDrive autonomousSystem;

    double autoSpeed = 0; //This stuff is used to control the robot during autonomous
    double autoRotate = 0;

    private double deltaTime = 0; //This helps measure the time for rotations
    private final double ninetyDegreeTime = 10; //In milliseconds //TODO test this
    private final int magicAngleTime = 10; //Angle to turn to low goal in milliseconds


    public AutonomousController(Encoder distanceEncoder, RobotDrive driveSystem) {
        this.distanceEncoder = distanceEncoder;
        this.autonomousSystem = driveSystem;
    }
    ////END AutonomousController constructor////


    public AutonomousFunctions[] lowBar = new AutonomousFunctions[] {
            new AutonomousFunctions("straight", 116),
            new AutonomousFunctions("turnLeft", magicAngleTime), //TODO find this out
            new AutonomousFunctions("straight", 108),
            new AutonomousFunctions("done", 000),
    };

    public AutonomousFunctions[] positionTwo = new AutonomousFunctions[] {
            new AutonomousFunctions("straight", 116),
            new AutonomousFunctions("turnLeft", ninetyDegreeTime), //TODO find this out
            new AutonomousFunctions("straight", 48),
            new AutonomousFunctions("turnRight", magicAngleTime - ninetyDegreeTime), //TODO check this
            new AutonomousFunctions("straight", 108),
            new AutonomousFunctions("done", 000)
    };

    public AutonomousFunctions[] crowdChoice = new AutonomousFunctions[] {
            new AutonomousFunctions("straight", 116),
            new AutonomousFunctions("turnLeft", ninetyDegreeTime),
            new AutonomousFunctions("straight", 108),
            new AutonomousFunctions("turnRight", magicAngleTime - ninetyDegreeTime), //TODO check this
            new AutonomousFunctions("straight", 108),
            new AutonomousFunctions("done", 000)
    };

    public AutonomousFunctions[] four = new AutonomousFunctions[] {
            new AutonomousFunctions("straight", 116),
            new AutonomousFunctions("turnRight", ninetyDegreeTime),
            new AutonomousFunctions("straight", 48),
            new AutonomousFunctions("turnRight", magicAngleTime - ninetyDegreeTime), //TODO check this
            new AutonomousFunctions("straight", 108),
            new AutonomousFunctions("done", 000)
    };

    public AutonomousFunctions[] five = new AutonomousFunctions[] {
            new AutonomousFunctions("striahgt", 116),
            new AutonomousFunctions("turnLeft", magicAngleTime),
            new AutonomousFunctions("straight", 108),
            new AutonomousFunctions("done", 000)
    };

    public AutonomousFunctions[] secretPassage = new AutonomousFunctions[] {
            //TODO fill this out
    };

    //TODO figure out what to do about the lift thing and the door

    int currentIndex = 0;
    ////END variable creation////

    /**
     *
     * @param handling The position in autonomous to run: lowBar, positionTwo, crowdChoice, four, five, secretPassage
     */
    public void runAutonomous(AutonomousFunctions[] handling) {
        String command = handling[currentIndex].getCommand();
        double commandUnit = handling[currentIndex].getCommandUnit();

        if (command == "straight") {
            if (distanceEncoder.getDistance() < commandUnit) {
                autoSpeed = .5;
            } else {
                endCommand();
            }
        } else if (command == "turnLeft") {
            if (deltaTime == 0) {
                deltaTime = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - deltaTime < commandUnit) {
                autoRotate = .25; //TODO test if left is positive or negative
            } else {
                endCommand();
            }
        } else if (command == "turnRight") {
            if (deltaTime == 0) {
                deltaTime = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - deltaTime < commandUnit) {
                autoRotate = -0.25; //TODO test if right is positive or negative
            } else {
                endCommand();
            }
        } else if (command == "done") {
            SmartDashboard.putBoolean("Autonomous Done", true);
        } else {
            SmartDashboard.putBoolean("Glitch", true);
        }

        autonomousSystem.mecanumDrive_Polar(autoSpeed, 0, autoRotate);
    }
    ////END runAutonomous()////

    public void endCommand() {
        distanceEncoder.reset();
        ++currentIndex;
        autoRotate = 0;
        deltaTime = 0;

        SmartDashboard.putNumber("Current Index", currentIndex + 0.00);
    }
    ////END endCommand()////
}
////END AutonomousController class////
