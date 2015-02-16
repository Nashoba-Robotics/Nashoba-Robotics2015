package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class ScoreGroup extends CommandGroup {
    
    public  ScoreGroup() {
    	/* Score!:
		 * Release bin
		 * Elevator down
		 * Push the stack forward
		 * Go backward
		 */
    	addSequential(new ReleaseBinCommand());
    	addSequential(new WaitCommand(1));
    	addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BOTTOM));
    	
    	DriveDistanceCommand pushForward = new DriveDistanceCommand(0.8, 0.25, 0.3);
    	pushForward.setIParams(0.9, 0.01);
    	
    	addSequential(pushForward);
    	DriveDistanceCommand temp = new DriveDistanceCommand(-2, 1, 0.3);
    	temp.setRoughStopDistance(0.2);
    	addSequential(temp);
    }
}
