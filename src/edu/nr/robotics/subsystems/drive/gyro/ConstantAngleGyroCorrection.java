package edu.nr.robotics.subsystems.drive.gyro;

import edu.nr.robotics.subsystems.drive.Drive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ConstantAngleGyroCorrection extends GyroCorrection
{
	private static double alignmentAngle = 0;
	
	public static void setAlignmentAngle(double angleDegrees)
	{
		alignmentAngle = limitRange(angleDegrees + 180);
	}
	
	private static double limitRange(double angleDegrees)
	{
		angleDegrees %= 360;
		if(angleDegrees < 0)
			angleDegrees += 360;
		return angleDegrees;
	}
	
	private double offset;
	
	public double getAngleErrorDegrees()
	{
		SmartDashboard.putNumber("Aligment Angle", alignmentAngle);
		SmartDashboard.putNumber("Current Angle", Drive.getInstance().getAngleDegrees());
		SmartDashboard.putNumber("Offset", offset);
		
		//Error is just based off initial angle
    	return (Drive.getInstance().getAngleDegrees() + offset - alignmentAngle);
	}
	
	public void reset()
	{
		double actualRobotAngle = Drive.getInstance().getAngleDegrees();
		double currentRobotAngle = limitRange(actualRobotAngle);
		if(currentRobotAngle+180 < alignmentAngle)
		{
			currentRobotAngle += 360;
		}
		else if(currentRobotAngle-180 > alignmentAngle)
		{
			currentRobotAngle -= 360;
		}
		offset = currentRobotAngle - actualRobotAngle;
	}
}
