package edu.nr.robotics.subsystems.binGrabber;

import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class BinGrabber extends Subsystem
{
	private static BinGrabber singleton;
	public static BinGrabber getInstance()
	{
		init();
		return singleton;
	}
	
	public static void init()
	{
		if(singleton == null)
			singleton = new BinGrabber();
	}
	
	private DoubleSolenoid binGrabber;
	
	private BinGrabber()
	{
		binGrabber = new DoubleSolenoid(RobotMap.pneumaticsModule, 
				  RobotMap.doubleSolenoidForward, 
				  RobotMap.doubleSolenoidReverse);
	}
	@Override
	protected void initDefaultCommand()
	{
		
	}
	
	public void binGrabberForward()
    {
    	binGrabber.set(Value.kForward);
    }
    
    public void binGrabberReverse()
    {
    	binGrabber.set(Value.kReverse);
    }
    
    public void binGrabberOff()
    {
		binGrabber.set(Value.kOff);
	}
    
    public Value getBinGrabber()
    {
    	return binGrabber.get();//binGrabberValue;
    }
}
