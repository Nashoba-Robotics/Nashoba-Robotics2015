package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.frontElevator.FrontElevator;

public class FrontElevatorGoToAdjustHeightWithHalfwayRelease extends FrontElevatorGoToHeightCommand
{
	private boolean released = false;
	
	public FrontElevatorGoToAdjustHeightWithHalfwayRelease() 
	{
		super(FrontElevator.HEIGHT_ADJUST_BIN);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		released = false;
	}
	
	@Override
	protected void onExecute()
	{
		super.onExecute();
		
		if(FrontElevator.getInstance().pidGet() < FrontElevator.HEIGHT_RELEASE_BIN_WHILE_GOING_DOWN && !released)
		{
			released = true;
			FrontElevator.getInstance().binGrabberReverse();
		}
	}
}
