package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.custom.PID;
import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.GyroPIDSource;
import edu.nr.robotics.subsystems.drive.RotationPIDOutput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveAngleCommand extends CMD 
{
	private boolean absolute;
	PID pidController;
	GyroPIDSource gyroSource;
	private double targetAngleRadians;
	
	/**
	 * 
	 * @param targetAngleRadians Angle in radians
	 */
    public DriveAngleCommand(double targetAngleRadians, boolean absolute) 
    {
    	requires(Drive.getInstance());
    	gyroSource = new GyroPIDSource();
    	
    	pidController = new PID(0.5, 0.01, 0, gyroSource, new RotationPIDOutput());
    	
    	this.absolute = absolute;
    	this.targetAngleRadians = targetAngleRadians;
    }
    
    public void setP(double p)
    {
    	pidController.setPID(p, pidController.getI(), pidController.getD());
    }

    @Override
	protected void onStart() 
	{
		pidController.enable();
		if(absolute)
    	{
    		pidController.setSetpoint(targetAngleRadians);
    	}
    	else
		{
    		pidController.setSetpoint(gyroSource.pidGet() + targetAngleRadians);
		}
		Drive.getInstance().setDriveP(1);
		correctCount = 0;
	}

    @Override
    protected void onExecute() 
    {
    	SmartDashboard.putNumber("Drive Angle Command err", pidController.getError());
    	if(Math.abs(pidController.getError())  > .19)
    		pidController.resetTotalError();
    	SmartDashboard.putNumber("Angle Total Error", pidController.getTotalError());
    }

    private double correctCount = 0;
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	if(Math.abs(pidController.getError()) < 0.034)
    		correctCount++;
    	else
    		correctCount = 0;
        return correctCount > 3;
    }

    // Called once after isFinished returns true
    @Override
    protected void onEnd(boolean interrupted) 
    {
    	Drive.getInstance().setDriveP(Drive.JOYSTICK_DRIVE_P);
    	pidController.disable();
    }

}
