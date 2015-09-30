package edu.nr.robotics.subsystems.whip;

import edu.nr.robotics.subsystems.CMD;

public class WhipDeployLockCommand extends CMD
{
	public WhipDeployLockCommand()
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
		Whip.getInstance().deployLock();
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
