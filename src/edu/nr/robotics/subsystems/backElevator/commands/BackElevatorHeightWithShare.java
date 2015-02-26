package edu.nr.robotics.subsystems.backElevator.commands;

import edu.nr.robotics.custom.IsFinishedShare;

/**
 * Same as the gotoheightcommand, but this command will use the IsFinishedShare object to notify
 * another command with the IsFinishedShare instance when this command ends
 */
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
		{
			//Notify the other command that we are done
			share.isFinished = true;
		}
		
		return done;
	}
}
