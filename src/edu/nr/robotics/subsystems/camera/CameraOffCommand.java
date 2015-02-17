package edu.nr.robotics.subsystems.camera;

import edu.nr.robotics.subsystems.CMD;

public class CameraOffCommand extends CMD
{
	public CameraOffCommand()
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
		Camera.getInstance().cameraOff();
	}

	@Override
	protected void onEnd(boolean interrupted) {
		
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

}
