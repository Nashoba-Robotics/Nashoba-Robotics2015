package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;

/**
 *
 */
public class FrontElevatorIdleCommand extends CMD {
	
    private static final double JOYSTICK_ADJUSTMENT_VALUE = 0.2;
    
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
    	/*if(Math.abs(OI.getInstance().getFrontElevatorJoy()) > 0.05)
    	{
    		FrontElevator.getInstance().setSetpoint(FrontElevator.getInstance().getSetpoint() + OI.getInstance().getFrontElevatorJoy()*JOYSTICK_ADJUSTMENT_VALUE );
    	}*/
    	
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
