package edu.nr.robotics.custom;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MotorPair implements PIDOutput
{
	private SpeedController first, second;
	private boolean debugMode = false;
	
	public MotorPair(SpeedController first, SpeedController second)
	{
		this.first = first;
		this.second = second;
	}
	
	public void enableDebug()
	{
		debugMode = true;
	}
	
	@Override
	public void pidWrite(double output) 
	{
		first.set(output);
		second.set(output);
		if(debugMode)
			SmartDashboard.putNumber("MotorPair actual output", output);
	}
	
	public void set(double output)
	{
		//Same thing
		pidWrite(output);
	}
	
	public void setCantTalonRampRate(double percentPerSecond)
	{
		if(first instanceof CantTalon && second instanceof CantTalon)
		{
			((CantTalon)first).setVoltageRampRate(percentPerSecond);
			((CantTalon)second).setVoltageRampRate(percentPerSecond);
		}
		else
		{
			printCantWarning();
		}
	}
	
	public void setCantTalonRampDirection(CantTalon.RampDirection direction)
	{
		if(isCantTalonPair())
		{
			((CantTalon)first).setVoltageRampRateDirection(direction);
			((CantTalon)second).setVoltageRampRateDirection(direction);
		}
		else
		{
			printCantWarning();
		}
	}
	
	public void disableCantTalonRamping()
	{
		if(isCantTalonPair())
		{
			((CantTalon)first).disableVoltageRamp();
			((CantTalon)second).disableVoltageRamp();
		}
		else
		{
			printCantWarning();
		}
	}
	
	public void enableCantTalonLimitSwitch(boolean forward, boolean reverse)
	{
		if(isCantTalonPair())
		{
			((CantTalon)first).enableLimitSwitch(forward, reverse);
			((CantTalon)second).enableLimitSwitch(forward, reverse);
		}
		else
		{
			printCantWarning();
		}
	}
	
	private boolean isCantTalonPair()
	{
		return (first instanceof CantTalon && second instanceof CantTalon);
	}
	
	private void printCantWarning()
	{
		System.err.println("Warning: Tried to use non-CantTalon speed controller as a CantTalon");
	}
}
