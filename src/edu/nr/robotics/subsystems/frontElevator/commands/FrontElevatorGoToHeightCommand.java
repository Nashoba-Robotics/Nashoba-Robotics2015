package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.lib.CMD;
import edu.nr.lib.PID.PID;
import edu.nr.lib.navx.NavX;
import edu.nr.robotics.OI;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
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
        pid = new PID(0, 0, 0, FrontElevator.getInstance(), FrontElevator.getInstance());
    }
    
    boolean goingDown;
    private double maxDownSpeedMagnitude = 0.9;
    private boolean talonRampGoingDownFast = false;
    
    @Override
	protected void onStart() 
    {
    	pid.enable();
		pid.setSetpoint(height);
		//FrontElevator.getInstance().setRampEnabled(true);
		
		goingDown = FrontElevator.getInstance().pidGet() > height;
		
		if(goingDown)
		{
			if(talonRampGoingDownFast)
				FrontElevator.getInstance().setTalonRampRate(15);
			else
				FrontElevator.getInstance().setTalonRampRate(10);
			
			pid.setPID(1, 0.03, pid.getD());
			pid.setOutputRange(-maxDownSpeedMagnitude, maxDownSpeedMagnitude);
		}
		else
		{
			FrontElevator.getInstance().setTalonRampRate(-48);
			pid.setPID(2, 0.05, pid.getD());
			pid.setOutputRange(-1, 1);
		}
	}
    
    public void setGoingDownMaxRange(double maxDownSpeedMagnitude)
    {
    	this.maxDownSpeedMagnitude = Math.abs(maxDownSpeedMagnitude);
    }
    
    public void setTalonRamp(boolean fast)
    {
    	talonRampGoingDownFast = fast;
    }
    
    public void setPI(double p, double i)
    {
    	pid.setPID(p, i, pid.getD());
    }

    
    
    // Called repeatedly when this Command is scheduled to run
    @Override
	protected void onExecute()
    {
    	SmartDashboard.putNumber("Elevator Err", pid.getError());
    	
    	if(NavX.getInstance().getPitch() < -3 && OI.getInstance().getArcadeMoveValue() == 0)
    	{
    		if(this.getGroup() != null)
    			this.getGroup().cancel();
    		else
    			cancel();
    	}
    }
    
    private double epsilon = 0.2/12d;
    
    public void setEpsilon(double value)
    {
    	epsilon = Math.abs(value);
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
    	
        return Math.abs(pid.getError()) < epsilon;
    }

    @Override
	protected void onEnd(boolean interrupted) 
    {
    	pid.reset();
    	FrontElevator.getInstance().setElevatorSpeed(0);
	}
}
