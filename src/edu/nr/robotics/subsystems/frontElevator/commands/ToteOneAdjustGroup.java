package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class ToteOneAdjustGroup extends CommandGroup
{
	//TODO Test this command
	public ToteOneAdjustGroup()
	{
		this.addSequential(new FrontArmsOpenCommand());
		this.addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_ADJUST_TOTE_ONE));
		this.addSequential(new FrontArmsCloseCommand());
	}
}
