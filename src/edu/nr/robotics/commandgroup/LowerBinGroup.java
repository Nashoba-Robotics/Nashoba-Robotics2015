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
		//Lower the elevator util the bins just touch the ground
		addSequential(new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_BINS_GRAZE_GROUND));
		
		//Drive forward while lowering the elevator slowly
		addParallel(new DriveDistanceCommand(.5, 0.6, 0.3));
		BackElevatorGoToHeightCommand temp = new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_BIN_LOWERED_FULLY);
		temp.setMaxSpeed(0.4);
		addSequential(temp);
		
		//Finally, drive forward to finish the release
		addSequential(new DriveDistanceCommand(1, 1.1, 0.4));
	}
}
