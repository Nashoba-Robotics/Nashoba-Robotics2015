
package edu.nr.robotics;

import edu.nr.robotics.auton.AutonCloseAndDrive;
import edu.nr.robotics.auton.AutonCloseAndLift;
import edu.nr.robotics.auton.AutonDoNothingCommand;
import edu.nr.robotics.auton.AutonRedeemGroup;
import edu.nr.robotics.auton.AutonRedeemGroup.AutonType;
import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.nr.robotics.subsystems.camera.CameraOffCommand;
import edu.nr.robotics.subsystems.camera.CameraOnCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.commands.AlignAnglePlayerStation;
import edu.nr.robotics.subsystems.drive.commands.AlignHorizontalToPlayerStationCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveAngleCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveToPlayerStationDistance;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontElevatorGoToHeightCommand;
import edu.nr.robotics.subsystems.frontfingers.FrontFingers;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot 
{
    Command autonomousCommand;
    SendableChooser autoCommandChooser;
    PowerDistributionPanel pdp;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
		OI.init();
		Drive.init();
		FrontElevator.init();
		BackElevator.init();
		FrontFingers.init();
		
		//TODO Test these autonomous commands
		autoCommandChooser = new SendableChooser();
		autoCommandChooser.addDefault("Recycle Set", new AutonRedeemGroup(AutonType.ShortDistanceRecycleSet));
		autoCommandChooser.addObject("Robot Set", new AutonRedeemGroup(AutonType.ShortDistanceRobotSet));
		//autoCommandChooser.addObject("Redeem Left", new AutonRedeemGroup(AutonType.ShortDistanceDriveLeft));
		//autoCommandChooser.addObject("Redeem Right", new AutonRedeemGroup(AutonType.ShortDistanceDriveRight));
		autoCommandChooser.addObject("Pickup Bin And Lift", new AutonCloseAndLift());
		autoCommandChooser.addObject("Redeem + Lower Bins and Close", new AutonRedeemGroup(AutonType.ShortDistancePutBinsDown));
		autoCommandChooser.addObject("Do Nothing", new AutonDoNothingCommand());
		autoCommandChooser.addObject("Pickup + Drive to Auto Zone", new AutonCloseAndDrive());
		autoCommandChooser.addObject("Redeem + Drive Short", new AutonRedeemGroup(AutonType.ShortDistanceDriveShort));
		SmartDashboard.putData("Autonomous Chooser", autoCommandChooser);
		
		SmartDashboard.putData(FrontElevator.getInstance());
		SmartDashboard.putData(Drive.getInstance());
		SmartDashboard.putData(BackElevator.getInstance());
		
		SmartDashboard.putData("Front Elevator Starting Height", new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_STARTING_CONFIGURATION));
		
		SmartDashboard.putNumber("Angle Chooser", Math.PI/4);
		SmartDashboard.putData("", new EmptyCommand("")
		{
			@Override
			protected void onStart() {
				new DriveAngleCommand(SmartDashboard.getNumber("Angle Chooser"), false).start();
			}

			@Override
			protected void onExecute() {
				
			}
			
		});
		
		SmartDashboard.putData("Align Camera Angle", new AlignAnglePlayerStation(false));
		SmartDashboard.putData("Align Camera Center", new AlignHorizontalToPlayerStationCommand(false));
		
		SmartDashboard.putData(new CameraOnCommand());
		SmartDashboard.putData(new CameraOffCommand());
		SmartDashboard.putData(new DriveToPlayerStationDistance(3, true));
		
		DriveAngleCommand cmd = new DriveAngleCommand(Math.PI, false);
		SmartDashboard.putData("Rotate 180", cmd);
		
		DriveAngleCommand cmd2 = new DriveAngleCommand(-Math.PI, false);
		SmartDashboard.putData("Rotate -180", cmd2);
		
		pdp = new PowerDistributionPanel();
		
		/* Angle Testing
        SmartDashboard.putNumber("Smart Angle", 1.57);
        SmartDashboard.putData(new EmptyCommand("Drive to smartdash angle")
        {
			@Override
			protected void onStart() 
			{
			}
			@Override
			protected void onExecute() 
			{
				new DriveAngleCommandNew(SmartDashboard.getNumber("Smart Angle")).start();
			}
        });*/
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
        Scheduler.getInstance().run();
        
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
        Scheduler.getInstance().run();
    	
        //Update SmartDashboard info after the scheduler runs our commands
        putSubsystemDashInfo();
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
		Scheduler.getInstance().run();
		
		//Update SmartDashboard info after the scheduler runs our commands
        putSubsystemDashInfo();
	}

    private void putSubsystemDashInfo()
    {
    	Drive.getInstance().putSmartDashboardInfo();
    	FrontElevator.getInstance().putSmartDashboardInfo();
    	BackElevator.getInstance().dashboardInfo();
    }
}
