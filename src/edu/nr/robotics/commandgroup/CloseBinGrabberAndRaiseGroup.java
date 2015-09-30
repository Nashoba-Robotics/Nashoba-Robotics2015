package edu.nr.robotics.commandgroup;

import edu.nr.robotics.subsystems.binGrabber.CloseBinGrabberCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontArmsCloseCommand;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontElevatorGoToHeightCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class CloseBinGrabberAndRaiseGroup extends CommandGroup
{
	public CloseBinGrabberAndRaiseGroup()
	{
		this.addSequential(new CloseBinGrabberCommand());
		this.addSequential(new WaitCommand(0.2));
		this.addSequential(new FrontArmsCloseCommand());
		this.addSequential(new WaitCommand(.5));
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
		
		//These to are put as a separate set of commands so that the driver can get control of the drivetrain
		//as soon as the DriveDistanceCommand finishes, and while the front elevator is still lifting to save time
		
		FrontElevatorGoToHeightCommand temp2 = new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BEFORE_TOTE_ADJUST);
		temp2.setPI(4, 0.1);
		temp2.start();
		
		DriveDistanceCommand temp = new DriveDistanceCommand(-0.5, 0.25, 0.5);
		temp.setRoughStopDistance(0.1);
		temp.setP(1);
		temp.setIParams(0.5, 0.01);
		temp.start();
	}
}
