package edu.nr.robotics.subsystems.frontfingers;

import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class FrontFingers extends Subsystem
{
	private static FrontFingers singleton;
	public static FrontFingers getInstance()
	{
		init();
		return singleton;
	}
	
	public static void init()
	{
		if(singleton == null)
			singleton = new FrontFingers();
	}
	
	private DoubleSolenoid fingers;
	private FrontFingers()
	{
		fingers = new DoubleSolenoid(RobotMap.pneumaticsModule, RobotMap.FRONT_FINGERS_FORWARD, RobotMap.FRONT_FINGERS_REVERSE);
	}
	
	@Override
	protected void initDefaultCommand() 
	{
		this.setDefaultCommand(new FrontFingersIdle());
	}
	
	public void open()
	{
		fingers.set(Value.kReverse);
	}
	
	public void close()
	{
		fingers.set(Value.kForward);
	}
}
