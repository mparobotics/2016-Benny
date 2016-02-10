package org.usfirst.frc.team3926.robot;


import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.lang.reflect.Array;

public class AutonomousController {

    Encoder distanceEncoder;

    public AutonomousController(Encoder distanceEncoder) {
        this.distanceEncoder = distanceEncoder;
    }
    ////END AutonomousController constructor////


    double autoSpeed = 0; //This stuff is used to control the robot during autonomous
    double autoRotate = 0;

    private double deltaTime = 0; //This helps measure the time for rotations

    /**
     * THIS TIME IS IN MILISECONDS...MAKE SURE YOU USE MILISECONDS
     */
    static double ninetyDegreeTime = 10; //TODO test this
    static int magicAngleTime = 10; //This is the one angle that rotates us towards the low goal

    //TODO create a handler for which array to use

    private AutonomousFunctions[] lowBar = new AutonomousFunctions[] {
            new AutonomousFunctions("straight", 116),
            new AutonomousFunctions("turnLeft", magicAngleTime),
            new AutonomousFunctions("straight", 108),
            new AutonomousFunctions("done", 000),
    };

    private AutonomousFunctions[] positionTwo = new AutonomousFunctions[] {
            new AutonomousFunctions("straight", 116)
    };

    int currentIndex = 0;
    ////END variable creation////

    public void autonomousRun() {

        //TODO put in autonomous drive function
    }
    ////END autonomousRun()////

    private boolean functionHandler(AutonomousFunctions[] handling) {
        boolean commandState = false;
        String command = handling[currentIndex].getCommand();
        int commandUnit = handling[currentIndex].getCommandUnit();

        if (command == "straight") {
            if (distanceEncoder.getDistance() < commandUnit) {
                autoSpeed = .5;
            } else {
                distanceEncoder.reset();
                ++currentIndex;
                autoRotate = 0; //Set the speed to 0 to prevent the robot from rotating more
                deltaTime = 0; //Reset deltaTime
            }
        }
        return commandState = false;
    }
    ////END functionHanlder()////

    /**
     *
     * @param rotateTime
     * @param rotateRight
     */
    /*public boolean autoRotate(double rotateTime, boolean rotateRight) { //X corresponds to our marking on the board not the axis
        rotateDone = false;

        if (deltaTime == 0) {
            deltaTime = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - deltaTime < rotateTime) {
            autoRotate = 0.25;
            if (rotateRight) {
                autoRotate *= -1;
            }
        } else {
            distanceEncoder.reset();
            autoRotate = 0; //Set the speed to 0 to prevent the robot from rotating more
            deltaTime = 0;
            rotateDone = true;
        }

        return rotateDone;
    }*/
    ////END autoRotate()////

    public void autoDone() {
        SmartDashboard.putString("Autonomous Status", "Done");;
    }
    ////END autoDone()////
}
////END AutonomousController class////
