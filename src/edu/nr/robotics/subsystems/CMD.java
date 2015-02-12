package edu.nr.robotics.subsystems;

import edu.wpi.first.wpilibj.command.Command;

public abstract class CMD extends Command
{
	public CMD()
	{
		super();
	}
	
	public CMD(String name)
	{
		super(name);
	}
	
	private boolean reset = true;
	
	/**
	 * Called every time the command starts after being stopped
	 */
	protected abstract void onStart();
	protected abstract void onExecute();
	
	/**
	 * Called when the command ends
	 * @param interrupted True if the command was interrupted
	 */
	protected abstract void onEnd(boolean interrupted);
	
	protected void initialize()
	{
		
	}
	
	protected final void execute()
	{
		if(reset)
		{
			onStart();
			reset = false;
		}
		
		onExecute();
	}
	
	protected final void end()
	{
		reset = true;
		onEnd(false);
	}
	
	protected final void interrupted()
	{
		reset = true;
		onEnd(true);
	}
}
