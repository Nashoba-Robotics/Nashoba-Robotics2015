package edu.nr.robotics;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class EmptyCommand extends Command
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
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}

}
