package edu.nr.robotics.subsystems.frontElevator;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontElevatorIdleCommand;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class FrontElevator extends Subsystem implements PIDSource, PIDOutput
{
	public static FrontElevator singleton;
	
	private static double BELT_SKIP_OFFSET = 0.31;
	
	public static final double HEIGHT_ADJUST_TOTE_ONE = 0.33 + BELT_SKIP_OFFSET;
	public static final double HEIGHT_WAITING = 2.93 + BELT_SKIP_OFFSET;
	public static final double HEIGHT_PICK_UP_TOTE_ONE = 0.02 + BELT_SKIP_OFFSET;
	public static final double HEIGHT_OBTAIN_NOODLE = 0.20 + BELT_SKIP_OFFSET;
	public static final double HEIGHT_PICK_UP_TOTE_TWO = 0.85 + BELT_SKIP_OFFSET;
	public static final double HEIGHT_ADJUST_BIN = 0.44 + BELT_SKIP_OFFSET;
	public static final double HEIGHT_SCORING = 0.5 + BELT_SKIP_OFFSET;
	public static final double HEIGHT_BOTTOM = 0.01 + BELT_SKIP_OFFSET;
	public static final double HEIGHT_BEFORE_TOTE_ADJUST = 1.14 + BELT_SKIP_OFFSET;
	public static final double HEIGHT_RELEASE_BIN_WHILE_GOING_DOWN = 1.7 + BELT_SKIP_OFFSET;
	public static final double HEIGHT_LIFT_4_STACK = 1.299 + BELT_SKIP_OFFSET;
	public static final double HEIGHT_BEFORE_TOTE_LOWERING = 0.00;
	
	private final double MAX_ALLOWED_HEIGHT = 4.2 + BELT_SKIP_OFFSET;
    private final double MIN_ALLOWED_HEIGHT = 0.03 + BELT_SKIP_OFFSET;
	
	public static final double BARREL_ABOVE_FIRST_TOTE = 1.22;
	
	private final double POT_MAX = 0.80; //potentiometer voltage at max position
	private final double POT_MIN = 0.08;//potentiometer voltage at min value
	private final double POT_RANGE = (51)/12d; //Range between max and min in feet

	AnalogPotentiometer potentiometer;
    CANTalon talon1;
    
    private DoubleSolenoid binGrabber;
    private Value binGrabberValue;

    public FrontElevator() 
    {
    	talon1 = new CANTalon (RobotMap.frontElevatorTalon1);
    	CANTalon slave = new CANTalon(RobotMap.frontElevatorTalon2);
    	slave.changeControlMode(CANTalon.ControlMode.Follower);
    	slave.set(talon1.getDeviceID());
    	
		potentiometer = new AnalogPotentiometer(RobotMap.POTENTIOMETER_FRONT_ELEVATOR);
		        
        binGrabber = new DoubleSolenoid(RobotMap.pneumaticsModule, 
				  RobotMap.doubleSolenoidForward, 
				  RobotMap.doubleSolenoidReverse);
        binGrabberValue = Value.kReverse;
        
        //setRampDirection(CantTalon.RampDirection.Both);
    }
    
    public static FrontElevator getInstance()
    {
		init();
        return singleton;
    }
	
	public static void init()
	{
		if(singleton == null)
		{
			singleton = new FrontElevator();
			SmartDashboard.putData("Front Elevator Subsystem", singleton);
		}
	}
	
	public void setTalonRampRate(double value)
	{
		talon1.setVoltageRampRate(value);
	}
	
    public void initDefaultCommand() 
    {
        setDefaultCommand(new FrontElevatorIdleCommand());
    }
    
    public void setElevatorSpeed(double speed)
    {
    	speed = -speed;
    	
    	if(getScaledPot() > MAX_ALLOWED_HEIGHT && speed < 0)
    		speed = 0;
    	else if(getScaledPot() < MIN_ALLOWED_HEIGHT && speed > 0)
    		speed = 0;
    	
    	SmartDashboard.putNumber("Front Elevator Speed", speed);
    	
    	talon1.set(speed);
    }
    
    public void binGrabberForward()
    {
    	binGrabber.set(Value.kForward);
    	binGrabberValue = Value.kForward;
    }
    
    public void binGrabberReverse()
    {
    	binGrabber.set(Value.kReverse);
    	binGrabberValue = Value.kReverse;
    }
    
    public void binGrabberOff(){
		binGrabber.set(Value.kOff);
		binGrabberValue = Value.kOff;
	}
    
    public Value getBinGrabber()
    {
    	return binGrabberValue;
    }
    
    public void putSmartDashboardInfo()
    {
    	SmartDashboard.putNumber("Front Potentiometer", getScaledPot());
    }

	@Override
	public void pidWrite(double output) 
	{
		SmartDashboard.putNumber("Front Elevator pid output", output);
		setElevatorSpeed(output);
	}

	@Override
	public double pidGet() 
	{
    	return getScaledPot();
	}
	
	private double getScaledPot()
	{
		double value = potentiometer.get();
    	value -= POT_MIN;
    	value = value/(POT_MAX-POT_MIN) * POT_RANGE;
    	return value;
	}
}
