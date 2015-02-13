package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class FrontElevatorGoToHeightCommand extends CMD 
{
	int count = 0;
	
	double height;
	
	PIDController pid;
	
    public FrontElevatorGoToHeightCommand(double height) 
    {
        requires(FrontElevator.getInstance());
        
        this.height = height;
        pid = new PIDController(0, 0.01, 0, FrontElevator.getInstance(), FrontElevator.getInstance());
    }
    
    boolean goingDown;
    
    @Override
	protected void onStart() 
    {
    	count = 0;
    	pid.enable();
		pid.setSetpoint(height);
		//FrontElevator.getInstance().setRampEnabled(true);
		
		goingDown = FrontElevator.getInstance().pidGet() > height;
		
		if(goingDown)
		{
			FrontElevator.getInstance().setTalonRampRate(6);
			pid.setPID(0.5, pid.getI(), pid.getD());
		}
		else
		{
			FrontElevator.getInstance().setTalonRampRate(12);
			pid.setPID(0.5, pid.getI(), pid.getD());
			
			/*FrontElevator.getInstance().setTalonRampRate(24);
			pid.setPID(2, pid.getI(), pid.getD());*/
		}
	}

    // Called repeatedly when this Command is scheduled to run
    @Override
	protected void onExecute()
    {
    	count++;
    	SmartDashboard.putNumber("Go to height count", count);
    	
    	if(Math.abs(pid.getError()) < 4d/12)
    		FrontElevator.getInstance().setTalonRampRate(256);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
        return Math.abs(pid.getError()) < 0.25/12d;
    }

    @Override
	protected void onEnd(boolean interrupted) 
    {
    	pid.disable();
	}
}
