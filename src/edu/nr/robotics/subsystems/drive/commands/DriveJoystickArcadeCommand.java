package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.OI;
import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.gyro.AngleGyroCorrection;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveJoystickArcadeCommand extends CMD 
{
	private AngleGyroCorrection gyroCorrection;
	private boolean hDriveActivated = false;
	
    public DriveJoystickArcadeCommand() 
    {
        requires(Drive.getInstance());
        gyroCorrection = new AngleGyroCorrection();
    }

    @Override
	protected void onStart()
    {
		
	}
    
    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void onExecute()
    {
    	double moveValue = OI.getInstance().getArcadeMoveValue();
    	double driveMagnitude = Math.pow(moveValue, 2) * Math.signum(moveValue);
    	
    	//TODO Test direction reversing
    	if(OI.getInstance().reverseDriveDirection())
    		driveMagnitude *= -1;
    	
    	double turn;
    	
    	if(OI.getInstance().useGyroCorrection())
    	{
    		hDriveActivated = true;
    		turn = gyroCorrection.getTurnValue();
    	}
    	else
    	{
    		//Use the joystick to get turn value
    		double rawTurn = OI.getInstance().getArcadeTurnValue();
    		
    		//TODO Test new H-Drive controls
    		//Wait until joystick returns to rest before switching controls to turning
    		if(rawTurn == 0)
    			hDriveActivated = false;
    		
    		if(hDriveActivated)
    			rawTurn = 0;
    		
    		//Scale down the turn value intensity and square it
    		turn = Math.pow(rawTurn*0.9, 2) * Math.signum(rawTurn);
    		
    		gyroCorrection.clearInitialValue();
    	}
    	
		Drive.getInstance().setHDrive(OI.getInstance().getHDriveValue());
    	
    	SmartDashboard.putNumber("Drive Magnitude", driveMagnitude);
    	SmartDashboard.putNumber("Turn", turn);    	

    	Drive.getInstance().arcadeDrive(driveMagnitude, turn, false);
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
