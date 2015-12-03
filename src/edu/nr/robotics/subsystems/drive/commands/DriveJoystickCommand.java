package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.lib.CMD;
import edu.nr.robotics.OI;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.gyro.AngleGyroCorrection;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveJoystickCommand extends CMD 
{
	private AngleGyroCorrection gyroCorrection;
	private boolean hDriveActivated = false;
	private double oldTurn;
	
    public DriveJoystickCommand() 
    {
        requires(Drive.getInstance());
        gyroCorrection = new AngleGyroCorrection();
    }

    @Override
	protected void onStart()
    {
		oldTurn = OI.getInstance().getArcadeMoveValue();
	}
    
    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void onExecute()
    {
    	if(OI.getInstance().drivingModeChooser.getSelected().equals("arcade"))
    	{
	    	double moveValue = OI.getInstance().getArcadeMoveValue();
	    	double driveMagnitude = moveValue;
			if(OI.getInstance().reverseDriveDirection())
	    		driveMagnitude  *= -1;
	    	
			double turn = OI.getInstance().getArcadeTurnValue()/3;	    	
	    	if(Math.abs(turn) < 0.05)
	    	{
	    		if (Math.abs(driveMagnitude) > .1)
	    		{
	    			turn = gyroCorrection.getTurnValue()/3;
	    		}
	    	}
	    	else
	    	{	    		
	    		gyroCorrection.clearInitialValue();
	    	}
	    	
			Drive.getInstance().setHDrive(OI.getInstance().speedMultiplier*OI.getInstance().getHDriveValue());
	    	turn *= 0.3;
	    	SmartDashboard.putNumber("Drive Magnitude", moveValue);
	    	SmartDashboard.putNumber("Turn", turn);    	
	    	
	    	Drive.getInstance().arcadeDrive(OI.getInstance().speedMultiplier*driveMagnitude, -OI.getInstance().speedMultiplier*turn, oldTurn, true);
	    	
	    	oldTurn = OI.getInstance().speedMultiplier*turn;
    	}
    	else{
    		//Get values of the joysticks
    		double left = OI.getInstance().getTankLeftValue();
        	double right = OI.getInstance().getTankRightValue();
    		
        	//Do the math for turning
        	if(Math.abs(left - right) < .25)
        	{
        		left = (Math.abs(left) + Math.abs(right))/2*Math.signum(left);
        		right = (Math.abs(left) + Math.abs(right))/2*Math.signum(right);
        	}
        	
    		// cube the inputs (while preserving the sign) to increase fine control while permitting full power
            right = right*right*right;
            left = left*left*left;

            SmartDashboard.putNumber("Tank Left Motor", left);
            SmartDashboard.putNumber("Tank Right Motor", right);

    		Drive.getInstance().setHDrive(OI.getInstance().getHDriveValue());
    		Drive.getInstance().tankDrive(OI.getInstance().speedMultiplier*left, OI.getInstance().speedMultiplier*right);

    	}
    }

    //Always return false for a default command
    protected boolean isFinished() 
    {
        return false;
    }

    // Called once after isFinished returns true
    protected void onEnd(boolean interrupted) 
    {
    	Drive.getInstance().arcadeDrive(0, 0);
    	Drive.getInstance().setHDrive(0);
    	gyroCorrection.clearInitialValue();
    }
}
