package edu.nr.robotics.commandgroup;

import edu.nr.robotics.EmptyCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.nr.robotics.subsystems.drive.gyro.ConstantAngleGyroCorrection;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveAndGetStationAngle extends CommandGroup
{
	public DriveAndGetStationAngle()
	{
		this.addSequential(new EmptyCommand("Zero Player Station Angle")
		{
			@Override
			protected void onStart() {
				ConstantAngleGyroCorrection.setAlignmentAngle(Drive.getInstance().getAngleDegrees());
			}

			@Override
			protected void onExecute() {
				
			}
			
		});
		
		DriveDistanceCommand driveWithTote = new DriveDistanceCommand(4, 3, 0.25);
		driveWithTote.setRoughStopDistance(.5);
		this.addSequential(driveWithTote);
	}
}
