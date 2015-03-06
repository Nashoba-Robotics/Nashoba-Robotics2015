package edu.nr.robotics.commandgroup;

import edu.nr.robotics.subsystems.camera.CameraOffCommand;
import edu.nr.robotics.subsystems.camera.CameraOnCommand;
import edu.nr.robotics.subsystems.drive.commands.AlignToPlayerStationCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AlignToPlayerStationGroup extends CommandGroup
{
	public AlignToPlayerStationGroup()
	{
		this.addSequential(new CameraOnCommand());
		this.addSequential(new AlignToPlayerStationCommand());
		this.addSequential(new CameraOffCommand());
	}
	
	@Override
	public void interrupted()
	{
		new CameraOffCommand().start();
	}
}
