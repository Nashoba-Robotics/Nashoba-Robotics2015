package edu.nr.robotics.subsystems.backElevator.commands;

import edu.nr.robotics.custom.IsFinishedShare;

public class BackElevatorHeightWithShare extends BackElevatorGoToHeightCommand
{

	private IsFinishedShare share;
	public BackElevatorHeightWithShare(double height, IsFinishedShare share) 
	{
		super(height);
		this.share = share;
	}
	
	@Override
	protected boolean isFinished()
	{
		boolean done = super.isFinished();
		if(done)
			share.isFinished = true;
		return done;
	}
}
