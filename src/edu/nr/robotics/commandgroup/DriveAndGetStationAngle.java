package edu.nr.robotics.commandgroup;

import edu.nr.robotics.EmptyCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveAndGetStationAngle extends CommandGroup
{
	public DriveAndGetStationAngle()
	{
		DriveDistanceCommand driveWithTote = new DriveDistanceCommand(4, 3, 0.25);
		driveWithTote.setRoughStopDistance(.5);
		this.addSequential(driveWithTote);
	}
}
