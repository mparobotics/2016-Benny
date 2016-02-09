package org.usfirst.frc.team3926.robot;

public class VariableStore {

    ////CAN Controller Constants////
    final int TALON_BACK_LEFT_CAN_ID 	= 1;
    final int TALON_FRONT_LEFT_CAN_ID	= 2;
    final int TALON_BACK_RIGHT_CAN_ID 	= 3;
    final int TALON_FRONT_RIGHT_CAN_ID 	= 4;
    final int TALON_ROLLER_CAN_ID 		= 5;
    final int TALON_ROLLER_ARM_CAN_ID	= 6;
    final int TALON_WEDGE_ARM_CAN_ID 	= 7;

    ////Digital Input Constants////
    final int ROLLER_ARM_EXTENDED_DIGITAL_INPUT 	= 0;
    final int ROLLER_ARM_RETRACTED_DIGITAL_INPUT	= 1;
    final int WEDGE_ARM_EXTENDED_DIGITAL_INPUT 		= 2;
    final int WEDGE_ARM_RETRACTED_DIGITAL_INPUT		= 3;
    final int ENCODER_A_DIGITAL_INPUT 				= 4;
    final int ENCODER_B_DIGITAL_INPUT 				= 5;
    final int ENCODER_INDEX_DIGITAL_INPUT			= 6;
    final int EMPTRY_DIGITAL_INPUT_SEVEN 			= 7; //add second encoder
    final int EMPTRY_DIGITAL_INPUT_EIGHT 			= 8;
    final int EMPTRY_DIGITAL_INPUT_NINE 			= 9;

    ////Pneumatic Controller Constants////
    static final int EMPTY_PNEUMATIC_ZERO 		= 0;
    static final int EMPTY_PNEUMATIC_ONE 		= 1;
    static final int EMPTY_PNEUMATIC_TWO 		= 2;
    static final int EMPTY_PNEUMATIC_THREE 		= 3;
    static final int EMPTY_PNEUMATIC_FOUR 		= 4;
    static final int EMPTY_PNEUMATIC_FIVE 		= 5;
    static final int EMPTY_PNEUMATIC_SIX 		= 6;
    static final int EMPTY_PNEUMATIC_SEVEN 		= 7;
    static final int EMPTY_PNEUMATIC_EIGHT 		= 8;
    static final int EMPTY_PNEUMATIC_NINE 		= 9;

    ////PWM Constants////
    static final int EMPTY_PWM_ZERO 			= 0;
    static final int EMPTY_PWM_ONE 				= 1;
    static final int EMPTY_PWM_TWO 				= 2;
    static final int EMPTY_PWM_THREE 			= 3;
    static final int EMPTY_PWM_FOUR 			= 4;
    static final int EMPTY_PWM_FIVE 			= 5;
    static final int EMPTY_PWM_SIX 				= 6;
    static final int EMPTY_PWM_SEVEN 			= 7;
    static final int EMPTY_PWM_EIGHT 			= 8;
    static final int EMPTY_PWM_NINE 			= 9;

    ////Joystick Constants////
    final int LEFT_JOYSTICK_ID 	= 0;
    final int RIGHT_JOYSTICK_ID = 1;
    final int XBOX_JOYSTICK_ID = 2;
}
