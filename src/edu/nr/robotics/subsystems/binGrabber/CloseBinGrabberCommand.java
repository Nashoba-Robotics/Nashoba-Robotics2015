package edu.nr.robotics.subsystems.binGrabber;

import edu.nr.robotics.subsystems.CMD;

/**
 *
 */
public class CloseBinGrabberCommand extends CMD {

    public CloseBinGrabberCommand() {
        requires(BinGrabber.getInstance());
    }

    // Called just before this Command runs the first time
    @Override
    protected void onStart() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void onExecute() {
    	BinGrabber.getInstance().binGrabberForward();
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
