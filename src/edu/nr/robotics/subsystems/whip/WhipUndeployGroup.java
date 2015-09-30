package edu.nr.robotics.subsystems.whip;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class WhipUndeployGroup extends CommandGroup
{
	public WhipUndeployGroup()
	{
		this.addSequential(new WhipUndeployLockCommand());
		this.addSequential(new WhipUndeployCommand());
	}
}
