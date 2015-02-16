package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.custom.IsFinishedShare;
import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;

public class EndAutoStall extends CMD
{
	private IsFinishedShare isFinished;
	
	public EndAutoStall(IsFinishedShare isFinishedShare)
	{
		requires(Drive.getInstance());
		this.isFinished = isFinishedShare;
	}
	
	@Override
	protected void onStart() 
	{
	}

	@Override
	protected void onExecute() 
	{
		Drive.getInstance().arcadeDrive(-0.2, 0);
	}

	@Override
	protected boolean isFinished() 
	{
		return isFinished.isFinished;
	}
	
	@Override
	protected void onEnd(boolean interrupted) 
	{
		Drive.getInstance().arcadeDrive(0, 0);
	}

	

}
