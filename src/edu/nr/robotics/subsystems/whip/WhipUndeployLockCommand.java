package edu.nr.robotics.subsystems.whip;

import edu.nr.robotics.subsystems.CMD;

public class WhipUndeployLockCommand extends CMD
{
	public WhipUndeployLockCommand()
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
		Whip.getInstance().undeployLock();
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
