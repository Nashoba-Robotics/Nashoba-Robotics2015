package edu.nr.robotics.subsystems.drive;

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
		return Drive.getInstance().getEncoderAve() - initialDistance;
	}
	
	public void resetInitialValue()
	{
		initialDistance = Drive.getInstance().getEncoderAve();
	}
}
