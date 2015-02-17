package edu.nr.robotics.commandgroup;

import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.nr.robotics.subsystems.backElevator.commands.BackElevatorGoToHeightCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class LowerBinGroup extends CommandGroup
{
	public LowerBinGroup()
	{
		addSequential(new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_BINS_GRAZE_GROUND));
		/*addSequential(new WaitCommand(0.2));
		addSequential(new LowerBinAndDrive());*/
		addParallel(new DriveDistanceCommand(.5, 0.6, 0.3));
		BackElevatorGoToHeightCommand temp = new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_BIN_LOWERED);
		temp.setMaxSpeed(0.4);
		addSequential(temp);
		addSequential(new DriveDistanceCommand(1, 1.1, 0.4));
	}
}
