package edu.nr.robotics.subsystems.backElevator.commands;

import edu.nr.robotics.custom.PID;
import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BackElevatorGoToHeightCommand extends CMD implements PIDOutput
{
	protected PID pid;
	private double maxSpeed;
	private boolean useMaxSpeed = false;
	
    public BackElevatorGoToHeightCommand(double height) 
    {
        requires(BackElevator.getInstance());
        
        pid = new PID(1, 0.05, 0, BackElevator.getInstance(), this);
        pid.setSetpoint(height);
    }
    
    public void setMaxSpeed(double max)
    {
    	this.maxSpeed = max;
    	useMaxSpeed = true;
    }

    // Called just before this Command runs the first time
    protected void onStart()
    {
    	pid.enable();
    }
    
    @Override
	public void pidWrite(double output)
    {
    	if(Math.abs(pid.getError()) > 6d/12)
    		pid.resetTotalError();
    	if(useMaxSpeed)
    		output = Math.min(Math.abs(output), maxSpeed) * Math.signum(output);
		BackElevator.getInstance().pidWrite(output);
	}

    // Called repeatedly when this Command is scheduled to run
    protected void onExecute() 
    {
    	SmartDashboard.putNumber("Back err", pid.getError());
    }

    private double epsilon = 0.25d/12;
    
    public void setEpsilon(double valueFeet)
    {
    	this.epsilon = valueFeet;
    }
    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
        return Math.abs(pid.getError()) < epsilon;
    }
    
	@Override
	protected void onEnd(boolean interrupted) 
	{
		pid.reset();
	}
}
