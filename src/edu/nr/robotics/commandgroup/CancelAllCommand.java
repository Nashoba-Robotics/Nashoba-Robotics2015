package edu.nr.robotics.commandgroup;

import edu.nr.lib.CMD;
import edu.nr.robotics.subsystems.binGrabber.BinGrabber;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.nr.robotics.subsystems.whip.Whip;

public class CancelAllCommand extends CMD
{
	public CancelAllCommand()
	{
		this.requires(FrontElevator.getInstance());
		this.requires(Drive.getInstance());
		this.requires(Whip.getInstance());
		this.requires(BinGrabber.getInstance());
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
