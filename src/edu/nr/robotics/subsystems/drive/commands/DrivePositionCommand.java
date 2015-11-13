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
	double errorLast;
	
    public DrivePositionCommand(Position[] positions, double Kv, double Ka, double Kp, double Kd) {
        requires(Drive.getInstance());
        startTime = System.currentTimeMillis();
        errorLast = 0;
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
    	Setpoint setpoint = lookUpSetpoint(System.currentTimeMillis() - startTime);
    	double error = setpoint.pos - FieldCentric.getInstance().getDistance();
    	double error_deriv = (error - errorLast) / dt;
    	double motorSpeed = Kv * setpoint.vel + Ka * setpoint.acc + Kp * error + Kd * error_deriv;
    	Drive.getInstance().tankDrive(motorSpeed, motorSpeed);
    	prevTime = System.currentTimeMillis();
    	errorLast = error;
    }

    private void createPath() {
    	// TODO determine the path the robot will take (a spline curve) using positions
    }
    
    private Setpoint lookUpSetpoint(long t) {
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
