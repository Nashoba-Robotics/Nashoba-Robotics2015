
package edu.nr.robotics.subsystems.drive;

import edu.nr.robotics.OI;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.commands.DriveJoystickArcadeCommand;
import edu.nr.robotics.subsystems.drive.commands.DriveJoystickTankCommand;
import edu.nr.robotics.subsystems.drive.mxp.NavX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;

/**
 *
 */
public class Drive extends Subsystem
{	
	public static final double JOYSTICK_DRIVE_P = 0.25;
	public static final double CENTER_OF_ROTATION_RELATIVE_TO_CAMERA_FEET = 16.25/12;
	
	private static Drive singleton;
	
	private Encoder leftEnc, rightEnc;
	private double ticksPerRev = 256, wheelDiameter = 0.4975;
	
	private PIDController leftPid, rightPid;
	
	//Max speed of the robot in ft/sec (used to scale down encoder values for PID) See constructor for details.
	private final double MAX_ENCODER_RATE = 12;
	
	
	CANTalon leftTalon, rightTalon;
	CANTalon hDrive;
	
	private Drive()
	{
		leftTalon = new CANTalon(RobotMap.leftDriveTalon1);
		leftTalon.enableBrakeMode(true);
		
		CANTalon tempLeftTalon = new CANTalon(RobotMap.leftDriveTalon2);
		tempLeftTalon.changeControlMode(ControlMode.Follower);
		tempLeftTalon.set(leftTalon.getDeviceID());
		tempLeftTalon.enableBrakeMode(true);
		
		rightTalon = new CANTalon(RobotMap.rightDriveTalon1);
		rightTalon.enableBrakeMode(true);
		
		CANTalon tempRightTalon = new CANTalon(RobotMap.rightDriveTalon2);
		tempRightTalon.changeControlMode(ControlMode.Follower);
		tempRightTalon.set(rightTalon.getDeviceID());
		tempRightTalon.enableBrakeMode(true);
		
		leftEnc = new Encoder(RobotMap.ENCODER_LEFT_A, RobotMap.ENCODER_LEFT_B);
		rightEnc = new Encoder(RobotMap.ENCODER_RIGHT_A, RobotMap.ENCODER_RIGHT_B);
		
		leftEnc.setPIDSourceParameter(PIDSourceParameter.kRate);
		rightEnc.setPIDSourceParameter(PIDSourceParameter.kRate);
		
		double distancePerPulse = (1 / ticksPerRev) * Math.PI * wheelDiameter;
		
		//Max speed of robot is 20 ft/sec, so in order for our PIDController to work, the scale of encoder rate
		//in ft/sec must be on a scale of -1 to 1 (so it can be used to calculate motor output)
		leftEnc.setDistancePerPulse(distancePerPulse / MAX_ENCODER_RATE);
		rightEnc.setDistancePerPulse(distancePerPulse / MAX_ENCODER_RATE);
		
		leftPid = new PIDController(JOYSTICK_DRIVE_P, 0, 0, 1, leftEnc, leftTalon);
		rightPid = new PIDController(JOYSTICK_DRIVE_P, 0, 0, 1, rightEnc, rightTalon);
		leftPid.enable();
		rightPid.enable();

		hDrive = new CANTalon(RobotMap.HDriveTalon);
		
		NavX.init();
	}
	
	public static Drive getInstance()
    {
		init();
        return singleton;
    }
	
	public static void init()
	{
		if(singleton == null)
		{
			singleton = new Drive();
		}
	}
	
	/**
	 * @param value Maximum change in voltage, in volts / sec.
	 */
	public void setTalonRampRate(double value)
	{
		leftTalon.setVoltageRampRate(value);
		rightTalon.setVoltageRampRate(value);
	}
	
	public void initDefaultCommand()
	{
		if(OI.USING_ARCADE)
		{
			setDefaultCommand(new DriveJoystickArcadeCommand());
		}
		else
		{
			setDefaultCommand(new DriveJoystickTankCommand());
		}
    }

	public void arcadeDrive(double moveValue, double rotateValue)
	{
		this.arcadeDrive(moveValue, rotateValue, false);
	}
	
