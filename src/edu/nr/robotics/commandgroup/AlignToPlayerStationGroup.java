package edu.nr.robotics.commandgroup;

import edu.nr.robotics.subsystems.camera.CameraOffCommand;
import edu.nr.robotics.subsystems.camera.CameraOnCommand;
import edu.nr.robotics.subsystems.drive.commands.AlignAnglePlayerStation;
import edu.nr.robotics.subsystems.drive.commands.AlignHorizontalToPlayerStationCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveTimeCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AlignToPlayerStationGroup extends CommandGroup
{
	public AlignToPlayerStationGroup()
	{
		this.addSequential(new CameraOffCommand());
		
		for(int i = 0; i < 3; i++)
		{
			this.addSequential(new AlignHorizontalToPlayerStationCommand());
			this.addSequential(new AlignAnglePlayerStation());
		}
		this.addSequential(new DriveTimeCommand(0.3, 0.5));
		this.addSequential(new CameraOnCommand());
	}
	
	@Override
	public void interrupted()
	{
		new CameraOnCommand().start();
	}
}
