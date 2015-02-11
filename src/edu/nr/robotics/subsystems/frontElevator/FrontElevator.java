package edu.nr.robotics.subsystems.frontElevator;

import edu.nr.robotics.CantTalon;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.I2C;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontElevatorIdleCommand;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class FrontElevator extends PIDSubsystem {

	public static FrontElevator singleton;
	
	//These need to be found
	//All heights in feet
	public static final double HEIGHT_ADJUST_TOTE_ONE = 0;
	public static final double HEIGHT_WAITING = 0;
	public static final double HEIGHT_PICK_UP_TOTE_ONE = 0;
	public static final double HEIGHT_PICK_UP_TOTE_TWO = 0;
	public static final double HEIGHT_SCORING = 0;
	public static final double HEIGHT_MAX = 5;
	public static final double HEIGHT_MIN = 0;

	
	public static final boolean USING_LASER = false;

	AnalogPotentiometer potentiometer;
	LIDAR laser;
    CantTalon talon;
    
    private DoubleSolenoid binGrabber;

    public FrontElevator() 
    {
    	//Only need the P, I, D terms in this case (because our elevator has no back-drive, so motors can be cut when at target)
    	super("Front Elevator", 0, 0, 0);
    	
    	talon = new CantTalon(RobotMap.frontElevatorTalon);
    	
		laser = new LIDAR(I2C.Port.kMXP);
		laser.start(); //Start polling
		
		potentiometer = new AnalogPotentiometer(RobotMap.POTENTIOMETER_FRONT_ELEVATOR, HEIGHT_MAX, -HEIGHT_MAX/2);
		        
        binGrabber = new DoubleSolenoid(RobotMap.pneumaticsModule, 
				  RobotMap.doubleSolenoidForward, 
				  RobotMap.doubleSolenoidReverse);
        
        enable();
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
        setDefaultCommand(new FrontElevatorIdleCommand());
    }
    
    protected double returnPIDInput() 
    {
        if(USING_LASER)
        {
        	return laser.getDistanceCentimeters();
        }
        else
        {
        	return potentiometer.get();
        }
    }
    
    protected void usePIDOutput(double output) 
    {
        talon.set(output);
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
    }
}
