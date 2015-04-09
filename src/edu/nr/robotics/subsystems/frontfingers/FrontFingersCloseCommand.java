package edu.nr.robotics.subsystems.frontfingers;

import edu.nr.robotics.subsystems.CMD;

public class FrontFingersCloseCommand extends CMD
{
	public FrontFingersCloseCommand()
	{
		this.requires(FrontFingers.getInstance());
	}
	
	@Override
	protected void onStart() 
	{
		FrontFingers.getInstance().close();
	}

	@Override
	protected void onExecute() {
		
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
