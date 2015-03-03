package edu.nr.robotics.auton;

import edu.nr.robotics.subsystems.CMD;

public class AutonDoNothingCommand extends CMD
{
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
		return true;
	}

}
