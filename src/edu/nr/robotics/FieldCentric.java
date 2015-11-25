package edu.nr.robotics;

import edu.nr.robotics.mxp.NavX;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.Position;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FieldCentric {

	//NOTE: X is forward, Y is side-to-side
	
	private static FieldCentric instance;
	private final double initialTheta;
    private double initialGyro = 0;
    private double x = 0, y = 0, dis = 0, lastEncoderDistance = 0;
    private long lastUpdateTime;

	public static FieldCentric getInstance() {
		if(instance == null)
			instance = new FieldCentric(Math.PI/2);
		return instance;
	}
	
	public FieldCentric(double initialTheta)
	{
		this.initialTheta = initialTheta;
	}
	
	public void update()
    {
        if(System.currentTimeMillis() - lastUpdateTime > 300)
        {
            //System.err.println("WARNING: FieldCentric not being called often enough: (" + ((System.currentTimeMillis() - lastUpdateTime)/1000f) + "s)");
        }
        
        double angle = Drive.getInstance().getAngleDegrees() - initialGyro;
        angle *= (Math.PI / 180); //Convert to radians
        angle *= -1; //Gyro is reversed (clockwise causes an increase in the angle)
        angle += initialTheta; //Make the initial position be facing north
        
        double ave = Drive.getInstance().getEncoderAve();
        double delta_x_r = (ave-lastEncoderDistance);
        double deltax = delta_x_r * Math.sin(angle);
        double deltay = delta_x_r * Math.cos(angle);
        x += deltax;
        y += deltay;
        dis += delta_x_r;
        
        lastEncoderDistance = ave;
        
        lastUpdateTime = System.currentTimeMillis();
    }
	
	//Gets the distance the robot has moved since FieldCentric was reset
	public double getDistance() {
		return dis;
	}
	
    public double getX()
    {
    	return x;
    }
	
	public double getY()
    {
    	return y;
    }
    
    public Position getPosition() {
    	return new Position(x,y);
    }
    
    //The angle used for current coordinate calculations
    public double getFieldCentricAngleRadians()
    {
    	return (Drive.getInstance().getAngleDegrees() - initialGyro) * (-Math.PI / 180) + initialTheta;
    }

    public void reset()
    {
    	x = 0;
    	y = 0;
    	lastEncoderDistance = Drive.getInstance().getEncoderAve();
    	initialGyro = Drive.getInstance().getAngleDegrees();
    }

	public void putSmartDashboardInfo() {
		SmartDashboard.putNumber("NavX Yaw", NavX.getInstance().getYaw());
		SmartDashboard.putNumber("NavX Pitch", NavX.getInstance().getPitch());
		SmartDashboard.putNumber("NavX Roll", NavX.getInstance().getRoll());
		
		SmartDashboard.putNumber("Gyro", Drive.getInstance().getAngleDegrees());
	}
}
