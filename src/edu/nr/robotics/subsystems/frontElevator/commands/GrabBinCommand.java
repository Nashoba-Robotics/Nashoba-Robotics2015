package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;

/**
 *
 */
public class GrabBinCommand extends CMD {

    public GrabBinCommand() {
        requires(FrontElevator.getInstance());
    }

    // Called just before this Command runs the first time
    @Override
    protected void onStart() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void onExecute() {
    	FrontElevator.getInstance().binGrabberForward();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    @Override
    protected void onEnd(boolean interrupted) {
    }

}
