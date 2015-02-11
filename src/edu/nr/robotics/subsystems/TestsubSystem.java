package edu.nr.robotics.subsystems;

import edu.nr.robotics.CantTalon;
import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.command.Subsystem;

public class TestsubSystem extends Subsystem
{
	
	private static TestsubSystem singleton;
	public static TestsubSystem getInstance()
	{
		if(singleton == null)
			singleton = new TestsubSystem();
		return singleton;
	}
	
	private CantTalon talon;
	
	private TestsubSystem()
	{
		talon = new CantTalon(RobotMap.HDriveTalon);
		talon.setVoltageRampRate(0.01);
	}
	
	@Override
	protected void initDefaultCommand() 
	{
		
	}
	
	public void set(double x)
	{
		talon.set(x);
	}
}
