
package edu.nr.robotics;

import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.commands.DriveIdleCommand;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.nr.robotics.subsystems.frontElevator.commands.FrontElevatorGoToHeightCommand;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot 
{
    Command autonomousCommand;
    PowerDistributionPanel pdp;

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
		
		/*SmartDashboard.putNumber("ElevatorHeightSet", 1);
		SmartDashboard.putData("Front Elevator Height 3", new FrontElevatorGoToHeightCommand(3));
		SmartDashboard.putData("Front Elevator Height 4", new FrontElevatorGoToHeightCommand(4));
        SmartDashboard.putData("Go to smartdashboard height", new EmptyCommand("elevator to smart height")
        {
			@Override
			protected void onExecute() 
			{
				new FrontElevatorGoToHeightCommand(SmartDashboard.getNumber("ElevatorHeightSet")).start();;
			}
			
			protected void onStart(){}
        });*/
		
		
        // instantiate the command used for the autonomous period
        autonomousCommand = new DriveIdleCommand();
    }
	
    public void autonomousInit() 
    {
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
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
    	previousTime = System.currentTimeMillis();
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
    }
    
    long previousTime = 0;
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
    	SmartDashboard.putNumber("dt", System.currentTimeMillis() - previousTime);
    	previousTime = System.currentTimeMillis();
    	//FieldCentric should come first in periodic functions, so the commands run by the scheduler
    	//aren't using stale location data
    	
    	Fieldcentric.getRobotInstance().update();
    	
        Scheduler.getInstance().run();
    	
        //Update SmartDashboard info after the scheduler runs our commands
        //putSubsystemDashInfo();
        
        
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
    }
}
