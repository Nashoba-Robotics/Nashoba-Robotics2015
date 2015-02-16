package edu.nr.robotics.subsystems.backElevator.commands;

import edu.nr.robotics.custom.PID;
import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BackElevatorGoToHeightCommand extends CMD
{
	PID pid;
	
    public BackElevatorGoToHeightCommand(double height) 
    {
        requires(BackElevator.getInstance());
        
        pid = new PID(1.5, 0.05, 0, BackElevator.getInstance(), BackElevator.getInstance());
        pid.setSetpoint(height);
    }

    // Called just before this Command runs the first time
    protected void onStart()
    {
    	pid.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void onExecute() 
    {
    	SmartDashboard.putNumber("Back err", pid.getError());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
        return Math.abs(pid.getError()) < 0.25d/12;
    }
    
	@Override
	protected void onEnd(boolean interrupted) 
	{
		pid.disable();
	}
}
