package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.drive.Drive;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ResetEncodersCommand extends Command {

    public ResetEncodersCommand() {
        requires(Drive.getInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Drive.getInstance().resetEncoders();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
