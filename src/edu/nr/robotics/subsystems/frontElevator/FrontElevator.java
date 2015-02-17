package edu.nr.robotics.subsystems.frontElevator;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.custom.CantTalon;
import edu.nr.robotics.custom.I2C;
import edu.nr.robotics.custom.LIDAR;
import edu.nr.robotics.custom.MotorPair;
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
	
	private static double BELT_SKIP_OFFSET = +0.02;
	
	//These need to be found
	//All heights in feet
	public static final double HEIGHT_ADJUST_TOTE_ONE = 0.33 + BELT_SKIP_OFFSET;
	public static final double HEIGHT_WAITING = 2.93 + BELT_SKIP_OFFSET;
	public static final double HEIGHT_PICK_UP_TOTE_ONE = 0.02 + BELT_SKIP_OFFSET;
	public static final double HEIGHT_OBTAIN_NOODLE = 0.20 + BELT_SKIP_OFFSET;
	public static final double HEIGHT_PICK_UP_TOTE_TWO = 0.85 + BELT_SKIP_OFFSET;
	public static final double HEIGHT_ADJUST_BIN = 0.44 + BELT_SKIP_OFFSET;
	public static final double HEIGHT_SCORING = 0.5 + BELT_SKIP_OFFSET;
	public static final double HEIGHT_BOTTOM = 0.01 + BELT_SKIP_OFFSET;
	public static final double HEIGHT_BEFORE_TOTE_ADJUST = 1.14 + BELT_SKIP_OFFSET;
	
	public static final double BARREL_ABOVE_FIRST_TOTE = 1.22;
	
	public static final ToteHeightPair[] commandedHeights = 
		{
		new ToteHeightPair("Waiting Height", HEIGHT_WAITING),
		new ToteHeightPair("Bottom Height", HEIGHT_BOTTOM),
		new ToteHeightPair("Pickup Tote Two", HEIGHT_PICK_UP_TOTE_TWO),
		new ToteHeightPair("Recycle above tote  Height", BARREL_ABOVE_FIRST_TOTE),
		new ToteHeightPair("Adjust Tote Height", HEIGHT_ADJUST_TOTE_ONE)
		};
	
	//A utility class for putting batch commands to smartdashboard for these heights
	public static class ToteHeightPair
	{
		public final String cmdName;
		public final double height;
		public ToteHeightPair(String cmdName, double height)
		{
			this.cmdName = cmdName;
			this.height = height;
		}
	}
	
	private final double POT_MAX = 0.80; //potentiometer voltage at max position
	private final double POT_MIN = 0.08;//potentiometer voltage at min value
	private final double POT_RANGE = (51)/12d; //Range between max and min in feet

	public static final boolean USING_LASER = false;
	
	AnalogPotentiometer potentiometer;
	LIDAR laser;
    CANTalon talon1;
    //MotorPair motors;
    
    private final double MAX_ALLOWED_HEIGHT = 4.2;
    private final double MIN_ALLOWED_HEIGHT = 0.03;
    
    private DoubleSolenoid binGrabber;
    private Value binGrabberValue;

    public FrontElevator() 
    {
    	talon1 = new CANTalon (RobotMap.frontElevatorTalon1);
    	CANTalon slave = new CANTalon(RobotMap.frontElevatorTalon2);
    	slave.changeControlMode(CANTalon.ControlMode.Follower);
    	slave.set(talon1.getDeviceID());
    	
    	
    	//talon1.setVoltageRampRate(1);
    	//talon2.setVoltageRampRate(1);
    	
    	//motors = new MotorPair(talon1, talon2);
    	//motors.enableDebug();
    	
		laser = new LIDAR(I2C.Port.kMXP);
		laser.start(); //Start polling
		
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
    
    public void startLaserPolling()
	{
		laser.start(100);
	}
	
	public void stopLaserPolling()
	{
		laser.stop();
	}
	
	public double getLaserDistanceFeet()
	{
		return laser.getDistanceFeet();
	}
	
	public double getLaserDistanceInches()
	{
		return laser.getDistanceInches();
	}
	
    public void putSmartDashboardInfo()
    {
    	SmartDashboard.putNumber("Laser Distance", (getLaserDistanceInches()));
    	SmartDashboard.putNumber("Potentiometer", getScaledPot());
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
		if(USING_LASER)
        {
        	return laser.getDistanceCentimeters();
        }
        else
        {
        	return getScaledPot();
        }
	}
	
	private double getScaledPot()
	{
		double value = potentiometer.get();
    	value -= POT_MIN;
    	value = value/(POT_MAX-POT_MIN) * POT_RANGE;
    	return value;
	}
}
