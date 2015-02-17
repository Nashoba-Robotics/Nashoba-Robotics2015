package edu.nr.robotics.subsystems.camera;

import edu.nr.robotics.subsystems.CMD;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CameraFollowDashboard extends CMD
{
	public CameraFollowDashboard()
	{
		this.requires(Camera.getInstance());
	}
	
	@Override
	protected void onStart() 
	{
		
	}

	@Override
	protected void onExecute()
	{
		double state = SmartDashboard.getNumber("RealmsCameraState");
		if(state == 0)
			Camera.getInstance().cameraOff();
		else
			Camera.getInstance().cameraOn();
	}

	@Override
	protected void onEnd(boolean interrupted) 
	{
		
	}

	@Override
	protected boolean isFinished() 
	{
		
		return false;
	}
}
