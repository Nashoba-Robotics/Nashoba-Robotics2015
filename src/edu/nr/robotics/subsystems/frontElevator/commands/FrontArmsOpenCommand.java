package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;

public class FrontArmsOpenCommand extends CMD
{
	public FrontArmsOpenCommand()
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
		FrontElevator.getInstance().openArms();
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
