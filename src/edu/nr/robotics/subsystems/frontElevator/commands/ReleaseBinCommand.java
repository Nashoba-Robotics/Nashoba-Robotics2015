package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ReleaseBinCommand extends Command {

    public ReleaseBinCommand() {
        requires(FrontElevator.getInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	FrontElevator.getInstance().binGrabberReverse();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
