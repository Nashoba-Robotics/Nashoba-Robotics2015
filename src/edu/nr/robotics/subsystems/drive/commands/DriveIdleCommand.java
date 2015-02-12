package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveIdleCommand extends CMD {

    public DriveIdleCommand() {
        requires(Drive.getInstance());
    }

    @Override
    protected void onStart()
    {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void onExecute() {
    	Drive.getInstance().arcadeDrive(0, 0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void onEnd(boolean interrupted)
    {
    	
    }
}
