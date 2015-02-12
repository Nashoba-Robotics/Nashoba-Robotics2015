package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreGroup extends CommandGroup {
    
    public  ScoreGroup() {
    	/* Score!:
		 * Release bin
		 * Elevator down, once it touches bottom, down at 5%
		 * Go backward
		 */
    	addSequential(new ReleaseBinCommand());
    	addSequential(new FrontElevatorGoToHeightCommand(0));
    	addSequential(new DriveDistanceCommand(-5, 0.5));
    }
}