	public void arcadeDrive(double moveValue, double rotateValue, boolean squaredInputs) 
	{
        double leftMotorSpeed;
        double rightMotorSpeed;

        moveValue = limit(moveValue);
        rotateValue = limit(rotateValue);

        if (squaredInputs) 
        {
            // square the inputs (while preserving the sign) to increase fine control while permitting full power
            if (moveValue >= 0.0) {
                moveValue = (moveValue * moveValue);
            } else {
                moveValue = -(moveValue * moveValue);
            }
            if (rotateValue >= 0.0) {
                rotateValue = (rotateValue * rotateValue);
            } else {
                rotateValue = -(rotateValue * rotateValue);
            }
        }

        if (moveValue > 0.0)
        {
            if (rotateValue > 0.0) 
            {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = Math.max(moveValue, rotateValue);
            } 
            else
            {
                leftMotorSpeed = Math.max(moveValue, -rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            }
        } 
        else 
        {
            if (rotateValue > 0.0) 
            {
                leftMotorSpeed = -Math.max(-moveValue, rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            } 
            else 
            {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
            }
        }
        rightMotorSpeed = -rightMotorSpeed;
        
        SmartDashboard.putNumber("Arcade Left Motors", leftMotorSpeed);
        SmartDashboard.putNumber("Arcade Right Motors", rightMotorSpeed);
        
        if(leftPid.isEnable() && rightPid.isEnable())
        {
        	leftPid.setSetpoint(leftMotorSpeed);
            rightPid.setSetpoint(rightMotorSpeed);
        }
        else
        {
        	setRawMotorSpeed(leftMotorSpeed, -rightMotorSpeed);
        }
	}
	
	public void setHDrive(double value)
	{
		hDrive.set(value);
	}
	
	public void setPIDEnabled(boolean enabled)
	{
		if(enabled && !leftPid.isEnable())
		{
			leftPid.enable();
			rightPid.enable();
		}
		else if(!enabled && leftPid.isEnable())
		{
			leftPid.disable();
			rightPid.disable();
		}
	}
	
	public void setRawMotorSpeed(double left, double right)
	{
		setPIDEnabled(false);
		
		leftTalon.set(left);
		rightTalon.set(-right);
	}
	
	public void tankDrive(double leftMotorSpeed, double rightMotorSpeed)
	{
		leftPid.setSetpoint(leftMotorSpeed);
        rightPid.setSetpoint(rightMotorSpeed);
	}
	
	public void setDriveP(double p)
	{
		leftPid.setPID(p, 0, 0, 1);
		rightPid.setPID(p, 0, 0, 1);
	}
	
	private double limit(double num)
	{
		if (num > 1.0) {
            return 1.0;
        }
        if (num < -1.0) {
            return -1.0;
        }
        return num;
	}
	
	public double getAngleDegrees() 
	{
		return NavX.getInstance().getYaw();
	}
	
	public double getAngleRadians()
	{
		return getAngleDegrees() * (Math.PI)/180d;
	}
	
	public double getEncoderAve()
	{
		return (getEncoder1Distance() + getEncoder2Distance()) / 2f;
	}
	
	public void resetEncoders()
	{
		leftEnc.reset();
		rightEnc.reset();
	}
	
	public double getEncoder1Distance()
	{
		return leftEnc.getDistance() * MAX_ENCODER_RATE;
	}
	
	public double getEncoder2Distance()
	{
		return -rightEnc.getDistance() * MAX_ENCODER_RATE;
	}
	
	public double getLeftEncoderSpeed()
	{
		return leftEnc.getRate() * MAX_ENCODER_RATE;
	}
	
	public double getRightEncoderSpeed()
	{
		return -rightEnc.getRate() * MAX_ENCODER_RATE;
	}
	
	public double getEncoderAverageSpeed()
	{
		return (getRightEncoderSpeed() + getLeftEncoderSpeed()) / 2;
	}
	
	public void putSmartDashboardInfo()
	{
		//SmartDashboard.putNumber("NavX Yaw", NavX.getInstance().getYaw());
		//SmartDashboard.putNumber("NavX Pitch", NavX.getInstance().getPitch());
		
		//SmartDashboard.putNumber("Gyro", getAngleDegrees());
		
		
		SmartDashboard.putNumber("Encoders", this.getEncoderAve());
		SmartDashboard.putNumber("NavX Pitch", NavX.getInstance().getPitch());
		SmartDashboard.putNumber("NavX Yaw", NavX.getInstance().getYaw());
	}
}

