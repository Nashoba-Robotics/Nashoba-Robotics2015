package edu.nr.robotics.auton;

import edu.nr.robotics.subsystems.CMD;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class BooleanWaitCommand extends CMD
{
	private String key;
	private boolean defaultValue;
	
	/**
	 * 
	 * @param timeout The timeout to wait if the smartdashboard value is false
	 * @param key The key to check. True will cancel, false will wait.
	 * @param defaultValue The default value if the smartdashboard value can't be retrieved
	 */
	public BooleanWaitCommand(double timeout, String key, boolean defaultValue)
	{
		this.setTimeout(timeout);
		this.key = key;
		this.defaultValue = defaultValue;
	}
	
	private boolean cancelEarly = false;
	
	@Override
	protected void onStart() 
	{
		try
		{
			if(SmartDashboard.getBoolean(key))
				this.cancelEarly = true;
		}
		catch(Exception e)
		{
			if(defaultValue)
				this.cancelEarly = true;
		}
	}

	@Override
	protected void onExecute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onEnd(boolean interrupted) {
		
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return this.isTimedOut() || cancelEarly;
	}
	
}
