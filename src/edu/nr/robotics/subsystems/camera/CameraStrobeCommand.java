package edu.nr.robotics.subsystems.camera;

import edu.nr.robotics.subsystems.CMD;

public class CameraStrobeCommand extends CMD
{
	private double strobeRate = 7;
	private double strobeWaitTime = 1000.0/strobeRate;
	
	private long durationMillis;
	private long startTime;
	private long previousStrobeTime;
	
	public CameraStrobeCommand(long durationMillis)
	{
		this.requires(Camera.getInstance());
		this.durationMillis = durationMillis;
	}
	
	@Override
	protected void onStart()
	{
		this.startTime = System.currentTimeMillis();
		this.previousStrobeTime = startTime;
	}

	@Override
	protected void onExecute()
	{
		long delta = System.currentTimeMillis() - previousStrobeTime;
		if(delta >= strobeWaitTime)
		{
			Camera.getInstance().toggleCamera();
			previousStrobeTime = System.currentTimeMillis();
		}
	}

	@Override
	protected boolean isFinished()
	{
		return (System.currentTimeMillis() - startTime) > durationMillis;
	}
	
	@Override
	protected void onEnd(boolean interrupted)
	{
		Camera.getInstance().cameraOff();
	}
}
