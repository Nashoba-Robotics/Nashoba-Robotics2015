package edu.nr.robotics.subsystems.binGrabber;

import edu.nr.robotics.subsystems.CMD;

/**
 *
 */
public class OpenBinGrabberCommand extends CMD {

    public OpenBinGrabberCommand() {
        requires(BinGrabber.getInstance());
    }

    // Called just before this Command runs the first time
    protected void onStart() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void onExecute() {
    	BinGrabber.getInstance().binGrabberReverse();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void onEnd(boolean interrupted) {
    }

}
