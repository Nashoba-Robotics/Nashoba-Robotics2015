package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.binGrabber.CloseBinGrabberCommand;
import edu.nr.robotics.subsystems.binGrabber.OpenBinGrabberCommand;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class FirstToteTwoGroup extends CommandGroup {
    
    public  FirstToteTwoGroup() {
    	//Height: Pick up Tote #2, release bin, grab bin, go to waiting height
    	addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_PICK_UP_TOTE_TWO));
    	addSequential(new CloseBinGrabberCommand());
    	addSequential(new OpenBinGrabberCommand());
    	addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_WAITING));
    }
}
