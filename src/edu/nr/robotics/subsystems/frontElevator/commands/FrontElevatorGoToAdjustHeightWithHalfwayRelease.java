package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.frontElevator.FrontElevator;

public class FrontElevatorGoToAdjustHeightWithHalfwayRelease extends FrontElevatorGoToHeightCommand
{
	private double releaseHeight;
	private boolean released = false;
	
	public FrontElevatorGoToAdjustHeightWithHalfwayRelease(double height) 
	{
		super(height);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		released = false;
		releaseHeight = (FrontElevator.getInstance().pidGet() - height)/2;
	}
	
	@Override
	protected void onExecute()
	{
		super.onExecute();
		
		if(FrontElevator.getInstance().pidGet() < releaseHeight && !released)
		{
			released = true;
			FrontElevator.getInstance().binGrabberReverse();
		}
	}
}
