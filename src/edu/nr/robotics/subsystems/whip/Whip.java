package edu.nr.robotics.subsystems.whip;

import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Whip extends Subsystem
{
	private static Whip singleton;
	public static Whip getInstance()
	{
		init();
		return singleton;
	}
	
	public static void init()
	{
		if(singleton == null)
			singleton = new Whip();
	}
	
	
	private DoubleSolenoid whip;
	private DoubleSolenoid lock;
	
	private Whip()
	{
		whip = new DoubleSolenoid(RobotMap.pneumaticsModule,
				RobotMap.WHIP_FORWARD,
				RobotMap.WHIP_REVERSE);
		lock = new DoubleSolenoid(RobotMap.pneumaticsModule,
				RobotMap.WHIP_LOCK_FORWARD,
				RobotMap.WHIP_LOCK_REVERSE);
	}

	@Override
	protected void initDefaultCommand()
	{
		this.setDefaultCommand(new WhipIdleCommand());
	}
	
	public void deployWhip()
	{
		whip.set(Value.kForward);
	}
	
	public void undeployWhip()
	{
		whip.set(Value.kReverse);
	}
	
	public void deployLock()
	{
		lock.set(Value.kForward);
	}
	
	public void undeployLock()
	{
		lock.set(Value.kReverse);
	}
}
