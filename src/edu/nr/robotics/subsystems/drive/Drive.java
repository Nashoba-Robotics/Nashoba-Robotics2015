
package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRMath;
import edu.nr.lib.navx.NavX;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.commands.DriveJoystickCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CANTalon;
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
		leftEnc.setDistancePerPulse(distancePerPulse / RobotMap.MAX_ENCODER_RATE);
		rightEnc.setDistancePerPulse(distancePerPulse / RobotMap.MAX_ENCODER_RATE);
		
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
		setDefaultCommand(new DriveJoystickCommand());
    }

	public void arcadeDrive(double moveValue, double rotateValue)
	{
		this.arcadeDrive(moveValue, rotateValue, false);
	}
	
	public void arcadeDrive(double moveValue, double rotateValue, boolean squaredInputs) 
	{
		double leftMotorSpeed;
        double rightMotorSpeed;

        moveValue = NRMath.limit(moveValue);
        rotateValue = NRMath.limit(rotateValue);

        if (squaredInputs) 
        {
            // square the inputs (while preserving the sign) to increase fine control while permitting full power
            NRMath.squareWithSign(moveValue);
            NRMath.squareWithSign(rotateValue);
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
        
        SmartDashboard.putNumber("Arcade Left Motors", leftMotorSpeed);
        SmartDashboard.putNumber("Arcade Right Motors", rightMotorSpeed);
        SmartDashboard.putBoolean("Half Speed", false);
        
        tankDrive(leftMotorSpeed, -rightMotorSpeed);
	}
	
	
	//Source for a lot of this from 254's code from 2015.
	public void arcadeDrive(double throttle, double wheel, double oldWheel, boolean squaredInputs) 
	{
        double leftMotorSpeed;
        double rightMotorSpeed;

        throttle = NRMath.limit(throttle);
        wheel = NRMath.limit(wheel);
        
        
        if (squaredInputs) 
        {
            // square the inputs (while preserving the sign) to
        	// increase fine control while permitting full power
            NRMath.squareWithSign(throttle);
            NRMath.squareWithSign(wheel);
        }
        
        double negInertia = wheel - oldWheel;
               
        // Negative inertia!
        double negInertiaScalar;
        
        if (wheel * negInertia > 0) {
            negInertiaScalar = 2.5;
        } else {
            if (Math.abs(wheel) > 0.65) {
                negInertiaScalar = 5.0;
            } else {
                negInertiaScalar = 3.0;
            }
        }
        
        wheel = wheel + negInertia * negInertiaScalar;
        
        
        rightMotorSpeed = leftMotorSpeed = throttle;
        leftMotorSpeed += wheel;
        rightMotorSpeed -= wheel;

        if (leftMotorSpeed > 1.0) {
        	rightMotorSpeed -= leftMotorSpeed - 1.0;
            leftMotorSpeed = 1.0;
        } else if (rightMotorSpeed > 1.0) {
        	leftMotorSpeed -= rightMotorSpeed - 1.0;
        	rightMotorSpeed = 1.0;
        } else if (leftMotorSpeed < -1.0) {
        	rightMotorSpeed += -1.0 - leftMotorSpeed;
            leftMotorSpeed = -1.0;
        } else if (rightMotorSpeed < -1.0) {
        	leftMotorSpeed += -1.0 - rightMotorSpeed;
        	rightMotorSpeed = -1.0;
        }
                
        SmartDashboard.putNumber("Arcade Left Motors", leftMotorSpeed);
        SmartDashboard.putNumber("Arcade Right Motors", rightMotorSpeed);
        SmartDashboard.putBoolean("Half Speed", false);
        
        tankDrive(leftMotorSpeed, -rightMotorSpeed);
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
		rightTalon.set(right);
	}
	
	public void tankDrive(double leftMotorSpeed, double rightMotorSpeed) {

		if(leftPid.isEnable() && rightPid.isEnable())
        {
        	leftPid.setSetpoint(leftMotorSpeed);
            rightPid.setSetpoint(rightMotorSpeed);
        }
        else
        {
        	setRawMotorSpeed(leftMotorSpeed, rightMotorSpeed);
        }
	}
	
	public void setDriveP(double p)
	{
		leftPid.setPID(p, 0, 0, 1);
		rightPid.setPID(p, 0, 0, 1);
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
		return leftEnc.getDistance() * RobotMap.MAX_ENCODER_RATE;
	}
	
	public double getEncoder2Distance()
	{
		return -rightEnc.getDistance() * RobotMap.MAX_ENCODER_RATE;
	}
	
	public double getLeftEncoderSpeed()
	{
		return leftEnc.getRate() * RobotMap.MAX_ENCODER_RATE;
	}
	
	public double getRightEncoderSpeed()
	{
		return -rightEnc.getRate() * RobotMap.MAX_ENCODER_RATE;
	}
	
	public double getEncoderAverageSpeed()
	{
		return (getRightEncoderSpeed() + getLeftEncoderSpeed()) / 2;
	}
	
	public void putSmartDashboardInfo()
	{	
		SmartDashboard.putNumber("Encoder Left", this.getEncoder1Distance());
		SmartDashboard.putNumber("Encoder Right", this.getEncoder2Distance());
		
		SmartDashboard.putNumber("Encoders", this.getEncoderAve());
	}
}

