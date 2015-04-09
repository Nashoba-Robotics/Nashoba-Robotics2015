package edu.nr.robotics.subsystems.frontfingers;

import edu.nr.robotics.subsystems.CMD;

public class FrontFingersIdle extends CMD
{
	public FrontFingersIdle()
	{
		this.requires(FrontFingers.getInstance());
	}
	
	@Override
	protected void onStart() {
		
	}

	@Override
	protected void onExecute() {
		
	}

	@Override
	protected void onEnd(boolean interrupted) {
		
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
