package edu.nr.robotics;

import edu.nr.robotics.subsystems.CMD;
import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class EmptyCommand extends CMD
{
	public EmptyCommand(String name)
	{
		super(name);
	}
	public EmptyCommand(Subsystem requires)
	{
		this.requires(requires);
	}
	
	@Override
	protected void initialize() 
	{
	}

	@Override
	protected boolean isFinished() 
	{
		return true;
	}

	@Override
	protected void onEnd(boolean interrupted) {
	}

}
