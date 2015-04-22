package edu.nr.robotics.subsystems.whip;

import edu.nr.robotics.subsystems.CMD;

public class WhipDeployCommand extends CMD
{
	public WhipDeployCommand()
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
		Whip.getInstance().deployWhip();
	}

	@Override
	protected void onEnd(boolean interrupted)
	{
	}

	@Override
	protected boolean isFinished()
	{
		return true;
	}

}
