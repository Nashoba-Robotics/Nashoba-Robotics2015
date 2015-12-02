package edu.nr.lib;

import java.util.HashMap;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ProcessTimer 
{
	private static HashMap<String, Long> processes = new HashMap<String, Long>();
	
	public static void startTimer(String processName)
	{
		processes.put(processName, System.currentTimeMillis());
	}
	
	public static void stopTimer(String processName)
	{
		if(processes.containsKey(processName))
		{
			long delta = System.currentTimeMillis() - processes.get(processName);
			SmartDashboard.putNumber(processName, delta);
		}
	}
}
