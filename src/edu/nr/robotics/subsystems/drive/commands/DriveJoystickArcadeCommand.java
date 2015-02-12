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
	AngleGyroCorrection gyroCorrection;
	
    public DriveJoystickArcadeCommand() 
    {
        requires(Drive.getInstance());
        gyroCorrection = new AngleGyroCorrection();
    }

    @Override
	protected void onStart()
    {
		
	}

    private final double deadZone = 0.1;
    
    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void onExecute()
    {
    	double rawMoveValue = OI.getInstance().getArcadeMoveValue();
    	if(rawMoveValue < deadZone && rawMoveValue > -deadZone)
    	{
    		rawMoveValue = 0;
    	}
    	else if(rawMoveValue > 0)
    	{
    		rawMoveValue -= deadZone;
    		rawMoveValue *= (1 / (1 - deadZone));
    	}
    	else
    	{
    		rawMoveValue += deadZone;
    		rawMoveValue *= (1 / (1 - deadZone));
    	}
    	
    	double driveMagnitude = rawMoveValue/2 * OI.getInstance().getAmplifyMultiplyer();
    	double turn;
    	
    	if(OI.getInstance().useGyroCorrection())
    	{
    		turn = gyroCorrection.getTurnValue();
    	}
    	else
    	{
    		//Use the joystick to get turn value
    		double rawTurn = OI.getInstance().getArcadeTurnValue();
    		if(rawTurn < deadZone && rawTurn > -deadZone)
        	{
    			rawTurn = 0;
        	}
        	else if(rawTurn > 0)
        	{
        		rawTurn -= deadZone;
        		rawTurn *= (1 / (1 - deadZone));
        	}
        	else
        	{
        		rawTurn += deadZone;
        		rawTurn *= (1 / (1 - deadZone));
        	}
    		turn = rawTurn /2 * OI.getInstance().getAmplifyMultiplyer();
    		
    		gyroCorrection.clearInitialValue();
    	}
    	
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
    	gyroCorrection.clearInitialValue();
    }
}
