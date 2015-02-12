package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.GyroPIDSource;
import edu.nr.robotics.subsystems.drive.RotationPIDOutput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveAngleCommand extends CMD 
{
	double targetDeltaAngleRadians;
	PIDController pidController;
	GyroPIDSource gyroSource;
	
    public DriveAngleCommand(double targetDeltaAngleRadians) 
    {
    	requires(Drive.getInstance());
    	gyroSource = new GyroPIDSource();
    	pidController = new PIDController(0.5, 0.073, 0, gyroSource, new RotationPIDOutput());
    	SmartDashboard.putData("Angle PID", pidController);
    	this.targetDeltaAngleRadians = targetDeltaAngleRadians;
    }

    @Override
	protected void onStart() 
	{
		pidController.enable();
		pidController.setSetpoint(gyroSource.pidGet() + targetDeltaAngleRadians);
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
