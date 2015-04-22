package edu.nr.robotics.subsystems.frontElevator.commands;

import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class ToteTwoToWaitWithOpeningArmsGroup extends CommandGroup 
{
    //TODO Test this command
    public  ToteTwoToWaitWithOpeningArmsGroup() 
    {
    	//Height: Big red button: Pick up Tote #2, go to waiting height
    	
    	this.addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_ADJUST_BIN));
    	this.addSequential(new FrontArmsOpenCommand());
    	this.addSequential(new WaitCommand(0.25));
    	
    	FrontElevatorGoToHeightCommand tempDown = new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_TOTE_TWO_ADJUST);
    	tempDown.setGoingDownMaxRange(1);
    	tempDown.setTalonRamp(true);
    	tempDown.setEpsilon(1d/12);
    	addSequential(tempDown);
    	this.addSequential(new FrontArmsCloseCommand());
    	
    	tempDown = new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_PICK_UP_TOTE_TWO);
    	tempDown.setGoingDownMaxRange(1);
    	tempDown.setTalonRamp(true);
    	tempDown.setEpsilon(1d/12);
    	addSequential(tempDown);
    	
    	this.addSequential(new WaitCommand(0.5));
    	
    	addSequential(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_WAITING));
    }
}
