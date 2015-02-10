package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ToteOneToScoreGroup extends CommandGroup {
    
    public  ToteOneToScoreGroup() {
    	//Height: Pick up Tote #1, go to score height
    	addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_PICK_UP_TOTE_ONE));
    	addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_SCORING));

    }
}
