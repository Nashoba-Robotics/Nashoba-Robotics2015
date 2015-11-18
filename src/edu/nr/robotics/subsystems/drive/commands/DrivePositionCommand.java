package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.FieldCentric;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.FalconPathPlanner;
import edu.nr.robotics.subsystems.drive.Setpoint;
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
		path = new FalconPathPlanner(positions);
		path.calculate(totalTime/1000, timeStep/1000, RobotMap.ROBOT_WIDTH);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void onExecute() {
    	SmartDashboard.putNumber("Times so far", (System.currentTimeMillis() - startTime)/timeStep);

    	if(path.leftPath.length < (System.currentTimeMillis() - startTime)/timeStep) {
    		SmartDashboard.putString("Cancelled by time?", "Yes");
    		this.cancel();
	    } else {
    		SmartDashboard.putString("Cancelled by time?", "No");

	    	dt = System.currentTimeMillis() - prevTime;
	    	prevTime = System.currentTimeMillis();
	    	SmartDashboard.putNumber("Time in command", System.currentTimeMillis() - startTime);
	    	//Left wheel
	    	Setpoint leftSetpoint = lookUpLeftSetpoint(System.currentTimeMillis() - startTime);
	    	double leftXError = leftSetpoint.ypos - FieldCentric.getInstance().getY() - RobotMap.ROBOT_WIDTH;
	    	double leftYError = leftSetpoint.xpos - FieldCentric.getInstance().getX();
	    	double leftError = Math.signum(leftXError + leftYError) * Math.sqrt(Math.pow(leftXError, 2) + Math.pow(leftYError, 2));
	    	double leftErrorDeriv = (leftError - leftErrorLast) / dt;
	    	double leftMotorSpeed = Kv * leftSetpoint.vel + Ka * leftSetpoint.acc + Kp * leftError + Kd * leftErrorDeriv;
	    	leftErrorLast = leftError;
	    	SmartDashboard.putNumber("Left Error", leftError);
	    	SmartDashboard.putNumber("Left X Point", leftSetpoint.xpos);
	    	SmartDashboard.putNumber("Left Y Point", leftSetpoint.ypos);

	    	//Right wheel
	    	Setpoint rightSetpoint = lookUpRightSetpoint(System.currentTimeMillis() - startTime);
	    	double rightXError = rightSetpoint.xpos - FieldCentric.getInstance().getX();
	    	double rightYError = rightSetpoint.ypos - FieldCentric.getInstance().getY();
	    	double rightError = Math.signum(rightXError + rightYError) * Math.sqrt(Math.pow(rightXError, 2) + Math.pow(rightYError, 2));
	    	double rightErrorDeriv = (rightError - rightErrorLast) / dt;
	    	double rightMotorSpeed = Kv * rightSetpoint.vel + Ka * rightSetpoint.acc + Kp * rightError + Kd * rightErrorDeriv;
	    	rightErrorLast = rightError;
	    	SmartDashboard.putNumber("Right Error", rightError);
	    	SmartDashboard.putNumber("Right X Point", rightSetpoint.xpos);
	    	SmartDashboard.putNumber("Right Y Point", rightSetpoint.ypos);

	    	Drive.getInstance().tankDrive(leftMotorSpeed, -rightMotorSpeed);
    	}
    }
    
    private Setpoint lookUpLeftSetpoint(long t) {
    	Setpoint setpoint = lookUpSetpoint(t, path.smoothPath, path.smoothLeftVelocity, prevLeftVel);
		prevLeftVel = setpoint.vel;
    	return setpoint;
	}
    
    private Setpoint lookUpRightSetpoint(long t) {
    	Setpoint setpoint = lookUpSetpoint(t, path.smoothPath, path.smoothRightVelocity, prevRightVel);
		prevRightVel = setpoint.vel;
    	SmartDashboard.putNumber("Path Value 50", path.smoothPath[50][1]);
		return setpoint;
	}
    
    private Setpoint lookUpSetpoint(long t, double[][] path, double[][] velocity, double prevVel) {
    	double xpos = path[(int) (t/timeStep)][0];
    	double ypos = path[(int) (t/timeStep)][1];
    	double xvel = velocity[(int) (t/timeStep)][0];
    	double yvel = velocity[(int) (t/timeStep)][1];
    	double vel = Math.sqrt(Math.pow(xvel, 2) + Math.pow(yvel, 2));
    	double acc = (vel-prevVel)/dt;
    	SmartDashboard.putNumber("Path Length", velocity.length);
    	return new Setpoint(xpos, ypos, vel, acc);
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