package edu.nr.robotics.subsystems.drive.gyro;

import edu.nr.robotics.subsystems.drive.Drive;

public class AngleGyroCorrection extends GyroCorrection
{
	private double initialAngle;
	
	public double getAngleErrorDegrees()
	{
		//Error is just based off initial angle
    	return (Drive.getInstance().getAngleDegrees() - initialAngle);
	}
	
	public void reset()
	{
		initialAngle = Drive.getInstance().getAngleDegrees();
	}
}
