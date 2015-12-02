package edu.nr.robotics.subsystems.whip;

import edu.nr.lib.CMD;

public class WhipIdleCommand extends CMD
{
	public WhipIdleCommand()
	{
		this.requires(Whip.getInstance());
	}
	
	@Override
	protected void onStart()
	{
	}

	@Override
	protected void onExecute()
	{
	}

	@Override
	protected void onEnd(boolean interrupted)
	{
	}

	@Override
	protected boolean isFinished()
	{
		
		return false;
	}

}
