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
    	
    	addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BOTTOM));
    	addSequential(new ReleaseBinCommand());
    	
    	DriveDistanceCommand pushForward = new DriveDistanceCommand(0.8, 0.25, 0.3);
    	pushForward.setIParams(0.7, 0.01);
   
    	addSequential(pushForward);
    	
    	addSequential(new WaitCommand(0.5));
    	
    	DriveDistanceCommand temp = new DriveDistanceCommand(-2, 1, 0.3);
    	temp.setRoughStopDistance(0.2);
    	addSequential(temp);
    }
}
