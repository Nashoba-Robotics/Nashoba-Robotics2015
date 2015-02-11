package edu.nr.robotics;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;

public class CantTalon implements SpeedController, PIDOutput
{
	CANTalon talon;
	public CantTalon(int deviceNumber) 
	{
		talon = new CANTalon(deviceNumber);
		initThread();
	}
	
	private double previousSetValueTime = -1;
	private double targetValue = 0;
	private double voltageRampRate = 0;
	private boolean rampEnabled = false;
	
	public void set(double value)
	{
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
	}
	
	public void setVoltageRampRate(double voltsPerSecond)
	{
		rampEnabled = true;
		voltageRampRate = Math.abs(voltsPerSecond);
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
						double deltaTime = (System.currentTimeMillis() - previousSetValueTime) *1000;
						
						double currentSetValue = talon.get();
						
						double incrementSign = Math.signum(targetValue - currentSetValue);
						double incrementValue = voltageRampRate * deltaTime;
						
						incrementValue = incrementSign * Math.min(incrementValue, Math.abs(targetValue - currentSetValue));
						
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
