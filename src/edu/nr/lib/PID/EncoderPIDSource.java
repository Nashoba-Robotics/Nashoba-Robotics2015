package edu.nr.lib.PID;

import edu.nr.robotics.subsystems.drive.Drive;
import edu.wpi.first.wpilibj.PIDSource;

public class EncoderPIDSource implements PIDSource
{
	private double initialDistance;
	public EncoderPIDSource()
	{
		resetInitialValue();
	}
	
	@Override
	public double pidGet()
	{
		return Drive.getInstance().getEncoderAverageDistance() - initialDistance;
	}
	
	public void resetInitialValue()
	{
		initialDistance = Drive.getInstance().getEncoderAverageDistance();
	}
}
