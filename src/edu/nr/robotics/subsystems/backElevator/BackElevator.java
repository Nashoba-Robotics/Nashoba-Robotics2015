package edu.nr.robotics.subsystems.backElevator;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.backElevator.commands.BackElevatorIdleCommand;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BackElevator extends Subsystem implements PIDOutput, PIDSource
{

	//These needs to be set
	public static final double HEIGHT_HOLD = 3;
	public static final double HEIGHT_OBTAIN_STEP = 1.2;//1.31;
	public static final double HEIGHT_SLOW_DOWN = 0.73;
	public static final double HEIGHT_CLOSED = 0;
	
	public static final double HEIGHT_BINS_GRAZE_GROUND = 1.5;
	public static final double HEIGHT_BIN_LOWERED_FULLY = 0.697;
	public static final double HEIGHT_BIN_JUST_ABOVE_GROUND = 2;
	
	private static BackElevator singleton;
    
	AnalogPotentiometer potentiometer;
	private final double POT_MIN = 1 - 0.967;
	private final double POT_MAX = 1 - 0.358;
	private final double POT_RANGE = (36 + 1/8)/12; //Range between max and min in feet
	
	CANTalon talon1, talon2;
    
    public BackElevator() 
    {
    	super("Back Elevator");
    	
    	talon1 = new CANTalon(RobotMap.backElevatorTalon1);
    	talon1.enableLimitSwitch(true, true);
    	
    	talon2 = new CANTalon(RobotMap.backElevatorTalon2);
    	talon2.enableLimitSwitch(true, true);
    	talon2.changeControlMode(CANTalon.ControlMode.Follower);
    	talon2.set(RobotMap.backElevatorTalon1);
    	
		potentiometer = new AnalogPotentiometer(RobotMap.POTENTIOMETER_BACK_ELEVATOR);
    }
    
    public void initDefaultCommand() 
    {
    	setDefaultCommand(new BackElevatorIdleCommand());
    }
    
    protected void usePIDOutput(double output)
    {
        talon1.set(output);
    }
	public static BackElevator getInstance()
    {
		init();
        return singleton;
    }
	
    public void setElevatorSpeed(double speed)
    {
    	talon1.set(-speed);
    }
	
	public static void init()
	{
		if(singleton == null)
		{
			singleton = new BackElevator();
		}
	}

	@Override
	public double pidGet()
	{
		return getScaledPot();
	}
	
	public double getScaledPot()
	{
		//Back Elevator pot is reversed (elevator going up means potentiometer is going down)
		double value = 1 - potentiometer.get();
    	value -= POT_MIN;
    	value = value/(POT_MAX - POT_MIN) * POT_RANGE;
    	
	}

	@Override
	public void pidWrite(double output) 
	{
		setElevatorSpeed(output);
	}
	
	public void dashboardInfo()
	{
		//SmartDashboard.putNumber("Rear pot raw", potentiometer.get());
		SmartDashboard.putNumber("Rear Pot Scaled", getScaledPot());
		SmartDashboard.putNumber("Talon 1 Current", talon1.getOutputCurrent());
		SmartDashboard.putNumber("Talon 2 Current", talon2.getOutputCurrent());
	}
}
