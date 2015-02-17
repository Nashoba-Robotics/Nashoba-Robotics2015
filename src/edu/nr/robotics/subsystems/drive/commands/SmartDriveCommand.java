package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;

public class SmartDriveCommand extends CMD
{
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onExecute() 
	{
		Drive.getInstance().setPIDEnabled(true);
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
