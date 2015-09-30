package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;

public class FrontArmsCloseCommand extends CMD
{
	public FrontArmsCloseCommand()
	{
		this.requires(FrontElevator.getInstance());
	}
	
	@Override
	protected void onStart()
	{
	}

	@Override
	protected void onExecute()
	{
		FrontElevator.getInstance().closeArms();
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
