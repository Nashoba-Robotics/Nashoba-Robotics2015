package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;

/**
 *
 */
public class ReleaseBinCommand extends CMD {

    public ReleaseBinCommand() {
        requires(FrontElevator.getInstance());
    }

    // Called just before this Command runs the first time
    protected void onStart() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void onExecute() {
    	FrontElevator.getInstance().binGrabberReverse();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void onEnd(boolean interrupted) {
    }

}
