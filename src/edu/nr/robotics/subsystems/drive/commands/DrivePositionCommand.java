package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.FieldCentric;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.FalconPathPlanner;
import edu.nr.robotics.subsystems.drive.Setpoint;

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
	
	double timeStep = 100; //period of control loop on Rio, milliseconds
	double totalTime = 8000; //milliseconds
	
	double prevLeftVel;
	double prevRightVel;
	FalconPathPlanner path;
	
    public DrivePositionCommand(double[][] positions, double Kv, double Ka, double Kp, double Kd) {
        requires(Drive.getInstance());
        this.Kv = Kv;
        this.Ka = Ka;
        this.Kp = Kp;
        this.Kd = Kd;
        this.positions = positions;
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
		path = new FalconPathPlanner(positions);
		path.calculate(totalTime/1000, timeStep/1000, RobotMap.ROBOT_WIDTH);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void onExecute() {
    	dt = System.currentTimeMillis() - prevTime;
    	prevTime = System.currentTimeMillis();

    	//Left wheel
    	Setpoint leftSetpoint = lookUpLeftSetpoint(System.currentTimeMillis() - startTime);
    	double leftXError = leftSetpoint.xpos - FieldCentric.getInstance().getX();
    	double leftYError = leftSetpoint.ypos - FieldCentric.getInstance().getY();
    	double leftError = Math.sqrt(Math.pow(leftXError, 2) + Math.pow(leftYError, 2));
    	double leftErrorDeriv = (leftError - leftErrorLast) / dt;
    	double leftMotorSpeed = Kv * leftSetpoint.vel + Ka * leftSetpoint.acc + Kp * leftError + Kd * leftErrorDeriv;
    	leftErrorLast = leftError;

    	//Right wheel
    	Setpoint rightSetpoint = lookUpRightSetpoint(System.currentTimeMillis() - startTime);
    	double rightXError = rightSetpoint.xpos - FieldCentric.getInstance().getX();
    	double rightYError = rightSetpoint.ypos - FieldCentric.getInstance().getY();
    	double rightError = Math.sqrt(Math.pow(rightXError, 2) + Math.pow(rightYError, 2));
    	double rightErrorDeriv = (rightError - rightErrorLast) / dt;
    	double rightMotorSpeed = Kv * rightSetpoint.vel + Ka * rightSetpoint.acc + Kp * rightError + Kd * rightErrorDeriv;
    	rightErrorLast = rightError;
    	
    	Drive.getInstance().tankDrive(leftMotorSpeed, rightMotorSpeed);
    }
    
    private Setpoint lookUpLeftSetpoint(long t) {
    	Setpoint setpoint = lookUpSetpoint(t, path.leftPath, path.smoothLeftVelocity, prevLeftVel);
		prevLeftVel = setpoint.vel;
    	return setpoint;
	}
    
    private Setpoint lookUpRightSetpoint(long t) {
    	Setpoint setpoint = lookUpSetpoint(t, path.rightPath, path.smoothRightVelocity, prevRightVel);
		prevRightVel = setpoint.vel;
    	return setpoint;
	}
    
    private Setpoint lookUpSetpoint(long t, double[][] path, double[][] velocity, double prevVel) {
    	double xpos = path[(int) (t/timeStep)][0];
    	double ypos = path[(int) (t/timeStep)][1];
    	double xvel = velocity[(int) (t/timeStep)][0];
    	double yvel = velocity[(int) (t/timeStep)][1];
    	double vel = Math.sqrt(Math.pow(xvel, 2) + Math.pow(yvel, 2));
    	double acc = (vel-prevVel)/dt;
    	return new Setpoint(xpos, ypos, vel, acc);
    }

	// Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void onEnd(boolean interrupted) {
    	Drive.getInstance().arcadeDrive(0, 0);
    }
}