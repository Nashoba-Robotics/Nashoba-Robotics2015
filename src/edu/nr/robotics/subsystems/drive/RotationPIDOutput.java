package edu.nr.robotics.subsystems.drive;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RotationPIDOutput implements PIDOutput
{
	@Override
	public void pidWrite(double output) 
	{
		output = Math.min(Math.abs(output), 0.3) * Math.signum(output);
		Drive.getInstance().arcadeDrive(0, -output);
		SmartDashboard.putNumber("Rotation PID Output", -output);
	}
}
