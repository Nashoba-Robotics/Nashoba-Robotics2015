package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.gyro.AngleGyroCorrection;
import edu.nr.robotics.subsystems.drive.gyro.GyroCorrection;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoDriveDistance extends CMD
{
	private long startTime;
	private double speed;
	private final double DISTANCE = 221/12;
	private GyroCorrection gyroCorrection;
	
	private double initialEncoderDistance;
	
    public AutoDriveDistance(double speed) 
    {
    	gyroCorrection = new AngleGyroCorrection();
    }

    @Override
	public void onStart() 
	{
		initialEncoderDistance = Drive.getInstance().getEncoderAve();
		Drive.getInstance().setDriveP(3);
		startTime = System.currentTimeMillis();
	}

    protected void onExecute()
    {
		double turn = gyroCorrection.getTurnValue();
		
		double distanceDriven = Drive.getInstance().getEncoderAve() - initialEncoderDistance;
		
		double tempSpeed = speed;
		
		double absDriven = Math.abs(distanceDriven);
		if((absDriven < 68/12) || (absDriven > 11  && absDriven < 15))
			tempSpeed = Math.signum(tempSpeed) * 0.2;
		else if(absDriven > 16.8)
			tempSpeed = Math.signum(tempSpeed) * 0.1;
		
		double err = (DISTANCE - distanceDriven);
		
		double pMove = Math.abs(err * tempSpeed) * Math.signum(tempSpeed);
		SmartDashboard.putNumber("P Move", pMove);
		
		double move;
		if(speed < 0)
			move = Math.max(pMove, tempSpeed);
		else
			move = Math.min(pMove, tempSpeed);
		
		Drive.getInstance().arcadeDrive(move, turn);
		
		SmartDashboard.putNumber("Drive Distance Move", move);
		SmartDashboard.putNumber("Drive Distance Turn", turn);
		SmartDashboard.putNumber("Err", err);
    }

    protected boolean isFinished() 
    {
    	return (Drive.getInstance().getBumper1() && Drive.getInstance().getBumper2());
    }

    protected void onEnd(boolean interrupted) 
    {
    	Drive.getInstance().arcadeDrive(0, 0);
    	SmartDashboard.putNumber("Drive Distance Time", (System.currentTimeMillis() - startTime)/1000f);
    	gyroCorrection.clearInitialValue();
    	Drive.getInstance().setDriveP(0.5);
    }
}
