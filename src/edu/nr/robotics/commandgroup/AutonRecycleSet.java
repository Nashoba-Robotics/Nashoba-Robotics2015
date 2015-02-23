package edu.nr.robotics.commandgroup;

import edu.nr.robotics.custom.IsFinishedShare;
import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.nr.robotics.subsystems.backElevator.commands.BackElevatorGoToHeightCommand;
import edu.nr.robotics.subsystems.backElevator.commands.BackElevatorHeightWithShare;
import edu.nr.robotics.subsystems.drive.commands.AutonDriveShortDistance;
import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.nr.robotics.subsystems.drive.commands.EndAutoStall;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontElevatorGoToHeightCommand;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutonRecycleSet extends CommandGroup
{
	IsFinishedShare share;
	
	public AutonRecycleSet()
	{
		this.addParallel(new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_OBTAIN_STEP));
		this.addSequential(new AutonDriveShortDistance());
		this.addSequential(new WaitCommand(0.2));
		
		share = new IsFinishedShare();
		this.addParallel(new EndAutoStall(share));
		this.addSequential(new BackElevatorHeightWithShare(BackElevator.HEIGHT_HOLD, share));
		
		this.addSequential(new DriveDistanceCommand(13, 1, 0.5));
		this.addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BOTTOM));
	}
	
	protected void end()
	{
		share.isFinished = false;
	}
	
	protected void interupted()
	{
		end();
	}
}
