package edu.nr.robotics;

import edu.nr.robotics.commandgroup.CancelAllCommand;
import edu.nr.robotics.commandgroup.LowerBinGroup;
import edu.nr.robotics.commandgroup.StartingConfigurationGroup;
import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.nr.robotics.subsystems.backElevator.commands.BackElevatorCloseCommand;
import edu.nr.robotics.subsystems.backElevator.commands.BackElevatorGoToHeightCommand;
import edu.nr.robotics.subsystems.drive.commands.*;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.nr.robotics.subsystems.frontElevator.FrontElevatorStateMachine;
import edu.nr.robotics.subsystems.frontElevator.commands.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI 
{	
	public static boolean USING_ARCADE = true;
	public static boolean USING_COFFIN = true;
	
	private static OI singleton;
	
	Joystick stickTankLeft;
	Joystick stickTankRight;
	Joystick coffin2, coffin3;
	
	//joystick for Arcade goes in 0, left joystick for tank goes in 2, right joystick for tank goes in 3

	public static int H_DRIVE_BUTTON = 2;
	
	private OI()
	{
		stickTankLeft = new Joystick(0);
		stickTankRight = new Joystick(1);

		if(USING_COFFIN)
		{
			coffin2 = new Joystick(2);
			coffin3 = new Joystick(3);
			//Front Elevator Buttons
			new JoystickButton(coffin3, 9).whenPressed(new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_HOLD));
			new JoystickButton(coffin3, 8).whenPressed(new LowerBinGroup());
			new JoystickButton(coffin3, 7).whenPressed(new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_OBTAIN_STEP));
			new JoystickButton(coffin3, 6).whenPressed(new BackElevatorCloseCommand());
			
			new JoystickButton(coffin3, 5).whenPressed(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_WAITING));
			new JoystickButton(coffin3, 4).whenPressed(new AdjustRecycleGroup());
			new JoystickButton(coffin3, 10).whenPressed(new ToteTwoToWaitGroup());
			new JoystickButton(coffin3, 2).whenPressed(new ToteOneToScoreGroup());
			new JoystickButton(coffin3, 1).whenPressed(new ScoreGroup());
			new JoystickButton(coffin2, 8).whenPressed(new PickupBarrelAndRaiseGroup());
			
			new JoystickButton(coffin2, 5).whenPressed(new ToggleBinCommand());
			new JoystickButton(coffin2, 6).whenPressed(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_ADJUST_TOTE_ONE));
			new JoystickButton(coffin2, 7).whenPressed(new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_BIN_LOWERED));
			
			new JoystickButton(coffin3, 1).whenPressed(new CancelAllCommand());
			new JoystickButton(coffin2, 4).whenPressed(new EmptyCommand("Next")
			{
				@Override
				protected void onStart() {
				}
				@Override
				protected void onExecute() 
				{
					FrontElevatorStateMachine.getNextCommand().start();
				}
				
			});
			
			new JoystickButton(coffin2, 3).whenPressed(new EmptyCommand("Redo Step")
	        {
				@Override
				protected void onStart() 
				{
					
				}

				@Override
				protected void onExecute() 
				{
					FrontElevatorStateMachine.redoLastCommand().start();
				}
	        });
	        
			new JoystickButton(coffin2, 2).whenPressed(new EmptyCommand("Reset")
	        {
				@Override
				protected void onStart() {
				}

				@Override
				protected void onExecute() 
				{
					FrontElevatorStateMachine.reset();
				}
	        });
			
		}
		else
		{
			new JoystickButton(stickTankRight, 10).whenPressed(new StartingConfigurationGroup());
		}
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
			return -stickTankLeft.getY();
		}
		
		return 0;
	}
	
	public double getArcadeTurnValue()
	{
		if(USING_ARCADE)
		{
			return -stickTankRight.getX();
		}
		
		return 0;
	}
	
	public double getHDriveValue()
	{
		if(USING_COFFIN)
		{
			return snap(-coffin2.getRawAxis(0));
		}
		
		return 0;
	}
	
	public double getFrontElevatorManual()
	{
		if(USING_COFFIN)
		{
			return snap(-coffin3.getRawAxis(1));
		}
		
		return 0;
	}
	
	public double getRearElevatorManual()
	{
		if(USING_COFFIN)
		{
			return snap(-(coffin3.getRawAxis(0)-.1));
		}
		return 0;
	}
	
	private double snap(double value)
	{
		if(value > -0.1 && value < 0.1)
			return 0;
		
		return (value-0.1) / 0.9;
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
			return stickTankRight.getRawButton(1)?2:1;
		}
		
		return 1;
	}
		
	public double getDecreaseValue()
	{
		return 1;
	}
	
	/**
	 * @return true if the DriveJoystickCommand should ignore joystick Z value and use the gyro to drive straight instead.
	 */
	public boolean useGyroCorrection()
	{
		if(USING_ARCADE)
		{
			return stickTankRight.getRawButton(H_DRIVE_BUTTON);
		}
		return false;
	}
}

