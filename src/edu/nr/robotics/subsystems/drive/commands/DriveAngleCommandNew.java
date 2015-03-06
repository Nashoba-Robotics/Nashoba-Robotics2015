package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveAngleCommandNew extends CMD
{
	private double targetRelative;
	private double targetGyro;
	
	public DriveAngleCommandNew(double targetRelativeAngleRadians)
	{
		this.requires(Drive.getInstance());
		targetRelative = targetRelativeAngleRadians;
	}
	
	@Override
	protected void onStart()
	{
		targetGyro = Drive.getInstance().getAngleRadians() + targetRelative;
		Drive.getInstance().setPIDEnabled(false);
	}

	
	private double count;
	
	@Override
	protected void onExecute()
	{
		double error = targetGyro - Drive.getInstance().getAngleRadians();
		
		double maxSpeed = 0.3;
		double drivePSpeed = (Math.abs(error) / 1) * maxSpeed;
		double driveSpeed = Math.min(maxSpeed, drivePSpeed);
		
		if(Math.abs(error) > 0.3)
			count = 0;
		count++;
		
		driveSpeed += count * 0.001;
		
		driveSpeed *= Math.signum(error);
		
		Drive.getInstance().arcadeDrive(0, -driveSpeed);
		SmartDashboard.putNumber("DriveAngleNew Speed", -driveSpeed);
	}
	
	@Override
	protected boolean isFinished()
	{
		double error = Math.abs(Drive.getInstance().getAngleRadians() - targetGyro);
		SmartDashboard.putNumber("DriveAngleNew Error", error);
		return error < 0.04;
	}

	@Override
	protected void onEnd(boolean interrupted)
	{
		Drive.getInstance().setPIDEnabled(true);
	}
}
