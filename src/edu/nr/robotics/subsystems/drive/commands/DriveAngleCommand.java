package edu.nr.robotics.subsystems.drive.commands;

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
	double targetSetpoint;
	PIDController pidController;
	GyroPIDSource gyroSource;
	
	/**
	 * 
	 * @param targetAngleRadians Angle in radians
	 */
    public DriveAngleCommand(double targetAngleRadians, boolean absolute) 
    {
    	requires(Drive.getInstance());
    	gyroSource = new GyroPIDSource();
    	pidController = new PIDController(0.25, 0.03, 0, gyroSource, new RotationPIDOutput());
    	SmartDashboard.putData("Angle PID", pidController);
    	
    	if(absolute)
    	{
    		this.targetSetpoint = targetAngleRadians;
    	}
    	else
		{
    		this.targetSetpoint = gyroSource.pidGet() + targetAngleRadians;
		}
    }

    @Override
	protected void onStart() 
	{
		pidController.enable();
		pidController.setSetpoint(targetSetpoint);
	}

    @Override
    protected void onExecute() 
    {
    	SmartDashboard.putNumber("Drive Angle Command err", pidController.getError());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
        return Math.abs(pidController.getError()) < 0.01;
    }

    // Called once after isFinished returns true
    @Override
    protected void onEnd(boolean interrupted) 
    {
    	pidController.disable();
    }

}
