package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class PickupBarrelAndRaiseGroup extends CommandGroup
{
	public PickupBarrelAndRaiseGroup()
	{
		this.addSequential(new GrabBinCommand());
		
		DriveDistanceCommand temp = new DriveDistanceCommand(-0.5, 0.25, 0.5);
		temp.setRoughStopDistance(0.1);
		temp.setIParams(0.5, 0.01);
		this.addSequential(temp);
		
		FrontElevatorGoToHeightCommand temp2 = new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_OBTAIN_NOODLE);
		temp2.setPI(4, 0.1);
		
		this.addSequential(temp2);
	}
	
	@Override
	public void interrupted()
	{
		System.out.println("Pickup Barrel Command intrrupted");
	}
	
	@Override
	public void end()
	{
		System.out.println("Command ended peacefully");
	}
}
