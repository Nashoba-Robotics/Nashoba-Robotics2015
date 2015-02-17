package edu.nr.robotics.commandgroup;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;

public class CancelAllCommand extends CMD
{
	public CancelAllCommand()
	{
		this.requires(FrontElevator.getInstance());
		this.requires(Drive.getInstance());
		this.requires(BackElevator.getInstance());
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onExecute() {
		// TODO Auto-generated method stub
		
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
