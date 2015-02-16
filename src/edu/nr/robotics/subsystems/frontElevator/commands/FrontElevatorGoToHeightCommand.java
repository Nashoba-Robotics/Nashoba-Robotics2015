package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.custom.PID;
import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.mxp.NavX;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class FrontElevatorGoToHeightCommand extends CMD 
{
	double height;
	
	PID pid;
	
    public FrontElevatorGoToHeightCommand(double height) 
    {
        requires(FrontElevator.getInstance());
        
        this.height = height;
        pid = new PID(0, 0.01, 0, FrontElevator.getInstance(), FrontElevator.getInstance());
    }
    
    boolean goingDown;
    
    @Override
	protected void onStart() 
    {
    	pid.enable();
		pid.setSetpoint(height);
		//FrontElevator.getInstance().setRampEnabled(true);
		
		goingDown = FrontElevator.getInstance().pidGet() > height;
		
		if(goingDown)
		{
			FrontElevator.getInstance().setTalonRampRate(5);
			pid.setPID(1, 0.03, pid.getD());
			pid.setOutputRange(-0.8, 0.8);
		}
		else
		{
			FrontElevator.getInstance().setTalonRampRate(-24);
			pid.setPID(2, 0.05, pid.getD());
		}
	}

    // Called repeatedly when this Command is scheduled to run
    @Override
	protected void onExecute()
    {
    	SmartDashboard.putNumber("Elevator Err", pid.getError());
    	
    	if(NavX.getInstance().getPitch() < -3)
    	{
    		if(this.getGroup() != null)
    			this.getGroup().cancel();
    		else
    			cancel();
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	if(Math.abs(pid.getError()) < 6d/12)
    	{
			FrontElevator.getInstance().setTalonRampRate(0);
    	}
    	else
    	{
    		pid.resetTotalError();
    	}
    	
        return Math.abs(pid.getError()) < 0.3/12d;
    }

    @Override
	protected void onEnd(boolean interrupted) 
    {
    	pid.disable();
    	FrontElevator.getInstance().setElevatorSpeed(0);
	}
}
