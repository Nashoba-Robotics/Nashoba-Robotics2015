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
		count = 0;
	}
	
	private double count;

	@Override
	protected void onExecute()
	{
		boolean isVisible = (SmartDashboard.getNumber("TargetVisible") == 0)?false:true;
		double dx = SmartDashboard.getNumber("TargetX");
		epsilon = Math.abs(dx);
		
		double defaultDriveSpeed = 0.3;
		double pSpeed = Math.abs(dx) / 50 * defaultDriveSpeed;
		double driveSpeed = Math.min(defaultDriveSpeed, pSpeed) * Math.signum(dx);
		
		if(!isVisible)
			driveSpeed = 0;
		
		driveSpeed = -driveSpeed;
		
		if(epsilon < 40)
			count++;
		driveSpeed += (Math.signum(driveSpeed) * count * 0.001);
		
		SmartDashboard.putNumber("Align Player Station Drive Speed", driveSpeed);
		Drive.getInstance().setHDrive(driveSpeed);
	}

	@Override
	protected boolean isFinished()
	{
		SmartDashboard.putNumber("Align Player Station Epsilon", epsilon);
		return epsilon < 10 && epsilon > 0;
	}
	
	@Override
	protected void onEnd(boolean interrupted)
	{
		Drive.getInstance().setHDrive(0);
	}
}
