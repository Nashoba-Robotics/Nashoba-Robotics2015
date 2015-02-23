package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.OI;
import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;

/**
 *
 */
public class DriveJoystickTankCommand extends CMD {

    public DriveJoystickTankCommand() {
        requires(Drive.getInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }
    
    // Called repeatedly when this Command is scheduled to run
    protected void onExecute() 
    {
    	double left = OI.getInstance().getTankLeftValue();
    	double right = OI.getInstance().getTankRightValue();
    	
    	// square the inputs (while preserving the sign) to increase fine control while permitting full power
        right = right*right * Math.signum(right);
        left = left*left * Math.signum(left);
        
    	Drive.getInstance().tankDrive(left, right);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void onEnd() 
    {
    	
    }

	@Override
	protected void onStart()
	{
		
	}

	@Override
	protected void onEnd(boolean interrupted) 
	{
		
	}
}
