package edu.nr.robotics.subsystems.drive.commands;

import edu.nr.robotics.FieldCentric;
import edu.nr.robotics.subsystems.CMD;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.Position;
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
	
    public DrivePositionCommand(Position[] positions, double Kv, double Ka, double Kp, double Kd) {
        requires(Drive.getInstance());
        startTime = System.currentTimeMillis();
        rightErrorLast = 0;
        leftErrorLast = 0;
        prevTime = startTime;
        this.Kv = Kv;
        this.Ka = Ka;
        this.Kp = Kp;
        this.Kd = Kd;
    }

    // Called just before this Command runs the first time
    @Override
	protected void onStart() {
		createPath();
	}

    // Called repeatedly when this Command is scheduled to run
    protected void onExecute() {
    	dt = System.currentTimeMillis() - prevTime;
    	prevTime = System.currentTimeMillis();

    	//Left wheel
    	Setpoint leftSetpoint = lookUpLeftSetpoint(System.currentTimeMillis() - startTime);
    	double leftError = leftSetpoint.pos - FieldCentric.getInstance().getDistance();
    	double leftErrorDrive = (leftError - leftErrorLast) / dt;
    	double leftMotorSpeed = Kv * leftSetpoint.vel + Ka * leftSetpoint.acc + Kp * leftError + Kd * leftErrorDrive;
    	leftErrorLast = leftError;

    	//Right wheel
    	Setpoint rightSetpoint = lookUpRightSetpoint(System.currentTimeMillis() - startTime);
    	double rightError = rightSetpoint.pos - FieldCentric.getInstance().getDistance();
    	double rightErrorDeriv = (rightError - rightErrorLast) / dt;
    	double rightMotorSpeed = Kv * rightSetpoint.vel + Ka * rightSetpoint.acc + Kp * rightError + Kd * rightErrorDeriv;
    	rightErrorLast = rightError;
    	
    	Drive.getInstance().tankDrive(leftMotorSpeed, rightMotorSpeed);
    }

    private void createPath() {
    	// TODO determine the path the robot will take (a spline curve) using positions
    }
    
    private Setpoint lookUpLeftSetpoint(long t) {
		// TODO get the pos, vel, and acc we should be at at time t
    	double pos = 0, vel = 0, acc = 0;
		return new Setpoint(pos,vel,acc);
	}
    
    private Setpoint lookUpRightSetpoint(long t) {
		// TODO get the pos, vel, and acc we should be at at time t
    	double pos = 0, vel = 0, acc = 0;
		return new Setpoint(pos,vel,acc);
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
