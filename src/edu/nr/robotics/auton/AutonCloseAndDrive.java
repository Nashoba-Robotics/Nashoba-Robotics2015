package edu.nr.robotics.auton;

import edu.nr.robotics.subsystems.binGrabber.CloseBinGrabberCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontElevatorGoToHeightCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutonCloseAndDrive extends CommandGroup
{
	public AutonCloseAndDrive()
	{
		this.addSequential(new CloseBinGrabberCommand());
		this.addSequential(new WaitCommand(2));
		this.addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BEFORE_TOTE_ADJUST));
		this.addSequential(new WaitCommand(1));
		this.addSequential(new DriveDistanceCommand(-8, 1, 0.4));
	}
}
