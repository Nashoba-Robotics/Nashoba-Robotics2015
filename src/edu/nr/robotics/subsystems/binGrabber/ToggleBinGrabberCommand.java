package edu.nr.robotics.subsystems.binGrabber;

import edu.nr.lib.CMD;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 *
 */
public class ToggleBinGrabberCommand extends CMD {

    public ToggleBinGrabberCommand() {
        requires(BinGrabber.getInstance());
    }

    // Called just before this Command runs the first time
    protected void onStart() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void onExecute() {
    	Value value = BinGrabber.getInstance().getBinGrabber();
    	if(value == Value.kForward)
    	{
    		BinGrabber.getInstance().binGrabberReverse();
    	}
    	else if(value == Value.kReverse)
    	{
    		BinGrabber.getInstance().binGrabberForward();
    	}
    	else if(value == Value.kOff)
    	{
    		BinGrabber.getInstance().binGrabberForward();
    	}
    	
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
