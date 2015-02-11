package edu.nr.robotics.subsystems.backElevator;

import edu.nr.robotics.CantTalon;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.backElevator.commands.BackElevatorIdleCommand;
import edu.nr.robotics.subsystems.drive.MotorPair;
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
	CantTalon talon1;
	CantTalon talon2;
	MotorPair motors;
    
    public BackElevator() {
    	super("Back Elevator", 0, 0, 0);
        // setSetpoint() -  Sets where the PID controller should move the system to
        enable();
    	talon1 = new CantTalon(RobotMap.backElevatorTalon1);
    	talon2 = new CantTalon(RobotMap.backElevatorTalon2);
    	motors = new MotorPair(talon1, talon2);

		potentiometer = new AnalogPotentiometer(RobotMap.POTENTIOMETER_BACK_ELEVATOR, HEIGHT_MAX, -HEIGHT_MAX/2);
    }
    
    public void initDefaultCommand() {
    	setDefaultCommand(new BackElevatorIdleCommand());
    }
    
    protected double returnPIDInput() {
        return potentiometer.get();
    }
    
    protected void usePIDOutput(double output) {
        motors.set(output);
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
