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
	
	//These need to be found
	//All heights in feet
	public static final double HEIGHT_ADJUST_TOTE_ONE = 0;
	public static final double HEIGHT_WAITING = 0;
	public static final double HEIGHT_PICK_UP_TOTE_ONE = 0;
	public static final double HEIGHT_PICK_UP_TOTE_TWO = 0;
	public static final double HEIGHT_SCORING = 0;
	
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
    	
    	//motors.set(speed);
    	talon1.set(speed);
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
	
	/*public void setRampEnabled(boolean enabled)
	{
		if(enabled)
			motors.enableCantTalonRamping();
		else
			motors.disableCantTalonRamping();
	}
	
	public void setRampRate(double percentPerSecond)
	{
		motors.setCantTalonRampRate(percentPerSecond);
	}
	
	public void setRampDirection(CantTalon.RampDirection direction)
	{
		motors.setCantTalonRampDirection(direction);
	}*/
	
	private double getScaledPot()
	{
		double value = potentiometer.get();
    	value -= POT_MIN;
    	value = value/(POT_MAX-POT_MIN) * POT_RANGE;
    	return value;
	}
}
