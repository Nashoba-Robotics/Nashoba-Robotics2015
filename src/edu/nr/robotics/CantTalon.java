package edu.nr.robotics;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CantTalon implements SpeedController, PIDOutput
{
	private int deviceNum;
	
	CANTalon talon;
	public CantTalon(int deviceNumber) 
	{
		talon = new CANTalon(deviceNumber);
		initThread();
		deviceNum = deviceNumber;
	}
	
	private long previousSetValueTime = -1;
	private double targetValue = 0;
	private double voltageRampRate = 0;
	private boolean rampEnabled = false;
	private double currentSetValue = 0;
	
	public void set(double value)
	{
		value = Math.signum(value) * Math.min(1, Math.abs(value));
		
		if(rampEnabled)
		{
			targetValue = value;
		}
		else
		{
			rawSet(value);
		}
	}
	
	private void rawSet(double value)
	{
		previousSetValueTime = System.currentTimeMillis();
		talon.set(value);
		currentSetValue = value;
	}
	
	public void setVoltageRampRate(double percentPerSecond)
	{
		rampEnabled = true;
		voltageRampRate = Math.abs(percentPerSecond);
	}
	
	public void disableVoltageRamp()
	{
		rampEnabled = false;
	}
	
	private void initThread()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				while(true)
				{
					if(previousSetValueTime == -1)
						previousSetValueTime = System.currentTimeMillis();
					else
					{
						double deltaTime = (System.currentTimeMillis() - previousSetValueTime) / 1000d;
						
						if(deviceNum == RobotMap.HDriveTalon)
						{
							SmartDashboard.putNumber("Delta Time", deltaTime);
							SmartDashboard.putNumber("Talon Get", currentSetValue);
						}
						
						double incrementSign = Math.signum(targetValue - currentSetValue);
						
						double incrementValue = voltageRampRate * deltaTime;
						
						incrementValue = incrementSign * Math.min(incrementValue, Math.abs(targetValue - currentSetValue));
						
						if(deviceNum == RobotMap.HDriveTalon)
							SmartDashboard.putNumber("Increment per second", incrementValue / deltaTime);
						
						rawSet(currentSetValue + incrementValue);
					}
					
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}).start();
	}
	
	public void enableBrakeMode(boolean enabled)
	{
		talon.enableBrakeMode(enabled);
	}
	
	public void enableLimitSwitch(boolean forward, boolean reverse)
	{
		talon.enableLimitSwitch(forward, reverse);
	}

	@Override
	public void pidWrite(double output) 
	{
		set(output);
	}

	@Override
	public double get() 
	{
		return talon.get();
	}

	@Override
	public void set(double speed, byte syncGroup) {
		set(speed);
	}

	@Override
	public void disable() 
	{
		talon.disable();
	}
}
