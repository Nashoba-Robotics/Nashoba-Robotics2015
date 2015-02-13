package edu.nr.robotics.subsystems.backElevator;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.custom.CantTalon;
import edu.nr.robotics.custom.MotorPair;
import edu.nr.robotics.subsystems.backElevator.commands.BackElevatorIdleCommand;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BackElevator extends Subsystem implements PIDOutput, PIDSource
{

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
    	super("Back Elevator");
        // setSetpoint() -  Sets where the PID controller should move the system to
    	talon1 = CantTalon.newCantTalon(RobotMap.backElevatorTalon1);
    	talon2 = CantTalon.newCantTalon(RobotMap.backElevatorTalon2);
    	motors = new MotorPair(talon1, talon2);
    	
    	motors.enableCantTalonLimitSwitch(true, true);
    	
		potentiometer = new AnalogPotentiometer(RobotMap.POTENTIOMETER_BACK_ELEVATOR);
    }
    
    public void initDefaultCommand() {
    	setDefaultCommand(new BackElevatorIdleCommand());
    }
    
    protected double returnPIDInput() 
    {
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
	
    public void setElevatorSpeed(double speed)
    {
    	motors.set(-speed);
    }
	
	public static void init()
	{
		if(singleton == null)
		{
			singleton = new BackElevator();
			SmartDashboard.putData("Back Elevator Subsystem", singleton);
		}
	}

	@Override
	public double pidGet()
	{
		return potentiometer.get();
	}

	@Override
	public void pidWrite(double output) 
	{
		setElevatorSpeed(output);
	}
}
