package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ToteTwoToWaitGroup extends CommandGroup {
    
    public  ToteTwoToWaitGroup() {
    	//Height: Big red button: Pick up Tote #2, go to waiting height
    	addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_PICK_UP_TOTE_TWO));
    	addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_WAITING));
    }
}
