package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontElevatorGoToHeightCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveToPlayerStationDistance extends DriveDistanceCommand
{
	private double distance = 2;
	private boolean liftBin =false;
	public DriveToPlayerStationDistance(double distance, boolean liftBin)
	{
		super(0, 0, 0.3);
		this.distance = distance;
		this.liftBin = liftBin;
	}

	public void onStart()
	{
		try
		{
			this.setSetpoint(SmartDashboard.getNumber("TargetDistance") - distance);
		}
		catch(Exception e)
		{
			this.cancel();
			System.out.println("Drive to player station canceled!!");
		}
	}
	
	@Override
	public void onEnd(boolean interrupted)
	{
		super.onEnd(interrupted);
		new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_RECEiVE_FIRST_TOTE).start();
	}
}
