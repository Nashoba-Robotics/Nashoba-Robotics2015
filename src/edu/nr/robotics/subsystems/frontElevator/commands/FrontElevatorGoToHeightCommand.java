package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.wpi.first.wpilibj.PIDController;

/**
 *
 */
public class FrontElevatorGoToHeightCommand extends CMD 
{
	double height;
	
	PIDController pid;
	
    public FrontElevatorGoToHeightCommand(double height) 
    {
        requires(FrontElevator.getInstance());
        
        this.height = height;
        pid = new PIDController(2, 0, 0, FrontElevator.getInstance(), FrontElevator.getInstance());
    }
    
    @Override
	protected void onStart() 
    {
    	pid.enable();
		pid.setSetpoint(height);
		
	}

    // Called repeatedly when this Command is scheduled to run
    @Override
	protected void onExecute()
    {
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
        return Math.abs(pid.getError()) < 0.5/12d;
    }

    @Override
	protected void onEnd(boolean interrupted) 
    {
    	pid.disable();
	}
}
