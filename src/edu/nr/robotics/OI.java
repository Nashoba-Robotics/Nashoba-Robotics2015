package edu.nr.robotics;

import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.nr.robotics.subsystems.backElevator.BackElevatorGoToHeightCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveDistanceCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveJoystickArcadeCommand;
import edu.nr.robotics.subsystems.drive.commands.DrivePositionCommand;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.nr.robotics.subsystems.frontElevator.FrontElevatorGoToHeightCommand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI 
{	
	public static boolean USING_ARCADE = true;
	public static boolean USING_SPLIT_ARCADE = false;
	public static boolean USING_COFFIN = false;
	
	private static OI singleton;
	
	Joystick stickTankLeft;
	Joystick stickTankRight;
	Joystick stickArcade;
	Joystick coffin;
	//xBox goes in 0, joystick for Arcade goes in 1, left joystick for tank goes in 2, right joystick for tank goes in 3
	
	private OI()
	{
		if(USING_ARCADE && !USING_SPLIT_ARCADE)
		{
			stickArcade = new Joystick(0);
		}
		else
		{
			stickTankLeft = new Joystick(2);
			stickTankRight = new Joystick(3);
		}

		Joystick buttonAssignmentStick;
		if(USING_ARCADE)
		{
			if(USING_SPLIT_ARCADE)
				buttonAssignmentStick = stickTankRight;
			else
				buttonAssignmentStick = stickArcade;
		}
		else
		{
			buttonAssignmentStick = stickTankLeft;
		}
		
		if(USING_COFFIN)
		{
			coffin = new Joystick(1);
			//Front Elevator Buttons
			new JoystickButton(buttonAssignmentStick, 1).whenPressed(new EmptyCommand()//Height: Adjust Tote #1
			{
				@Override
				public void execute()
				{
					new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_ADJUST_TOTE_ONE).start();
				}
			});
			//INCOMPLETE:
			new JoystickButton(buttonAssignmentStick, 2).whenPressed(new EmptyCommand()//Height: Pick up Tote #2, release bin, grab bin, go to waiting height
			{
				@Override
				public void execute()
				{
					new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_PICK_UP_TOTE_TWO).start();
				}
			});
			//INCOMPLETE:
			new JoystickButton(buttonAssignmentStick, 3).whenPressed(new EmptyCommand()//Height: Big red button: Pick up Tote #2, go to waiting height
			{
				@Override
				public void execute()
				{
					new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_PICK_UP_TOTE_TWO).start();
				}
			});
			//INCOMPLETE:
			new JoystickButton(buttonAssignmentStick, 4).whenPressed(new EmptyCommand()//Height: Pick up Tote #2, go to score height
			{
				@Override
				public void execute()
				{
					new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_PICK_UP_TOTE_TWO).start();
				}
			});
			//INCOMPLETE:
			new JoystickButton(buttonAssignmentStick, 5).whenPressed(new EmptyCommand()//Height: Pick up Tote #1, go to score height
			{
				@Override
				public void execute()
				{
					new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_PICK_UP_TOTE_TWO).start();
				}
			});
			//INCOMPLETE:
			new JoystickButton(buttonAssignmentStick, 6).whenPressed(new EmptyCommand()
			/* Score!:
			 * Release bin
			 * Elevator down, once it touches bottom, down at 5%
			 * Go backward
			 */
			{
				@Override
				public void execute()
				{
					new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_PICK_UP_TOTE_TWO).start();
				}
			});	
			//INCOMPLETE:
			new JoystickButton(buttonAssignmentStick, 7).whenPressed(new EmptyCommand()//Toggle top gripper
			{
				@Override
				public void execute()
				{
					new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_PICK_UP_TOTE_TWO).start();
				}
			});
			new JoystickButton(buttonAssignmentStick, 8).whenPressed(new EmptyCommand()//Height: Elevator all the way down
			{
				@Override
				public void execute()
				{
					new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_MIN).start();
				}
			});

		    //Back Elevator Buttons
			new JoystickButton(buttonAssignmentStick, 9).whenPressed(new EmptyCommand()//Height: Hold
			{
				@Override
				public void execute()
				{
					new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_HOLD).start();
				}
			});
			new JoystickButton(buttonAssignmentStick, 10).whenPressed(new EmptyCommand()//Height: Obtain off step
			{
				@Override
				public void execute()
				{
					new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_OBTAIN_STEP).start();
				}
			});
			new JoystickButton(buttonAssignmentStick, 11).whenPressed(new EmptyCommand()//Height: Obtain off floor / Put bin down
			{
				@Override
				public void execute()
				{
					new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_OBTAIN_FLOOR).start();
				}
			});
			new JoystickButton(buttonAssignmentStick, 12).whenPressed(new EmptyCommand()//Height: Closed
			{
				@Override
				public void execute()
				{
					new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_CLOSED).start();
				}
			});
		}
		
		
		/* Update this whenever a button is used. Don't use one of these buttons.
		 * Used buttons: 10,12, 1
		 */
		new JoystickButton(buttonAssignmentStick, 3).whenPressed(new EmptyCommand()
		{
			@Override
			public void execute()
			{
				new DrivePositionCommand(true).start();
			}
		});
		new JoystickButton(buttonAssignmentStick, 4).whenPressed(new EmptyCommand()
		{
			@Override
			public void execute()
			{
				new DriveJoystickArcadeCommand().start();
			}
		});
		new JoystickButton(buttonAssignmentStick, 12).whenPressed(new DriveDistanceCommand(-14.5/12d, 0.5));
	}
	
	public static OI getInstance()
	{
		init();
		return singleton;
	}
	
	public static void init()
	{
		if(singleton == null)
            singleton = new OI();
	}
	
	public double getArcadeMoveValue()
	{
		if(USING_ARCADE)
		{
			if(USING_SPLIT_ARCADE)
				return -stickTankLeft.getY();
			else
				return -stickArcade.getY();
		}
		else
		{
			return 0;
		}
	}
	
	public double getArcadeTurnValue()
	{
		if(USING_ARCADE)
		{
			if(USING_SPLIT_ARCADE)
				return -stickTankRight.getX();
			else
				return -stickArcade.getZ();
		}
		else
		{
			return 0;
		}
	}

	public double getTankLeftValue()
	{
		if(USING_ARCADE)
			return 0;
		
		return -stickTankLeft.getY();
	}

	public double getTankRightValue()
	{
		if(USING_ARCADE)
			return 0;
		
		return stickTankRight.getY();
	}
	
	public double getAmplifyMultiplyer()
	{
		if(USING_ARCADE)
		{
			if(USING_SPLIT_ARCADE)
				return stickTankRight.getRawButton(1)?2:1;
			else
				return stickArcade.getRawButton(1)?2:1;
		}
		
		return 1;
	}
		
	public double getDecreaseValue()
	{
		return 1;
	}
	
	public double getFrontElevatorJoy()
	{
		if(USING_COFFIN)
		{
			return coffin.getRawAxis(1);//THIS NEEDS TO BE SET
		}
		return 0;
	}
	
	public double getBackElevatorJoy()
	{
		if(USING_COFFIN)
		{
			return coffin.getRawAxis(1);//THIS NEEDS TO BE SET
		}
		return 0;
	}

	
	/**
	 * @return true if the DriveJoystickCommand should ignore joystick Z value and use the gyro to drive straight instead.
	 */
	public boolean useGyroCorrection()
	{
		if(USING_ARCADE)
		{
			if(USING_SPLIT_ARCADE)
				return stickTankRight.getRawButton(2);
			else
				return stickArcade.getRawButton(2);
		}
		return false;
	}
}

