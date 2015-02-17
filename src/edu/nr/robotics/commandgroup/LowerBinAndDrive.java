package edu.nr.robotics.commandgroup;

import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.nr.robotics.subsystems.backElevator.commands.BackElevatorGoToHeightCommand;
import edu.nr.robotics.subsystems.drive.Drive;

public class LowerBinAndDrive extends BackElevatorGoToHeightCommand
{
	public LowerBinAndDrive()
	{
		super(BackElevator.HEIGHT_BIN_LOWERED);
		this.requires(Drive.getInstance());
	}

	@Override
	protected void onStart() 
	{
		super.onStart();
		this.setMaxSpeed(0.3);
	}

	@Override
	protected void onExecute() 
	{
		super.onExecute();
		Drive.getInstance().arcadeDrive(0.2, 0);
	}

	@Override
	protected boolean isFinished() 
	{
		return super.isFinished();
	}
	
	@Override
	protected void onEnd(boolean interrupted) 
	{
		super.onEnd(interrupted);
		Drive.getInstance().arcadeDrive(0, 0);
	}
}
