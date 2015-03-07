package edu.nr.robotics.commandgroup;

import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontElevatorGoToHeightCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class BackupAndLowerTote extends CommandGroup
{
	public BackupAndLowerTote()
	{
		this.addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BEFORE_TOTE_LOWERING));
		
		FrontElevatorGoToHeightCommand lower = new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BOTTOM);
		lower.setGoingDownMaxRange(0.25);
		this.addParallel(lower);
		
		DriveDistanceCommand driveBack = new DriveDistanceCommand(-4, 1, 0.2);
		driveBack.setRoughStopDistance(3.5);
		this.addSequential(driveBack);
	}
}
