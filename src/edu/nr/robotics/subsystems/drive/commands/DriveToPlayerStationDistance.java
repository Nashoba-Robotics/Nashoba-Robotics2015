package edu.nr.robotics.subsystems.drive.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveToPlayerStationDistance extends DriveDistanceCommand
{
	private final double DISTANCE = 2;
	public DriveToPlayerStationDistance()
	{
		super(0, 0, 0.3);
	}

	public void onStart()
	{
		try
		{
			this.setSetpoint(SmartDashboard.getNumber("TargetDistance") - DISTANCE);
		}
		catch(Exception e)
		{
			this.cancel();
			System.out.println("Drive to player station canceled!!");
		}
	}
}
