package edu.nr.robotics.commandgroup;

import edu.nr.robotics.EmptyCommand;
import edu.nr.robotics.subsystems.camera.CameraOffCommand;
import edu.nr.robotics.subsystems.camera.CameraOnCommand;
import edu.nr.robotics.subsystems.camera.CameraStrobeCommand;
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
			AlignHorizontalToPlayerStationCommand cmd = new AlignHorizontalToPlayerStationCommand();
			if(i == 0)
				cmd.setEpsilon(10);
			this.addSequential(cmd);
			this.addSequential(new AlignAnglePlayerStation());
		}
		this.addSequential(new DriveTimeCommand(0.3, 0.5));
		this.addSequential(new EmptyCommand("Camera Strobe")
		{
			@Override
			protected void onStart()
			{
				new CameraStrobeCommand(1000).start();
			}
			@Override
			protected void onExecute()
			{
			}
		});
	}
	
	@Override
	public void interrupted()
	{
		new CameraOnCommand().start();
	}
}
