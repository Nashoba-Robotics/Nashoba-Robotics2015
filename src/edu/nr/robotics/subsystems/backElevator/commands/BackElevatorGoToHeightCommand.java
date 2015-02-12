package edu.nr.robotics.subsystems.backElevator.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.backElevator.BackElevator;

/**
 *
 */
public class BackElevatorGoToHeightCommand extends CMD {
	double height;
    public BackElevatorGoToHeightCommand(double height) {
        requires(BackElevator.getInstance());
        this.height = height;
    }

    // Called just before this Command runs the first time
    protected void onStart() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void onExecute() {
    	BackElevator.getInstance().setSetpoint(height);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }
    
	@Override
	protected void onEnd(boolean interrupted) {
		
	}
}
