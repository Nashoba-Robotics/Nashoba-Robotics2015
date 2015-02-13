package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;

/**
 *
 */
public class FrontElevatorIdleCommand extends CMD 
{
	
    public FrontElevatorIdleCommand() 
    {
        requires(FrontElevator.getInstance());
    }

	@Override
	protected void onStart() 
	{
	}

    // Called repeatedly when this Command is scheduled to run
	@Override
    protected void onExecute() 
    {
    	FrontElevator.getInstance().setElevatorSpeed(0);
		FrontElevator.getInstance().binGrabberOff();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
        return false;
    }

	@Override
	protected void onEnd(boolean interrupted) {		
	}
}
