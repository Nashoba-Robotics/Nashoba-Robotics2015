package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;

public class ActivateSmartDriveCommand extends CMD
{
	@Override
	protected void onStart() {
		
	}

	@Override
	protected void onExecute() 
	{
		Drive.getInstance().setPIDEnabled(true);
	}

	@Override
	protected void onEnd(boolean interrupted) {
		
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

}
