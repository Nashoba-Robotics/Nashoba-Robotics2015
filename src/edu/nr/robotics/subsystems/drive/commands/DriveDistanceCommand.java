package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.lib.CMD;
import edu.nr.lib.PID.PID;
import edu.nr.lib.PID.EncoderPIDSource;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.gyro.AngleGyroCorrection;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveDistanceCommand extends CMD implements PIDOutput
{
	private AngleGyroCorrection gyroCorrection;
	private PID pid;
	private double stopRampDistance;
	private double usualErrorSign;
	private double currentSetThrust = 0;
	private double totalDistance;
	private double roughStopDistance = 0;
	private boolean roughEnabled = false;
	private double maxSpeed;
	
	private boolean useI = false;
	private double startCountingIError;
	
	private EncoderPIDSource encoderSource;
	
    public DriveDistanceCommand(double distanceFeet, double stopRampDistance, double maxSpeed) 
    {
    	requires(Drive.getInstance());
    	
    	encoderSource = new EncoderPIDSource();
    	
    	pid = new PID(0.25, 0.0, 0, encoderSource, this);
    	pid.setSetpoint(distanceFeet);
    	gyroCorrection = new AngleGyroCorrection();
    	
    	usualErrorSign = Math.signum(pid.getError());
    	totalDistance = distanceFeet;
    	this.stopRampDistance = stopRampDistance;
    	this.maxSpeed = Math.abs(maxSpeed);
    }
    
    public void setRoughStopDistance(double roughDistanceFromTarget)
    {
    	roughEnabled = true;
    	roughStopDistance = Math.abs(roughDistanceFromTarget);
    }
    
    public void setIParams(double startCountingErr, double i)
    {
    	pid.setPID(pid.getP(), i, pid.getD());
    	startCountingIError = startCountingErr;
    	useI = true;
    }
    
    public void setP(double value)
    {
    	pid.setPID(value, pid.getI(), pid.getD());
    }
	@Override
	protected void onStart() 
	{
		pid.enable();
		Drive.getInstance().setDriveP(1);
		Drive.getInstance().setTalonRampRate(24);
		encoderSource.resetInitialValue();
		pid.resetTotalError();
	}
	
	@Override
	public void pidWrite(double output) 
	{
		currentSetThrust = output;
	}
	
	protected void setSetpoint(double value)
	{
		pid.setSetpoint(value);
		usualErrorSign = Math.signum(pid.getError());
    	totalDistance = value;
	}
	
	protected void resetEncoderSource()
	{
		encoderSource.resetInitialValue();
	}

    @Override
	protected void onExecute()
    {
    	if(!useI || !(Math.abs(pid.getError()) < startCountingIError))
    	{
    		pid.resetTotalError();
    	}
    	
    	if(Math.abs(totalDistance) - Math.abs(pid.getError()) > stopRampDistance)
    	{
    		Drive.getInstance().setTalonRampRate(0);
    	}
		double turn = gyroCorrection.getTurnValue();
		
		double tempThrust = Math.min(Math.abs(currentSetThrust), maxSpeed) * Math.signum(currentSetThrust);
		
		if(debug)
		{
			SmartDashboard.putNumber("Whip Thrust", tempThrust);
			SmartDashboard.putNumber("Whip Turn", turn);
			SmartDashboard.putNumber("Whip Error", pid.getError());
		}
		Drive.getInstance().arcadeDrive(tempThrust, turn);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
		if(roughEnabled)
		{
			return (Math.signum(pid.getError()) != usualErrorSign) || (Math.abs(pid.getError()) < roughStopDistance);
		}
		else
		{
			return Math.abs(pid.getError()) < 2d/12;
		}
    }

	@Override
	protected void onEnd(boolean interrupted) 
	{
		Drive.getInstance().arcadeDrive(0, 0);
    	gyroCorrection.clearInitialValue();
    	Drive.getInstance().setTalonRampRate(0);
    	Drive.getInstance().setDriveP(Drive.JOYSTICK_DRIVE_P);	
    	pid.reset();
	}
	
	private boolean debug = false;
	public void setDebug(boolean value)
	{
		this.debug = value;
	}
}
