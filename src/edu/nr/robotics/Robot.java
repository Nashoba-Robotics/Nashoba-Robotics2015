
package edu.nr.robotics;

import edu.nr.robotics.auton.AutonRedeemGroup;
import edu.nr.robotics.auton.AutonRedeemGroup.AutonType;
import edu.nr.robotics.commandgroup.ScoreGroup;
import edu.nr.robotics.commandgroup.StartingConfigurationGroup;
import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.nr.robotics.subsystems.backElevator.commands.BackElevatorGoToHeightCommand;
import edu.nr.robotics.subsystems.camera.CameraOffCommand;
import edu.nr.robotics.subsystems.camera.CameraOnCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.commands.AutonDriveToStepShort;
import edu.nr.robotics.subsystems.drive.commands.DriveAngleCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveIdleCommand;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.nr.robotics.subsystems.frontElevator.FrontElevatorStateMachine;
import edu.nr.robotics.subsystems.frontElevator.commands.AdjustRecycleGroup;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontElevatorGoToHeightCommand;
import edu.nr.robotics.subsystems.frontElevator.commands.ToteOneToScoreGroup;
import edu.nr.robotics.subsystems.frontElevator.commands.ToteTwoToWaitGroup;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot 
{
    Command autonomousCommand;
    PowerDistributionPanel pdp;
    SendableChooser autoCommandChooser;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
    	pdp = new PowerDistributionPanel();
    	
		OI.init();
		Drive.init();
		FrontElevator.init();
		BackElevator.init();
		
		//TODO Test these autonomous commands
		autoCommandChooser = new SendableChooser();
		autoCommandChooser.addDefault("Robot Set", new AutonRedeemGroup(AutonType.ShortDistanceRobotSet));
		autoCommandChooser.addObject("Recycle Set", new AutonRedeemGroup(AutonType.ShortDistanceRecycleSet));
		SmartDashboard.putData("Autonomous Chooser", autoCommandChooser);
		
		SmartDashboard.putData(FrontElevator.getInstance());
		SmartDashboard.putData(Drive.getInstance());
		
		SmartDashboard.putNumber("ElevatorHeightSet", 1);
		
        SmartDashboard.putData("Go to smartdashboard height", new EmptyCommand("elevator to smart height")
        {
			@Override
			protected void onExecute() 
			{
				new FrontElevatorGoToHeightCommand(SmartDashboard.getNumber("ElevatorHeightSet")).start();;
			}
			
			protected void onStart(){}
        });
        
        SmartDashboard.putNumber("Smart Angle", 0.262);
        SmartDashboard.putData(new EmptyCommand("Drive to smartdash angle")
        {
			@Override
			protected void onStart() 
			{
			}

			@Override
			protected void onExecute() 
			{
				new DriveAngleCommand(SmartDashboard.getNumber("Smart Angle"), false).start();
			}
        });
        
        SmartDashboard.putNumber("Rear Elevator Height Set", 1);
        SmartDashboard.putData(new EmptyCommand("Rear Elevator to smart height")
        {
			@Override
			protected void onStart() 
			{
			}

			@Override
			protected void onExecute() 
			{
				new BackElevatorGoToHeightCommand(SmartDashboard.getNumber("Rear Elevator Height Set")).start();
			}
        });
        
        SmartDashboard.putData(new BackElevatorGoToHeightCommand(2.02));
        
        for(int i = 0; i < FrontElevator.commandedHeights.length; i++)
        {
        	SmartDashboard.putData(FrontElevator.commandedHeights[i].cmdName, new FrontElevatorGoToHeightCommand(FrontElevator.commandedHeights[i].height));
        }
        
        SmartDashboard.putData("Adjust Recycle Group", new AdjustRecycleGroup());
		
        SmartDashboard.putData("Camera Light On", new CameraOnCommand());
        SmartDashboard.putData("Camera Light Off", new CameraOffCommand());
        SmartDashboard.putData("Starting Configuration", new StartingConfigurationGroup());
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
    	//FieldCentric should come first in periodic functions, so the commands run by the scheduler
    	//aren't using stale location data
    	Fieldcentric.getRobotInstance().update();
    	
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
    	//FieldCentric should come first in periodic functions, so the commands run by the scheduler
    	//aren't using stale location data
    	Fieldcentric.getRobotInstance().update();
    	
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
		//FieldCentric should come first in periodic functions, so the commands run by the scheduler
    	//aren't using stale location data
    	Fieldcentric.getRobotInstance().update();
    	
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
