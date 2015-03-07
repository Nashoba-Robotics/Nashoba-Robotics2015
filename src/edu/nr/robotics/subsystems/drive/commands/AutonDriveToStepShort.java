package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.custom.PID;
import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.EncoderPIDSource;
import edu.nr.robotics.subsystems.drive.gyro.AngleGyroCorrection;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonDriveToStepShort extends CMD implements PIDOutput
{
	private final double distance = -59d/12;
	private PID pid;
	AngleGyroCorrection gyroCorrection;
	double currentSetSpeed = 0;
	EncoderPIDSource encoderSource;
	private double timeoutMillis;
	private double startTime;
	
	public AutonDriveToStepShort(double timeout)
	{
		super(timeout);
		timeoutMillis = timeout * 1000;
		requires(Drive.getInstance());
		encoderSource = new EncoderPIDSource();
		
		pid = new PID(0.1, 0.001, 0, encoderSource, this);
		pid.setSetpoint(distance);
		gyroCorrection = new AngleGyroCorrection();
	}
	
	@Override
	protected void onStart() 
	{
		pid.enable();
		gyroCorrection.clearInitialValue();
		pid.resetTotalError();
		encoderSource.resetInitialValue();
		Drive.getInstance().setTalonRampRate(24);
		startTime = System.currentTimeMillis();
	}

	@Override
	protected void onExecute() 
	{
		if(Math.abs(distance) - Math.abs(pid.getError()) > 1)
			Drive.getInstance().setTalonRampRate(0);
		double turn = gyroCorrection.getTurnValue();
		SmartDashboard.putNumber("Auton Current Set Speed", currentSetSpeed);
		
		currentSetSpeed = Math.max(Math.abs(currentSetSpeed), 0.25) * Math.signum(currentSetSpeed);
		
		Drive.getInstance().arcadeDrive(currentSetSpeed, turn);
	}
	
	@Override
	public void pidWrite(double output) 
	{
		currentSetSpeed = output;
	}
	
	@Override
	protected boolean isFinished() 
	{
		return (Drive.getInstance().getBumper1() && Drive.getInstance().getBumper2()) || this.isTimedOut();
	}

	@Override
	protected void onEnd(boolean interrupted) 
	{
		pid.reset();
		Drive.getInstance().arcadeDrive(0,0);
		Drive.getInstance().setTalonRampRate(0);
	}
}
