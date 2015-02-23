package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AdjustRecycleGroup extends CommandGroup
{
	public AdjustRecycleGroup()
	{
		this.addSequential(new FrontElevatorGoToAdjustHeightWithHalfwayRelease());
		//this.addSequential(new ReleaseBinCommand());
		this.addSequential(new WaitCommand(0.4));
		this.addSequential(new GrabBinCommand());
		this.addSequential(new WaitCommand(0.5));
		this.addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_WAITING));
	}
}
		