package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.drive.mxp.NavX;

public class DriveDistanceAfterDrivingOverStep extends DriveDistanceCommand
{
	boolean hasTiltedForward = false, isDoneDrivingOverStep = false;
	public DriveDistanceAfterDrivingOverStep() 
	{
		super(23, 1, 0.5);
	}
	
	public void onStart()
	{
		super.onStart();
	}
	
	public void onExecute()
	{
		if(!isDoneDrivingOverStep)
		{
			if(NavX.getInstance().getPitch() > 4)
				hasTiltedForward = true;
			if(hasTiltedForward)
			{
				if(Math.abs(NavX.getInstance().getPitch()) < 2)
				{
					this.resetEncoderSource();
					this.setSetpoint(2);
					isDoneDrivingOverStep = true;
				}
			}
		}
		
		
		super.onExecute();
	}
}
