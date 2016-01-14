package edu.nr.lib.PID;

import edu.nr.robotics.subsystems.drive.Drive;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class EncoderPIDSource implements PIDSource
{
	PIDSourceType type;
	
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

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		type = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return type;
	}
}
