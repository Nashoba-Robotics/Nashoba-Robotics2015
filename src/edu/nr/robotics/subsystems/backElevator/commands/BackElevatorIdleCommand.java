package edu.nr.robotics.subsystems.backElevator.commands;

import edu.nr.robotics.OI;
import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;

/**
 *
 */
public class BackElevatorIdleCommand extends CMD 
{
	public BackElevatorIdleCommand() 
	{
        requires(BackElevator.getInstance());
    }

    protected void onStart() {
    }

    protected void onExecute() 
    {
    	BackElevator.getInstance().setElevatorSpeed(OI.getInstance().getRearElevatorManual());
    }

    protected boolean isFinished() 
    {
        return false;
    }

    protected void onEnd(boolean interrupted) 
    {
    	BackElevator.getInstance().setElevatorSpeed(0);
    }
}
