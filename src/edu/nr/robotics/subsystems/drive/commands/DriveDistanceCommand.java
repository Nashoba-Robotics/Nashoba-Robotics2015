package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.gyro.AngleGyroCorrection;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveDistanceCommand extends CMD
{
	private double distanceFeet, speed;
	private AngleGyroCorrection gyroCorrection;
	
	private double initialEncoderDistance;
	
    public DriveDistanceCommand(double distanceFeet, double speed) 
    {
    	this.distanceFeet = distanceFeet;
    	this.speed = Math.abs(speed) * Math.signum(distanceFeet);
    	gyroCorrection = new AngleGyroCorrection();
    	requires(Drive.getInstance());
    }

	@Override
	protected void onStart() 
	{
		initialEncoderDistance = Drive.getInstance().getEncoderAve();
		Drive.getInstance().setDriveP(1);
	}

    @Override
	protected void onExecute()
    {
		double turn = gyroCorrection.getTurnValue();
		
		double distanceDriven = Drive.getInstance().getEncoderAve() - initialEncoderDistance;
		
		double tempSpeed = speed;
		
		double err = (distanceFeet - distanceDriven);
		
		double pMove = Math.max(Math.abs(err / 1* tempSpeed), 0.07) * Math.signum(tempSpeed);
		SmartDashboard.putNumber("P Move", pMove);
		
		double move;
		if(speed < 0)
			move = Math.max(pMove, tempSpeed);
		else
			move = Math.min(pMove, tempSpeed);
		
		Drive.getInstance().arcadeDrive(move, turn);
		
		SmartDashboard.putNumber("Drive Distance Move", move);
		SmartDashboard.putNumber("Drive Distance Turn", turn);
		SmartDashboard.putNumber("Drive Distance Err", err);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	double distanceDriven = Drive.getInstance().getEncoderAve() - initialEncoderDistance;
		double err = (distanceFeet - distanceDriven);
		
    	if(distanceFeet < 0)
    	{
    		return (err > 0);
    	}
		else
		{
			return (err < 0);
		}
    	
    }

	@Override
	protected void onEnd(boolean interrupted) 
	{
		Drive.getInstance().arcadeDrive(0, 0);
    	gyroCorrection.clearInitialValue();
    	Drive.getInstance().setDriveP(Drive.JOYSTICK_DRIVE_P);		
	}
}
