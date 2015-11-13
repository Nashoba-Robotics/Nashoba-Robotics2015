
package edu.nr.robotics;

import edu.nr.robotics.auton.AutonGrabAndDrive;
import edu.nr.robotics.auton.AutonGrabAndLift;
import edu.nr.robotics.auton.AutonDoNothingCommand;
import edu.nr.robotics.auton.AutonWhipAndDrive;
import edu.nr.robotics.subsystems.binGrabber.BinGrabber;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontElevatorGoToHeightCommand;
import edu.nr.robotics.subsystems.whip.Whip;
import edu.nr.robotics.subsystems.whip.WhipDeployCommand;
import edu.nr.robotics.subsystems.whip.WhipDeployLockCommand;
import edu.nr.robotics.subsystems.whip.WhipUndeployCommand;
import edu.nr.robotics.subsystems.whip.WhipUndeployLockCommand;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot 
{
    Command autonomousCommand;
    SendableChooser autoCommandChooser;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
    	
    	SmartDashboard.putNumber("Auton Whip Wait Time", 1);
    	
		OI.init();
		Drive.init();
		FrontElevator.init();
		Whip.init();
		BinGrabber.init();
		
		autoCommandChooser = new SendableChooser();
		autoCommandChooser.addDefault("Do Nothing", new AutonDoNothingCommand());
		autoCommandChooser.addObject("Whip + Drive Forward", new AutonWhipAndDrive());
		autoCommandChooser.addObject("Grab Bin + Lift", new AutonGrabAndLift());
		autoCommandChooser.addObject("Grab + Drive to Auto Zone", new AutonGrabAndDrive());
		SmartDashboard.putData("Autonomous Chooser", autoCommandChooser);
		
		OI.getInstance().drivingModeChooser = new SendableChooser();
		OI.getInstance().drivingModeChooser.addDefault("arcade", "arcade");
		OI.getInstance().drivingModeChooser.addObject("tank", "tank");
		SmartDashboard.putData("Driving Mode Chooser", OI.getInstance().drivingModeChooser);
				
		SmartDashboard.putNumber("Speed Multiplier", OI.getInstance().speedMultiplier);
		
		SmartDashboard.putData(FrontElevator.getInstance());
		SmartDashboard.putData(Drive.getInstance());
		SmartDashboard.putData(Whip.getInstance());
		SmartDashboard.putData(BinGrabber.getInstance());
		
		SmartDashboard.putData("Front Elevator Starting Height", new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_STARTING_CONFIGURATION));
		
		SmartDashboard.putData("Deploy Locks", new WhipDeployLockCommand());
		SmartDashboard.putData("Undeploy Lock", new WhipUndeployLockCommand());
		SmartDashboard.putData("Deploy Whip Piston", new WhipDeployCommand());
		SmartDashboard.putData("Undeploy Whip Piston", new WhipUndeployCommand());
    }
	
    public void autonomousInit() 
    {
    	// instantiate the command used for the autonomous period
        autonomousCommand =(Command) autoCommandChooser.getSelected();
        autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() 
    {
    	FieldCentric.getInstance().update();
        Scheduler.getInstance().run();
        ArduinoLink.getInstance().updateAuton();
        
        //Update SmartDashboard info after the scheduler runs our command(s)
        putSubsystemDashInfo();
    }

    public void teleopInit() 
    {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
    	FieldCentric.getInstance().update();
        Scheduler.getInstance().run();
        ArduinoLink.getInstance().updateTeleop();
    	
        //Update SmartDashboard info after the scheduler runs our commands
        putSubsystemDashInfo();
        OI.getInstance().speedMultiplier = SmartDashboard.getNumber("Speed Multiplier");
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit()
    {

    }
    
    public void disabledPeriodic() 
	{
    	FieldCentric.getInstance().update();
		Scheduler.getInstance().run();
		ArduinoLink.getInstance().updateDisabled();
		
		//Update SmartDashboard info after the scheduler runs our commands
        putSubsystemDashInfo();
	}

    private void putSubsystemDashInfo()
    {
    	Drive.getInstance().putSmartDashboardInfo();
    	FrontElevator.getInstance().putSmartDashboardInfo();
    }
}
