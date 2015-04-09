package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.gyro.AngleGyroCorrection;
import edu.nr.robotics.subsystems.drive.gyro.ConstantAngleGyroCorrection;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AlignHorizontalToPlayerStationCommand extends CMD
{
	//TODO Test player station alignment
	double centeringError, angleEpsilon;
	double epsilon = 10;
	private boolean withTote = false;
	
	AngleGyroCorrection gyroCorrection;
	
	public AlignHorizontalToPlayerStationCommand(boolean withTote)
	{
		this.requires(Drive.getInstance());
		gyroCorrection = new AngleGyroCorrection();
		this.withTote = withTote;
	}
	
	@Override
	protected void onStart()
	{
		centeringError = -1;
		count = 0;
	}
	
	private double count;

	@Override
	protected void onExecute()
	{
		boolean isVisible = false;
		try
		{
			isVisible = (SmartDashboard.getNumber("InsideVisible") == 0)?false:true;
		}
		catch(Exception e)
		{
			//roborealms not connected yet
		}
		
		try
		{
			double dx = SmartDashboard.getNumber("TargetX");
			centeringError = Math.abs(dx);
			
			double defaultDriveSpeed = 0.3;
			double pSpeed = Math.abs(dx) / 60 * defaultDriveSpeed;
			double driveSpeed = Math.min(defaultDriveSpeed, pSpeed) * Math.signum(dx);
			
			if(!isVisible)
				driveSpeed = 0;
			
			driveSpeed = -driveSpeed;
			
			if(centeringError < 40)
				count++;
			else
				count = 0;
			driveSpeed += (Math.signum(driveSpeed) * count * 0.0001);
			
			/*We don't want to rotate too fast, or the target will go off screen, so stop rotating when we are 3/4
			//from the center to the edge of the display
			if(dx < (imageWidth/2) * 3/4 && dx > (-imageWidth/2) * 3/4)
			{
				//Start slowing down at 3 degrees
				angleSpeed = Math.min(Math.abs(angle/3) * 0.2, 0.2);
				angleSpeed *= Math.signum(angle);
				SmartDashboard.putNumber("Target Angle Speed", angleSpeed);
				SmartDashboard.putBoolean("Using Target Angle", true);
			}
			else
			{
				SmartDashboard.putNumber("Target Angle Speed", 0);
				SmartDashboard.putBoolean("Using Target Angle", false);
			}*/
			
			Drive.getInstance().setHDrive(driveSpeed);
			double turnSpeed = 0;
			if(isVisible)
			{
				if(withTote)
					turnSpeed = gyroCorrection.getTurnValue(0.08);
				else
					turnSpeed = gyroCorrection.getTurnValue(0.03);
			}
			Drive.getInstance().arcadeDrive(0, turnSpeed);
		}
		catch(Exception e)
		{
			Drive.getInstance().setHDrive(0);
			Drive.getInstance().arcadeDrive(0, 0);
		}
	}

	private int correctCount = 0;
	
	public void setEpsilon(double value)
	{
		this.epsilon = Math.abs(value);
	}
	
	@Override
	protected boolean isFinished()
	{
		SmartDashboard.putNumber("Align Player Station Epsilon", centeringError);
		//SmartDashboard.putNumber("Angle Epsilon", angleEpsilon);
		if(centeringError < epsilon && centeringError > epsilon && Math.abs(gyroCorrection.getAngleErrorDegrees()) < 5)
				correctCount++;
		else
			correctCount = 0;
		
		return correctCount > 3;
	}
	
	@Override
	protected void onEnd(boolean interrupted)
	{
		Drive.getInstance().setHDrive(0);
		gyroCorrection.clearInitialValue();
		correctCount = 0;
	}
}
