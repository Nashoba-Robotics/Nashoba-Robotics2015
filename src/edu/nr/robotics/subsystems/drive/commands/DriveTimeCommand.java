package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;

public class DriveTimeCommand extends CMD
{
	private double speed;
	public DriveTimeCommand(double speed, double timeoutSeconds)
	{
		this.setTimeout(timeoutSeconds);
		this.speed = speed;
	}
	
	@Override
	protected void onStart() 
	{
		
	}

	@Override
	protected void onExecute() 
	{
		Drive.getInstance().arcadeDrive(speed, 0);
	}

	@Override
	protected void onEnd(boolean interrupted) 
	{
		Drive.getInstance().arcadeDrive(0, 0);
	}

	@Override
	protected boolean isFinished() 
	{
		return this.isTimedOut();
	}

}
