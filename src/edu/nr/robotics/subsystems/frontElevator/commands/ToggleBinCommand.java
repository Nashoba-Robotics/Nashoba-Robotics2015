package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 *
 */
public class ToggleBinCommand extends CMD {

    public ToggleBinCommand() {
        requires(FrontElevator.getInstance());
    }

    // Called just before this Command runs the first time
    protected void onStart() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void onExecute() {
    	Value value = FrontElevator.getInstance().getBinGrabber();
    	if(value == Value.kForward)
    	{
    		FrontElevator.getInstance().binGrabberReverse();
    	}
    	if(value == Value.kReverse)
    	{
    		FrontElevator.getInstance().binGrabberForward();
    	}
    	if(value == Value.kOff)
    	{
    		FrontElevator.getInstance().binGrabberForward();
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
