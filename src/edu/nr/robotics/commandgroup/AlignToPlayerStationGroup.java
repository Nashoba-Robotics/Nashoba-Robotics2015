package edu.nr.robotics.commandgroup;

import edu.nr.robotics.subsystems.camera.CameraOffCommand;
import edu.nr.robotics.subsystems.camera.CameraOnCommand;
import edu.nr.robotics.subsystems.drive.commands.AlignAnglePlayerStation;
import edu.nr.robotics.subsystems.drive.commands.AlignHorizontalToPlayerStationCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AlignToPlayerStationGroup extends CommandGroup
{
	public AlignToPlayerStationGroup()
	{
		//this.addSequential(new CameraOnCommand());
		
		for(int i = 0; i < 3; i++)
		{
			this.addSequential(new AlignHorizontalToPlayerStationCommand());
			this.addSequential(new AlignAnglePlayerStation());
		}
		
		//this.addSequential(new CameraOffCommand());
	}
	
	@Override
	public void interrupted()
	{
		//new CameraOffCommand().start();
	}
}
