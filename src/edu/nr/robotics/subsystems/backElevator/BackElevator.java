package edu.nr.robotics.subsystems.backElevator;

import edu.nr.robotics.CantTalon;
import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BackElevator extends PIDSubsystem {

	//These needs to be set
    private static final double HEIGHT_MAX = 0;
	public static final int HEIGHT_HOLD = 0;
	public static final int HEIGHT_OBTAIN_STEP = 0;
	public static final int HEIGHT_OBTAIN_FLOOR = 0;
	public static final int HEIGHT_CLOSED = 0;

	static BackElevator singleton;
    
	AnalogPotentiometer potentiometer;
	CantTalon talon;
    
    public BackElevator() {
    	super("Back Elevator", 0, 0, 0);
        // setSetpoint() -  Sets where the PID controller should move the system to
        enable();
    	talon = new CantTalon(RobotMap.backElevatorTalon);
		potentiometer = new AnalogPotentiometer(RobotMap.POTENTIOMETER_BACK_ELEVATOR, HEIGHT_MAX, -HEIGHT_MAX/2);
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;
    	return 0.0;
    }
    
    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);
    }
	public static BackElevator getInstance()
    {
		init();
        return singleton;
    }
	
	public static void init()
	{
		if(singleton == null)
		{
			singleton = new BackElevator();
			SmartDashboard.putData("Back Elevator Subsystem", singleton);
		}
	}

	
}
