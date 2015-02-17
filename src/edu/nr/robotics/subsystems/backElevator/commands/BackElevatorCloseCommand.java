package edu.nr.robotics.subsystems.backElevator.commands;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class BackElevatorCloseCommand extends CommandGroup
{
	public BackElevatorCloseCommand()
	{
		this.addSequential(new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_BIN_LOWERED));
		BackElevatorGoToHeightCommand temp = new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_CLOSED);
		temp.setMaxSpeed(0.2);
		this.addSequential(temp);
	}
}
