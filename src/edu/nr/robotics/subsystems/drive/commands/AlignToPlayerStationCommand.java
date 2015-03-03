package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AlignToPlayerStationCommand extends CMD
{
	//TODO Test player station alignment
	double epsilon;
	
	public AlignToPlayerStationCommand()
	{
		this.requires(Drive.getInstance());
	}
	
	@Override
	protected void onStart()
	{
		epsilon = -1;
	}

	@Override
	protected void onExecute()
	{
		boolean isVisible = (SmartDashboard.getNumber("TargetVisible") == 0)?false:true;
		double dx = SmartDashboard.getNumber("TargetX");
		epsilon = Math.abs(dx);
		
		double defaultDriveSpeed = 0.3;
		double pSpeed = Math.abs(dx) / 100 * defaultDriveSpeed;
		double driveSpeed = Math.min(defaultDriveSpeed, pSpeed) * Math.signum(dx);
		
		if(!isVisible)
			driveSpeed = 0;
		
		Drive.getInstance().setHDrive(driveSpeed);
	}

	@Override
	protected boolean isFinished()
	{
		return epsilon < 20 && epsilon > 0;
	}
	
	@Override
	protected void onEnd(boolean interrupted)
	{
		Drive.getInstance().setHDrive(0);
	}
}
