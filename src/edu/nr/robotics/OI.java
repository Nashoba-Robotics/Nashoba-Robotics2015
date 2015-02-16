package edu.nr.robotics;

import edu.nr.robotics.commandgroup.StartingConfigurationGroup;
import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.nr.robotics.subsystems.backElevator.commands.BackElevatorGoToHeightCommand;
import edu.nr.robotics.subsystems.drive.commands.*;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
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
	public static boolean USING_COFFIN = false;
	
	private static OI singleton;
	
	Joystick stickTankLeft;
	Joystick stickTankRight;
	Joystick coffin;
	//joystick for Arcade goes in 0, left joystick for tank goes in 2, right joystick for tank goes in 3

	public static int H_DRIVE_BUTTON = 2;
	
	private OI()
	{
		stickTankLeft = new Joystick(0);
		stickTankRight = new Joystick(1);

		if(USING_COFFIN)
		{
			coffin = new Joystick(1);
			//Front Elevator Buttons
			new JoystickButton(coffin, 1).whenPressed(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_ADJUST_TOTE_ONE));
			new JoystickButton(coffin, 2).whenPressed(new FirstToteTwoGroup());
			new JoystickButton(coffin, 3).whenPressed(new ToteTwoToWaitGroup());
			new JoystickButton(coffin, 4).whenPressed(new ToteTwoToScoreGroup());
			new JoystickButton(coffin, 5).whenPressed(new ToteOneToScoreGroup());
			new JoystickButton(coffin, 6).whenPressed(new ScoreGroup());	
			new JoystickButton(coffin, 7).whenPressed(new ToggleBinCommand());
			//TODO new JoystickButton(coffin, 8).whenPressed(new FrontElevatorGoToHeightCommand());

		    //Back Elevator Buttons
			new JoystickButton(coffin, 9).whenPressed(new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_HOLD));
			new JoystickButton(coffin, 10).whenPressed(new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_OBTAIN_STEP));
			new JoystickButton(coffin, 11).whenPressed(new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_OBTAIN_FLOOR));
			new JoystickButton(coffin, 12).whenPressed(new BackElevatorGoToHeightCommand(BackElevator.HEIGHT_CLOSED));
		}
		else
		{
			new JoystickButton(stickTankLeft, 3).whenPressed(new ToggleBinCommand());
			new JoystickButton(stickTankLeft, 5).whenPressed(new GrabBinCommand());
			new JoystickButton(stickTankLeft, 4).whenPressed(new ReleaseBinCommand());
			new JoystickButton(stickTankRight, 5).whenPressed(new PickupBarrelAndRaiseGroup());
			new JoystickButton(stickTankRight, 10).whenPressed(new StartingConfigurationGroup());
		}
		
		//new JoystickButton(buttonAssignmentStick, 3).whenPressed(new DrivePositionCommand(true));
		//new JoystickButton(buttonAssignmentStick, 4).whenPressed(new DriveJoystickArcadeCommand());
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
		else
		{
			return 0;
		}
	}
	
	public double getArcadeTurnValue()
	{
		if(useFrontElevatorManual() || useRearElevatorManual())
			return 0;
		
		if(USING_ARCADE)
		{
			//Right stick controls the H drive if this button is held
			if(!stickTankRight.getRawButton(H_DRIVE_BUTTON))
				return -stickTankRight.getX();
		}
		
		return 0;
	}
	
	//TODO make this compatible with the coffin 'dinky' joystick (so a tank drive system can go sideways as well)
	public double getHDriveValue()
	{
		if(USING_ARCADE)
		{
			if(stickTankRight.getRawButton(H_DRIVE_BUTTON))
				return -stickTankRight.getX();
		}
		
		return 0;
	}
	
	public double getFrontElevatorManual()
	{
		if(USING_ARCADE)
		{
			if(stickTankRight.getRawButton(3))
				return -stickTankRight.getY();
		}
		
		return 0;
	}
	
	public boolean useFrontElevatorManual()
	{
		return stickTankRight.getRawButton(3);
	}
	
	public double getRearElevatorManual()
	{
		if(USING_ARCADE)
		{
			if(stickTankRight.getRawButton(4))
				return -stickTankRight.getY();
		}
		return 0;
	}
	
	public boolean useRearElevatorManual()
	{
		return stickTankRight.getRawButton(4);
	}

	public boolean useHDrive()
	{
		if(USING_ARCADE)
		{
			return stickTankRight.getRawButton(H_DRIVE_BUTTON);
		}
		return false;
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
	
	public double getFrontElevatorJoy()
	{
		if(USING_COFFIN)
		{
			return coffin.getRawAxis(1);//TODO THIS NEEDS TO BE SET
		}
		return 0;
	}
	
	public double getBackElevatorJoy()
	{
		if(USING_COFFIN)
		{
			return coffin.getRawAxis(1);//TODO THIS NEEDS TO BE SET
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
			return stickTankRight.getRawButton(H_DRIVE_BUTTON);
		}
		return false;
	}
}

