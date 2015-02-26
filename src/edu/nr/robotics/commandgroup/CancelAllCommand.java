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
		
	}

	@Override
	protected void onExecute() {
		
	}

	@Override
	protected void onEnd(boolean interrupted) {
		
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

}
