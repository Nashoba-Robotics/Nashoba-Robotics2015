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
	
	private final String STATE_OFF = "Off",
			STATE_COUNTDOWN = "Countdown",
			STATE_SCORING = "Score";
	
	private String previousState = STATE_OFF;
	private long startedScoringTime = 0;
	
	private String state = STATE_OFF;
	private ArduinoLink()
	{
	}
	
	public void updateDisabled()
	{
		SmartDashboard.putString("ArduinoState", STATE_OFF);
		SmartDashboard.putNumber("ArduinoArgument", 0);
	}
	
	public void updateAuton()
	{
		SmartDashboard.putString("ArduinoState", STATE_OFF);
		SmartDashboard.putNumber("ArduinoArgument", 0);
	}
	
	public void updateTeleop()
	{
		try
		{
			int time = (int)Math.round(DriverStation.getInstance().getMatchTime());
			
			if(state.equals(STATE_SCORING))
			{
				if(System.currentTimeMillis() - startedScoringTime > 1000)
					state = previousState;
			}
			
			if(time <= 16)
				state = STATE_COUNTDOWN;
			
			SmartDashboard.putString("ArduinoState", state);
			
			if(state.equalsIgnoreCase(STATE_COUNTDOWN))
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
		state = STATE_SCORING;
	}
}
