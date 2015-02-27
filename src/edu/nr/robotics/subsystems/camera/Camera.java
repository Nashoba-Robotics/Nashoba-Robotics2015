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
    	if(state == false)
    	{
    		cameraLight.set(true);
    		state = true;
    	}
    }
    
    public void cameraOff()
    {
    	if(state == true)
    	{
    		cameraLight.set(false);
    		state = false;
    	}
    }
    
    public void toggleCamera()
    {
    	state = !state;
    	cameraLight.set(state);
    }
}

