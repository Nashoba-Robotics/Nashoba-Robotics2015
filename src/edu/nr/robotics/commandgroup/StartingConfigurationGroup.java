package edu.nr.robotics.commandgroup;

import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.nr.robotics.subsystems.backElevator.commands.BackElevatorGoToHeightCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class StartingConfigurationGroup extends CommandGroup
{
	public StartingConfigurationGroup()
	{
		this.addSequential(new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_OBTAIN_STEP));
	}
}
