package edu.nr.robotics.subsystems.frontfingers;

import edu.nr.robotics.subsystems.CMD;

public class FrontFingersOpenCommand extends CMD
{
	public FrontFingersOpenCommand()
	{
		this.requires(FrontFingers.getInstance());
	}
	
	@Override
	protected void onStart() {
		FrontFingers.getInstance().open();
	}

	@Override
	protected void onExecute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onEnd(boolean interrupted) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

}
