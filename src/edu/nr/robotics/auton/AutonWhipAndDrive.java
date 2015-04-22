package edu.nr.robotics.auton;

import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontArmsOpenCommand;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontElevatorGoToHeightCommand;
import edu.nr.robotics.subsystems.whip.WhipDeployCommand;
import edu.nr.robotics.subsystems.whip.WhipDeployLockCommand;
import edu.nr.robotics.subsystems.whip.WhipUndeployLockCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutonWhipAndDrive extends CommandGroup
{
	public AutonWhipAndDrive()
	{
		this.addSequential(new WhipDeployCommand());
		this.addSequential(new WhipUndeployLockCommand());
		//TODO get the right wait time
		this.addSequential(new WaitCommand(0.1));
		this.addSequential(new DriveDistanceCommand(5, 0, 1));
		this.addSequential(new FrontArmsOpenCommand());
		this.addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BOTTOM));
	}
}
