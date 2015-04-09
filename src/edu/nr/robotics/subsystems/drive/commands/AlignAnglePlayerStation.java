package edu.nr.robotics.subsystems.drive.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AlignAnglePlayerStation extends DriveAngleCommand
{
	public AlignAnglePlayerStation()
	{
		super(0, false);
		this.setP(1);
		this.setI(0.04);
	}

	@Override
	public void onStart()
	{
		try
		{
			this.setTargetAngle(SmartDashboard.getNumber("TargetAngleError"), false);
		}
		catch(Exception e)
		{
			this.cancel();
		}
		super.onStart();
	}
}
