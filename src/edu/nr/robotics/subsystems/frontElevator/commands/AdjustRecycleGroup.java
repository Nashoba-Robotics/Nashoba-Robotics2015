package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.binGrabber.CloseBinGrabberCommand;
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
		this.addSequential(new CloseBinGrabberCommand());
		this.addSequential(new WaitCommand(0.25));
		this.addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_PICK_UP_TOTE_TWO));
		this.addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_WAITING));
	}
}
		