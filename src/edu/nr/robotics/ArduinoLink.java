package edu.nr.robotics;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ArduinoLink 
{	
	private static ArduinoLink instance;
	public static ArduinoLink getInstance()
	{
		if(instance == null)
			instance = new ArduinoLink();
		return instance;
	}
	
	public enum State {
		OFF,COUNTDOWN,SCORING;
	}
	
	private State previousState = State.OFF;
	private long startedScoringTime = 0;
	
	private State state = State.OFF;
	private ArduinoLink()
	{
	}
	
	public void updateDisabled()
	{
		SmartDashboard.putString("ArduinoState", State.OFF.toString());
		SmartDashboard.putNumber("ArduinoArgument", 0);
	}
	
	public void updateAuton()
	{
		SmartDashboard.putString("ArduinoState", State.OFF.toString());
		SmartDashboard.putNumber("ArduinoArgument", 0);
	}
	
	public void updateTeleop()
	{
		try
		{
			int time = (int)Math.round(DriverStation.getInstance().getMatchTime());
			
			if(state.equals(State.SCORING))
			{
				if(System.currentTimeMillis() - startedScoringTime > 1000)
					state = previousState;
			}
			
			if(time <= 16)
				state = State.COUNTDOWN;
			
			SmartDashboard.putString("ArduinoState", state.toString());
			
			if(state == State.COUNTDOWN)
			{
				SmartDashboard.putNumber("ArduinoArgument", time);
			}
			else
			{
				SmartDashboard.putNumber("ArduinoArgument", 0);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public void scoreYo()
	{
		startedScoringTime = System.currentTimeMillis();
		previousState = state;
		state = State.SCORING;
	}
}
