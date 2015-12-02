package edu.nr.robotics.subsystems.whip;

import edu.nr.lib.CMD;

public class WhipUndeployCommand extends CMD
{
	public WhipUndeployCommand()
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
		Whip.getInstance().undeployWhip();
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
