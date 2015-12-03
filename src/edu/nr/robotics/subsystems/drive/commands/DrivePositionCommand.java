package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.lib.CMD;
import edu.nr.lib.FalconPathPlanner;
import edu.nr.lib.Position;
import edu.nr.lib.Setpoint;
import edu.nr.robotics.FieldCentric;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DrivePositionCommand extends CMD {

	double Kv,Ka,Kp,Kd;
	long startTime;
	long prevTime;
	long dt;
	double leftErrorLast;
	double rightErrorLast;
	double[][] positions;
	Position leftDistance;
	Position rightDistance;
	
	double timeStep = 20; //period of control loop on Rio, milliseconds
	double totalTime; //milliseconds
	
	double prevLeftVel;
	double prevRightVel;
	FalconPathPlanner path;
	
    public DrivePositionCommand(double time, double[][] positions, double Kv, double Ka, double Kp, double Kd) {
        requires(Drive.getInstance());
        this.Kv = Kv;
        this.Ka = Ka;
        this.Kp = Kp;
        this.Kd = Kd;
        this.positions = positions;
        this.totalTime = time*1000;
    }

    // Called just before this Command runs the first time
    @Override
	protected void onStart() {
    	startTime = System.currentTimeMillis();
        rightErrorLast = 0;
        leftErrorLast = 0;
        prevLeftVel = 0;
        prevRightVel = 0;
        prevTime = startTime;
		createPath();
	}
    
    private void createPath() {
    	FieldCentric.getInstance().reset();
    	leftDistance = new Position(0,0);
    	rightDistance = new Position(0,0);
		path = new FalconPathPlanner(positions);
		path.calculate(totalTime/1000, timeStep/1000, RobotMap.ROBOT_WIDTH);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void onExecute() {
    	SmartDashboard.putNumber("Times so far", (System.currentTimeMillis() - startTime)/timeStep);

    	if(path.smoothLeftVelocity.length < (System.currentTimeMillis() - startTime)/timeStep) {
    		this.cancel();
	    } else {
	    	//Time stuff
	    	dt = System.currentTimeMillis() - prevTime;
	    	prevTime = System.currentTimeMillis();
	    	SmartDashboard.putNumber("Time in command", ((int)(System.currentTimeMillis() - startTime))/1000.0);
	    	
	    	//Left wheel
	    	Setpoint leftSetpoint = lookUpLeftSetpoint(System.currentTimeMillis() - startTime, dt);
	    	double leftXError = leftSetpoint.ypos - FieldCentric.getInstance().getY() - RobotMap.ROBOT_WIDTH;
	    	double leftYError = leftSetpoint.xpos - FieldCentric.getInstance().getX();
	    	double leftError = Math.signum(leftXError + leftYError) * Math.sqrt(Math.pow(leftXError, 2) + Math.pow(leftYError, 2));
	    	double leftErrorDeriv = (leftError - leftErrorLast) / dt;
	    	double leftMotorSpeed = Kv * leftSetpoint.vel + Ka * leftSetpoint.acc + Kp * leftError + Kd * leftErrorDeriv;
	    	leftErrorLast = leftError;
	    	SmartDashboard.putNumber("Left Motor - Motion", leftMotorSpeed);
	    	SmartDashboard.putNumber("Left Error", leftError);
	    	SmartDashboard.putNumber("Left X Point", leftSetpoint.xpos);
	    	SmartDashboard.putNumber("Left Y Point", leftSetpoint.ypos);

	    	//Right wheel
	    	Setpoint rightSetpoint = lookUpRightSetpoint(System.currentTimeMillis() - startTime, dt);
	    	double rightXError = rightSetpoint.xpos - FieldCentric.getInstance().getX();
	    	double rightYError = rightSetpoint.ypos - FieldCentric.getInstance().getY();
	    	double rightError = Math.signum(rightXError + rightYError) * Math.sqrt(Math.pow(rightXError, 2) + Math.pow(rightYError, 2));
	    	double rightErrorDeriv = (rightError - rightErrorLast) / dt;
	    	double rightMotorSpeed = Kv * rightSetpoint.vel + Ka * rightSetpoint.acc + Kp * rightError + Kd * rightErrorDeriv;
	    	rightErrorLast = rightError;
	    	SmartDashboard.putNumber("Right Motor - Motion", rightMotorSpeed);
	    	SmartDashboard.putNumber("Right Error", rightError);
	    	SmartDashboard.putNumber("Right X Point", rightSetpoint.xpos);
	    	SmartDashboard.putNumber("Right Y Point", rightSetpoint.ypos);

	    	Drive.getInstance().tankDrive(leftMotorSpeed, -rightMotorSpeed);
    	}
    }
    
    private Setpoint lookUpLeftSetpoint(long t, long dt) {
    	Setpoint setpoint = lookUpSetpoint(t, dt, leftDistance, path.smoothLeftVelocity, prevLeftVel);
		prevLeftVel = setpoint.vel;
    	return setpoint;
	}
    
    private Setpoint lookUpRightSetpoint(long t, long dt) {
    	Setpoint setpoint = lookUpSetpoint(t, dt, rightDistance, path.smoothRightVelocity, prevRightVel);
		prevRightVel = setpoint.vel;
		return setpoint;
	}
    
    private Setpoint lookUpSetpoint(long t, long dt, Position distance, double[][] velocity, double prevVel) {
    	double xvel = velocity[(int) (t/timeStep)][0];
    	double yvel = velocity[(int) (t/timeStep)][1];
    	distance.x += xvel * dt;
    	distance.y += yvel * dt;
    	double vel = Math.sqrt(Math.pow(xvel, 2) + Math.pow(yvel, 2));
    	double acc = (vel-prevVel)/dt;
    	return new Setpoint(distance.x, distance.y, vel, acc);
    }

	// Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return path.leftPath.length < (System.currentTimeMillis() - startTime)/timeStep;
    }

    // Called once after isFinished returns true
    protected void onEnd(boolean interrupted) {
    	Drive.getInstance().arcadeDrive(0, 0);
    }
}