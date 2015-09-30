package edu.nr.robotics.auton;

import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontArmsOpenCommand;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontElevatorGoToHeightCommand;
import edu.nr.robotics.subsystems.whip.WhipDeployCommand;
import edu.nr.robotics.subsystems.whip.WhipDeployGroup;
import edu.nr.robotics.subsystems.whip.WhipDeployLockCommand;
import edu.nr.robotics.subsystems.whip.WhipUndeployLockCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonWhipAndDrive extends CommandGroup
{
	public AutonWhipAndDrive()
	{
		DriveDistanceCommand temp = new DriveDistanceCommand(0, 0, 0);
		this.addParallel(temp);
		this.addSequential(new WhipDeployGroup());
		//TODO get the right wait time
		try
		{
			this.addSequential(new WaitCommand(SmartDashboard.getNumber("Auton Whip Wait Time")));
		}
		catch(Exception e)
		{
			this.addSequential(new WaitCommand(0.5));
		}
		//DriveDistanceCommand drv = new DriveDistanceCommand(6, 0, 1);
		//drv.setDebug(true);
		this.addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BOTTOM));
	}
}
