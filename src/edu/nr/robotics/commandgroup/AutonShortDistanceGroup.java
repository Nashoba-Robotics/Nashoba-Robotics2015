package edu.nr.robotics.commandgroup;

import edu.nr.robotics.custom.IsFinishedShare;
import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.nr.robotics.subsystems.backElevator.commands.BackElevatorGoToHeightCommand;
import edu.nr.robotics.subsystems.backElevator.commands.BackElevatorHeightWithShare;
import edu.nr.robotics.subsystems.drive.commands.AutonDriveShortDistance;
import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.nr.robotics.subsystems.drive.commands.EndAutoStall;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutonShortDistanceGroup extends CommandGroup
{
	public AutonShortDistanceGroup()
	{
		this.addSequential(new AutonDriveShortDistance());
		this.addSequential(new WaitCommand(0.2));
		
		IsFinishedShare share = new IsFinishedShare();
		this.addParallel(new EndAutoStall(share));
		this.addSequential(new BackElevatorHeightWithShare(BackElevator.HEIGHT_HOLD, share));
		
		this.addSequential(new DriveDistanceCommand(9.5, 1, 0.5));
	}
}
