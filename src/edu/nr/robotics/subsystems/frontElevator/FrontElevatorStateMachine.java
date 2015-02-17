package edu.nr.robotics.subsystems.frontElevator;

import edu.nr.robotics.subsystems.frontElevator.commands.AdjustRecycleGroup;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontElevatorGoToHeightCommand;
import edu.nr.robotics.subsystems.frontElevator.commands.ScoreGroup;
import edu.nr.robotics.subsystems.frontElevator.commands.ToteOneToScoreGroup;
import edu.nr.robotics.subsystems.frontElevator.commands.ToteTwoToWaitGroup;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FrontElevatorStateMachine 
{
	private static int stepCount = 0;
	
	private static Command[] commands = 
		{
			new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BEFORE_TOTE_ADJUST),
			new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_ADJUST_TOTE_ONE),
			new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_WAITING),
			new AdjustRecycleGroup(),//One tote in elevator, one at bottom
			new ToteTwoToWaitGroup(),//Two totes in elevator, one at bottom
			new ToteTwoToWaitGroup(),//Three totes in elevator, one at bottom
			new ToteOneToScoreGroup(),//Grab all 5
			new ScoreGroup()
		};
	
	public static Command getNextCommand()
	{
		if(stepCount > commands.length-1)
			stepCount = 0;
		
		Command cmd = commands[stepCount];
		stepCount++;
		
		SmartDashboard.putNumber("StepCount", stepCount);
		return cmd;
	}
	
	public static Command redoLastCommand()
	{
		if(stepCount-1 < 0)
			return commands[0];
		return commands[stepCount-1];
	}
	
	public static void reset()
	{
		stepCount = 0;
		SmartDashboard.putNumber("StepCount", stepCount);
	}
}
