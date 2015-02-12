package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;

/**
 *
 */
public class HDriveJoystickCommand extends CMD 
{	
    public HDriveJoystickCommand() 
    {
        requires(Drive.getInstance());
    }
    
    @Override
	protected void onStart() 
	{
		
	}

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void onExecute() 
    {
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
        return false;
    }

    @Override
	protected void onEnd(boolean interrupted) 
    {
		
	}
}
