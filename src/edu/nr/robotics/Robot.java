
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

    public enum Mode {
    	TELEOP, AUTONOMOUS, DISABLED
    }
    
    public Mode currentMode;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {    	
		OI.init();
		Drive.init();
		FrontElevator.init();
		Whip.init();
		BinGrabber.init();
		
		smartDashboardInit();
    }
    
    public void smartDashboardInit() {
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
		
    	SmartDashboard.putNumber("Auton Whip Wait Time", 1);
    }
	
    public void autonomousInit() 
    {
    	currentMode = Mode.AUTONOMOUS;
    	// instantiate the command used for the autonomous period
        autonomousCommand =(Command) autoCommandChooser.getSelected();
        autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() 
    {
    	periodic(Mode.AUTONOMOUS);
    }

    public void teleopInit() 
    {
    	currentMode = Mode.TELEOP;
		// This makes sure that the autonomous stops running when
        // teleop starts running.
        if (autonomousCommand != null) autonomousCommand.cancel();
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
    	periodic(Mode.TELEOP);
        OI.getInstance().speedMultiplier = SmartDashboard.getNumber("Speed Multiplier");
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit()
    {
    	currentMode = Mode.DISABLED;
    }
    
    public void disabledPeriodic() 
	{
    	periodic(Mode.DISABLED);
		
	}

    private void periodic(Mode mode)
    {
    	FieldCentric.getInstance().update();
		Scheduler.getInstance().run();

		updateArduino(mode);
		
        putSubsystemDashInfo();
    }
    
    private void updateArduino(Mode mode)
    {
    	switch(mode) {
		case TELEOP:
	        ArduinoLink.getInstance().updateTeleop();
	        break;
		case DISABLED: 
			ArduinoLink.getInstance().updateDisabled();
			break;
		case AUTONOMOUS:
	        ArduinoLink.getInstance().updateAuton();
			break;
		default:
			break;
		}
    }
    
    private void putSubsystemDashInfo()
    {
    	Drive.getInstance().putSmartDashboardInfo();
    	FrontElevator.getInstance().putSmartDashboardInfo();
    	FieldCentric.getInstance().putSmartDashboardInfo();
    }
}
