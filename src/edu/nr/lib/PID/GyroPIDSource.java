package edu.nr.lib.PID;

import edu.nr.robotics.subsystems.drive.Drive;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GyroPIDSource implements PIDSource
{
	PIDSourceType type;
	
	@Override
	public double pidGet() 
	{
		double angle = Drive.getInstance().getAngleRadians();
		SmartDashboard.putNumber("Gyro Pid Source", angle);
		return angle;
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
