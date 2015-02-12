package edu.nr.robotics.subsystems.frontElevator;

import edu.nr.robotics.CantTalon;
import edu.nr.robotics.MotorPair;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.I2C;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontElevatorJoystickCommand;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
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
	
	//These need to be found
	//All heights in feet
	public static final double HEIGHT_ADJUST_TOTE_ONE = 0;
	public static final double HEIGHT_WAITING = 0;
	public static final double HEIGHT_PICK_UP_TOTE_ONE = 0;
	public static final double HEIGHT_PICK_UP_TOTE_TWO = 0;
	public static final double HEIGHT_SCORING = 0;
	
	public static final double POT_MAX = 0.80;
	public static final double POT_MIN = 0.08;
	private final double POT_RANGE = (51)/12d;

	
	public static final boolean USING_LASER = false;
	
	AnalogPotentiometer potentiometer;
	LIDAR laser;
    CantTalon talon1;
    CantTalon talon2;
    MotorPair motors;
    
    private DoubleSolenoid binGrabber;

    public FrontElevator() 
    {
    	talon1 = new CantTalon(RobotMap.frontElevatorTalon1);
    	talon2 = new CantTalon(RobotMap.frontElevatorTalon2);
    	talon1.setVoltageRampRate(4);
    	talon2.setVoltageRampRate(4);
    	
    	motors = new MotorPair(talon1, talon2);
		laser = new LIDAR(I2C.Port.kMXP);
		laser.start(); //Start polling
		
		potentiometer = new AnalogPotentiometer(RobotMap.POTENTIOMETER_FRONT_ELEVATOR);
		        
        binGrabber = new DoubleSolenoid(RobotMap.pneumaticsModule, 
				  RobotMap.doubleSolenoidForward, 
				  RobotMap.doubleSolenoidReverse);
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
	
    public void initDefaultCommand() 
    {
        setDefaultCommand(new FrontElevatorJoystickCommand());
    }
    
    public void setElevatorSpeed(double speed)
    {
    	motors.set(-speed);
    }
    
    public void binGrabberForward()
    {
    	binGrabber.set(Value.kForward);
    }
    
    public void binGrabberReverse()
    {
    	binGrabber.set(Value.kReverse);
    }
    
    public void binGrabberOff(){
		binGrabber.set(Value.kOff);
	}
    
    public Value getBinGrabber()
    {
    	return binGrabber.get();
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
