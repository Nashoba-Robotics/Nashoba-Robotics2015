package edu.nr.robotics;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap 
{
	public static final int leftDriveTalon1 = 12;
	public static final int leftDriveTalon2 = 13;
	
	public static final int rightDriveTalon1 = 0;
	public static final int rightDriveTalon2 = 3;
	
	public static final int HDriveTalon = 4;
	
	public static final int frontElevatorTalon1 = 2;
	public static final int frontElevatorTalon2 = 1;
	public static final int backElevatorTalon1 = 15;
	public static final int backElevatorTalon2 = 14;
	
	public static final int pneumaticsModule = 1;
	public static final int doubleSolenoidForward = 0;
	public static final int doubleSolenoidReverse = 1;
	
	public static final int IRSensor = 1, IRSensor2 = 2;
	
	public static final int ENCODER_LEFT_A = 7, ENCODER_LEFT_B = 6;
	public static final int ENCODER_RIGHT_A = 9, ENCODER_RIGHT_B = 8;
	
	public static final int BUMPER_BUTTON_LEFT = 4, BUMPER_BUTTON_RIGHT = 5;
	
	public static final int LASER_RANGING_MODULE = 0x62; //Provided by documentation.

	//SET THIS
	public static final int POTENTIOMETER_FRONT_ELEVATOR = 3;
	public static final int POTENTIOMETER_BACK_ELEVATOR = 0;
}
