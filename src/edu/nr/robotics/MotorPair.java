package edu.nr.robotics;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MotorPair implements PIDOutput
{
	private static int count = 1;
	
	private SpeedController first, second;
	private int num;
	
	public MotorPair(SpeedController first, SpeedController second)
	{
		num = count;
		count++;
		this.first = first;
		this.second = second;
	}
	
	@Override
	public void pidWrite(double output) 
	{
		first.set(output);
		second.set(output);
		SmartDashboard.putNumber("MotorPair " + num + " actual output", output);
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
