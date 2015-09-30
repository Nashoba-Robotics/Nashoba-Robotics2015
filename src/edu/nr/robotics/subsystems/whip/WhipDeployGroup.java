package edu.nr.robotics.subsystems.whip;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class WhipDeployGroup extends CommandGroup
{
	public WhipDeployGroup()
	{
		this.addSequential(new WhipUndeployLockCommand());
		this.addSequential(new WhipDeployCommand());
	}
}
