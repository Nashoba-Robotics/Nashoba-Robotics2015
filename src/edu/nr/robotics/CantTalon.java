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
	
	private RampDirection rampDirection = RampDirection.Both;
	
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
	
	/**
	 * Sets the raw speed on the talon, and takes an updated timestamp
	 * @param value The percentage (-1.0 to 1.0) to set the voltage bus at
	 */
	private void rawSet(double value)
	{
		previousSetValueTime = System.currentTimeMillis();
		
		talon.set(value);
		currentSetValue = value;
		
		SmartDashboard.putNumber("output value", value);
	}
	
	/**
	 * Sets the voltage ramp rate. If ramping is disabled, this will enable it.
	 * @param percentPerSecond The ramp rate in percent per second
	 */
	public void setVoltageRampRate(double percentPerSecond)
	{
		rampEnabled = true;
		voltageRampRate = Math.abs(percentPerSecond);
	}
	
	/**
	 */
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
					//If the time hasn't been initialized, initialize it to the current time
					if(previousSetValueTime == -1)
						previousSetValueTime = System.currentTimeMillis();
					else
					{
						//Amount of time since the last iteration (in seconds)
						double deltaTime = (System.currentTimeMillis() - previousSetValueTime) / 1000d;
						
						double incrementSign = Math.signum(targetValue - currentSetValue);
						
						//The value that will be added to the current speed
						double incrementValue = voltageRampRate * deltaTime;
						
						//If we are close enough to the target value that we would overshoot,
						//only go as far as we need to go to get to the target value
						incrementValue = incrementSign * Math.min(incrementValue, Math.abs(targetValue - currentSetValue));
						
						
						//Will be true if we should ramp (at the end of the function)
						boolean shouldRamp = (rampDirection == RampDirection.Both);
						
						double newSetValue = currentSetValue + incrementValue;
						
						//If the speed is decreasing (acceleration and velocity have opposite signs)
						if((Math.signum(incrementValue) != Math.signum(newSetValue))
								&& rampDirection == RampDirection.SpeedDecrease)
						{
							shouldRamp = true;
						}
						//If the speed is increasing (acceleration and velocity have the same signs)
						else if((Math.signum(incrementValue) == Math.signum(newSetValue))
								&& rampDirection == RampDirection.SpeedIncrease)
						{
							shouldRamp = true;
						}
						
						if(shouldRamp)
						{
							//Set the talons to the incremented value
							rawSet(newSetValue);
						}
						else
						{
							//Go straight to the target value if we shouldn't ramp
							rawSet(targetValue);
						}
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
	
	/**
	 * The allowed direction of acceleration for the voltage ramping.
	 * 
	 */
	public enum RampDirection
	{
		Both, SpeedIncrease, SpeedDecrease
	}
	
	public void setVoltageRampRateDirection(RampDirection direction)
	{
		this.rampDirection = direction;
	}
	
}
