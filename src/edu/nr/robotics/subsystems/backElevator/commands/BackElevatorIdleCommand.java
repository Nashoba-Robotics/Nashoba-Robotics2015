package edu.nr.robotics.subsystems.backElevator.commands;

import edu.nr.robotics.OI;
import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;

/**
 *
 */
public class BackElevatorIdleCommand extends CMD {

    private static final double JOYSTICK_ADJUSTMENT_VALUE = 0.2;

	public BackElevatorIdleCommand() {
        requires(BackElevator.getInstance());
    }

    // Called just before this Command runs the first time
    protected void onStart() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void onExecute() 
    {
    	BackElevator.getInstance().setElevatorSpeed(OI.getInstance().getRearElevatorManual());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void onEnd(boolean interrupted) 
    {
    	BackElevator.getInstance().setElevatorSpeed(0);
    }
}
