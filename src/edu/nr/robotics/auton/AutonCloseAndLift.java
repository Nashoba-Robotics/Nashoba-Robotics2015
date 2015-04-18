package edu.nr.robotics.auton;

import edu.nr.robotics.subsystems.binGrabber.CloseBinGrabberCommand;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontElevatorGoToHeightCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutonCloseAndLift extends CommandGroup
{
	public AutonCloseAndLift()
	{
		this.addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BOTTOM));
		this.addSequential(new CloseBinGrabberCommand());
		this.addSequential(new WaitCommand(2));
		this.addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BEFORE_TOTE_ADJUST));
	}
}
