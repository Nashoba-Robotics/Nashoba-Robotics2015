package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.OI;
import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;

/**
 * Command that moves the front elevator manually based off joystick input
 */
public class FrontElevatorJoystickCommand extends CMD 
{

    public FrontElevatorJoystickCommand() 
    {
    	this.requires(FrontElevator.getInstance());
    }

    // Called just before this Command runs the first time
    @Override
    protected void onStart() {
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

    // Called once after isFinished returns true
    @Override
    protected void onEnd(boolean interrupted) 
    {
    	FrontElevator.getInstance().setElevatorSpeed(0);
    }

}
