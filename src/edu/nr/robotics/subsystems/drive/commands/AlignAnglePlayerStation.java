package edu.nr.robotics.subsystems.drive.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AlignAnglePlayerStation extends DriveAngleCommand
{
	public AlignAnglePlayerStation(boolean withTote)
	{
		super(0, false);
		if(withTote)
		{
			this.setP(1);
			this.setI(0.04);
		}
		else
		{
			this.setP(0.5);
			this.setI(0.04);
		}
	}

	@Override
	public void onStart()
	{
		try
		{
			if(SmartDashboard.getNumber("OutsideVisible") != 0)
			{
				this.setTargetAngle(SmartDashboard.getNumber("TargetAngleError"), false);
			}
			else
			{
				this.cancel();
			}
		}
		catch(Exception e)
		{
			this.cancel();
		}
		super.onStart();
	}
}
