package edu.nr.robotics.commandgroup;

import edu.nr.robotics.custom.IsFinishedShare;
import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.nr.robotics.subsystems.backElevator.commands.BackElevatorGoToHeightCommand;
import edu.nr.robotics.subsystems.backElevator.commands.BackElevatorHeightWithShare;
import edu.nr.robotics.subsystems.drive.commands.AutonDriveToStepShort;
import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.nr.robotics.subsystems.drive.commands.EndAutoStall;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontElevatorGoToHeightCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutonRedeemGroup extends CommandGroup
{
	IsFinishedShare share;
	
	private final double DISTANCE_STEP_TO_ROBOT_SET = 9.9;
	private final double DISTANCE_STEP_TO_RECYCLE_SET = 13;
	
	public AutonRedeemGroup(AutonType type)
	{
		super(splitCamelCase(type.toString()));
		
		//Drive to the step
		this.addParallel(new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_OBTAIN_STEP));
		if(type == AutonType.ShortDistanceRecycleSet || type == AutonType.ShortDistanceRobotSet)
		{
			this.addSequential(new AutonDriveToStepShort(8));
		}
		
		this.addSequential(new WaitCommand(0.2));
		
		//Lift the bins, and keep driving forward into step
		share = new IsFinishedShare();
		this.addParallel(new EndAutoStall(share));
		this.addSequential(new BackElevatorHeightWithShare(BackElevator.HEIGHT_HOLD, share));
		
		if(type == AutonType.ShortDistanceRobotSet || type == AutonType.LongDistanceRobotSet)
		{
			this.addSequential(new DriveDistanceCommand(DISTANCE_STEP_TO_ROBOT_SET, 1.5, 0.5));
		}
		else if(type == AutonType.ShortDistanceRecycleSet || type == AutonType.LongDistanceRecycleSet)
		{
			this.addSequential(new DriveDistanceCommand(DISTANCE_STEP_TO_RECYCLE_SET, 1, 0.5));
		}
		else
			System.out.println("Error: Couldn't add a final drive command to autonomous");
		
		this.addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BOTTOM));
		
	}
	
	public enum AutonType
	{
		ShortDistanceRecycleSet, ShortDistanceRobotSet, LongDistanceRecycleSet, LongDistanceRobotSet
	}
	
	protected void end()
	{
		share.isFinished = false;
	}
	
	protected void interrupted()
	{
		end();
	}
	
	/**
	 * Turns camel case strings into human readable space-separated strings
	 */
	static String splitCamelCase(String s) {
		   return s.replaceAll(
		      String.format("%s|%s|%s",
		         "(?<=[A-Z])(?=[A-Z][a-z])",
		         "(?<=[^A-Z])(?=[A-Z])",
		         "(?<=[A-Za-z])(?=[^A-Za-z])"
		      ),
		      " "
		   );
		}
}
