package edu.nr.robotics.subsystems.camera;

import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Camera extends Subsystem 
{
	private boolean state = false;
	private static Camera singleton;
	public static Camera getInstance()
	{
		if(singleton == null)
			singleton = new Camera();
		return singleton;
	}
	
	private Camera()
	{
		cameraLight = new DigitalOutput(RobotMap.CAMERA_DIGITAL_PORT);
		cameraOff();
	}
	
    DigitalOutput cameraLight;
    
    public void initDefaultCommand() 
    {
    }
    
    public void cameraOn()
    {
    	cameraLight.set(true);
    	if(state == false)
    	{
    		state = true;
    		SmartDashboard.putNumber("RealmsCommandRecieved", 1);
    	}
    }
    
    public void cameraOff()
    {
    	cameraLight.set(false);
    	if(state == true)
		{
    		state = false;
    		SmartDashboard.putNumber("RealmsCommandRecieved", 1);
		}
    }
}

