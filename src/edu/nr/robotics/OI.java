package edu.nr.robotics;

import edu.nr.robotics.commandgroup.AlignToPlayerStationGroup;
import edu.nr.robotics.commandgroup.CancelAllCommand;
import edu.nr.robotics.commandgroup.DriveAndGetStationAngle;
import edu.nr.robotics.commandgroup.LowerBinGroup;
import edu.nr.robotics.commandgroup.ScoreGroup;
import edu.nr.robotics.commandgroup.StartingConfigurationGroup;
import edu.nr.robotics.subsystems.backElevator.BackElevator;
import edu.nr.robotics.subsystems.backElevator.commands.BackElevatorCloseCommand;
import edu.nr.robotics.subsystems.backElevator.commands.BackElevatorGoToHeightCommand;
import edu.nr.robotics.subsystems.drive.commands.*;
import edu.nr.robotics.subsystems.frontElevator.FrontElevator;
import edu.nr.robotics.subsystems.frontElevator.commands.*;
import edu.nr.robotics.subsystems.frontfingers.FrontFingersCloseCommand;
import edu.nr.robotics.subsystems.frontfingers.FrontFingersOpenCommand;
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
	
	private final double JOYSTICK_DEAD_ZONE = 0.05;
	
	public double gyroValueforPlayerStation = 0;
	
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
			
			FrontElevatorGoToHeightCommand adjust = new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_ADJUST_TOTE_ONE);
			adjust.setGoingDownMaxRange(1);
			adjust.setTalonRamp(true);
			new JoystickButton(coffin2, 7).whenPressed(adjust);
			
			new JoystickButton(coffin2, 6).whenPressed(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BEFORE_TOTE_ADJUST));
			
			new JoystickButton(coffin3, 1).whenPressed(new CancelAllCommand());
			
			new JoystickButton(coffin2, 3).whenPressed(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_SCORING));
	        
			new JoystickButton(coffin2, 2).whenPressed(new ToteTwoToScoreGroup());
			
			new JoystickButton(coffin2, 4).whenPressed(new FrontElevatorGoToHeightCommand(FrontElevator.HEIGHT_BOTTOM));
			
			JoystickButton fighter = new JoystickButton(coffin2, 9);
			fighter.whenPressed(new ActivateDumbDriveCommand());
			fighter.whenReleased(new ActivateSmartDriveCommand());
			
			new JoystickButton(stickTankRight, 3).whenPressed(new AlignToPlayerStationGroup(false));
			//new JoystickButton(stickTankRight, 4).whenPressed(new AlignToPlayerStationGroup(true));
			
			new JoystickButton(stickTankLeft, 1).whenPressed(new CancelAllCommand());
			
			
			//new JoystickButton(stickTankLeft, 5).whenPressed(new DriveAndGetStationAngle());
			new JoystickButton(stickTankRight, 4).whenPressed(new FrontFingersCloseCommand());
			new JoystickButton(stickTankRight, 5).whenPressed(new FrontFingersOpenCommand());
		}
		else
		{
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
			return -snapDriveJoysticks(stickTankLeft.getY());
		}
		
		return 0;
	}
	
	public double getArcadeTurnValue()
	{
		if(USING_ARCADE)
		{
			return -snapDriveJoysticks(stickTankRight.getX());
		}
		
		return 0;
	}
	
	public double getHDriveValue()
	{
		if(USING_COFFIN)
		{
			if(stickTankRight.getRawButton(2))
			{
				return -stickTankRight.getX();
			}
			else
			{
				return snapCoffinJoysticks(-coffin2.getRawAxis(0));
			}
		}
		
		return 0;
	}
	
	//Reversing drive direction makes it easy to maneuver in reverse
	public boolean reverseDriveDirection()
	{
		return stickTankRight.getRawButton(1);
	}
	
	public double getFrontElevatorManual()
	{
		if(USING_COFFIN)
		{
			double value = snapCoffinJoysticks(-coffin3.getRawAxis(1));
			return Math.pow(value, 2) * Math.signum(value);
		}
		
		return 0;
	}
	
	public double getRearElevatorManual()
	{
		if(USING_COFFIN)
		{
			double value = snapCoffinJoysticks(-(coffin3.getRawAxis(0)-.1));
			return Math.pow(value, 2) * Math.signum(value);
		}
		return 0;
	}
	
	
	
	public double getTankLeftValue()
	{
		if(USING_ARCADE)
			return 0;
		
		return -snapDriveJoysticks(stickTankLeft.getY());
	}

	public double getTankRightValue()
	{
		if(USING_ARCADE)
			return 0;
		
		return snapDriveJoysticks(stickTankRight.getY());
	}
	
	private double snapDriveJoysticks(double value)
	{
		if(Math.abs(value) < JOYSTICK_DEAD_ZONE)
    	{
			value = 0;
    	}
    	else if(value > 0)
    	{
    		value -= JOYSTICK_DEAD_ZONE;
    	}
    	else
    	{
    		value += JOYSTICK_DEAD_ZONE;
    	}
		value /=  (1 - JOYSTICK_DEAD_ZONE);
		
		return value;
	}
	
	private double snapCoffinJoysticks(double value)
	{
		if(value > -0.1 && value < 0.1)
			return 0;
		
		return (value-0.1) / 0.9;
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
	
	public double getRawMove()
	{
		return -stickTankLeft.getY();
	}
	
	public double getRawTurn()
	{
		return -stickTankRight.getX();
	}
}

