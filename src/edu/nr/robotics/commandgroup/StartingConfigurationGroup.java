package edu.nr.robotics.commandgroup;

import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.nr.robotics.subsystems.backElevator.commands.BackElevatorGoToHeightCommand;
import edu.nr.robotics.subsystems.binGrabber.OpenBinGrabberCommand;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontElevatorGoToHeightCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class StartingConfigurationGroup extends CommandGroup
{
	public StartingConfigurationGroup()
	{
		this.addSequential(new OpenBinGrabberCommand());
		this.addParallel(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BOTTOM));
		this.addSequential(new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_OBTAIN_STEP));
	}
}
