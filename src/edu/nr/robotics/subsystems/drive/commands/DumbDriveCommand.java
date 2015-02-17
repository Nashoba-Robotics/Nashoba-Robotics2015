package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;

public class DumbDriveCommand extends CMD
{
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onExecute() 
	{
		Drive.getInstance().setPIDEnabled(false);
	}

	@Override
	protected void onEnd(boolean interrupted) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

}
