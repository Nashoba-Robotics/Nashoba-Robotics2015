package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.custom.PID;
import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.EncoderPIDSource;
import edu.nr.robotics.subsystems.drive.gyro.AngleGyroCorrection;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonDriveShortDistance extends CMD implements PIDOutput
{
	private final double distance = -58d/12;
	private PID pid;
	AngleGyroCorrection gyroCorrection;
	double currentSetSpeed = 0;
	EncoderPIDSource encoderSource;
	
	public AutonDriveShortDistance()
	{
		requires(Drive.getInstance());
		encoderSource = new EncoderPIDSource();
		
		pid = new PID(0.1, 0.001, 0, encoderSource, this);
		pid.setSetpoint(distance);
		gyroCorrection = new AngleGyroCorrection();
		Drive.getInstance().setTalonRampRate(24);
	}
	
	@Override
	protected void onStart() 
	{
		pid.enable();
		gyroCorrection.clearInitialValue();
		pid.resetTotalError();
		encoderSource.resetInitialValue();
	}

	@Override
	protected void onExecute() 
	{
		if(Math.abs(distance) - Math.abs(pid.getError()) > 1)
			Drive.getInstance().setTalonRampRate(0);
		double turn = gyroCorrection.getTurnValue();
		SmartDashboard.putNumber("Auton Current Set Speed", currentSetSpeed);
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
		return (Drive.getInstance().getBumper1() && Drive.getInstance().getBumper2());
	}

	@Override
	protected void onEnd(boolean interrupted) 
	{
		pid.disable();
		Drive.getInstance().arcadeDrive(0,0);
	}
}