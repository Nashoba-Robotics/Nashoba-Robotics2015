package edu.nr.robotics.subsystems.camera;

import edu.nr.robotics.subsystems.CMD;

public class CameraOnCommand extends CMD
{
	public CameraOnCommand()
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
		Camera.getInstance().cameraOn();
	}

	@Override
	protected void onEnd(boolean interrupted) {
		
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

}
