package edu.nr.robotics.subsystems.camera;

import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Camera extends Subsystem 
{
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
	}
	
    DigitalOutput cameraLight;
    
    public void initDefaultCommand() 
    {
    }
    
    public void cameraOn()
    {
    	cameraLight.set(true);
    }
    
    public void cameraOff()
    {
    	cameraLight.set(false);
    }
}

