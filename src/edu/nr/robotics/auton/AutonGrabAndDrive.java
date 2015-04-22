package edu.nr.robotics.auton;

import edu.nr.robotics.subsystems.binGrabber.CloseBinGrabberCommand;
import edu.nr.robotics.subsystems.binGrabber.OpenBinGrabberCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontArmsCloseCommand;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontArmsOpenCommand;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontElevatorGoToHeightCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutonGrabAndDrive extends CommandGroup
{
	public AutonGrabAndDrive()
	{
		this.addSequential(new FrontArmsCloseCommand());
		this.addSequential(new OpenBinGrabberCommand());
		this.addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BOTTOM));
		this.addSequential(new CloseBinGrabberCommand());
		this.addSequential(new WaitCommand(1));
		this.addSequential(new FrontArmsCloseCommand());
		this.addSequential(new WaitCommand(1));
		this.addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BEFORE_TOTE_ADJUST));
		this.addSequential(new WaitCommand(1));
		this.addSequential(new DriveDistanceCommand(-7.8, 1, 0.4));
		this.addSequential(new FrontArmsOpenCommand());
	}
}
