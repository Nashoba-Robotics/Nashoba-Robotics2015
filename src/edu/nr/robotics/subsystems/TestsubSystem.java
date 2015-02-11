package edu.nr.robotics.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

public class TestsubSystem extends Subsystem
{
	private CantTalon talon;
	
	@Override
	protected void initDefaultCommand() 
	{
		talon = new CantTalon(12);
	}

}
