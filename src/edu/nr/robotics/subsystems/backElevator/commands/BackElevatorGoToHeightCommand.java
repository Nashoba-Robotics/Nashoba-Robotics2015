package edu.nr.robotics.subsystems.backElevator.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.wpi.first.wpilibj.PIDController;

/**
 *
 */
public class BackElevatorGoToHeightCommand extends CMD
{
	PIDController pid;
	
    public BackElevatorGoToHeightCommand(double height) 
    {
        requires(BackElevator.getInstance());
        
        pid = new PIDController(1, 0.1, 0, BackElevator.getInstance(), BackElevator.getInstance());
        pid.setSetpoint(height);
    }

    // Called just before this Command runs the first time
    protected void onStart() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void onExecute() 
    {
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
        return Math.abs(pid.getError()) < 0.25d/12;
    }
    
	@Override
	protected void onEnd(boolean interrupted) {
		
	}
}
