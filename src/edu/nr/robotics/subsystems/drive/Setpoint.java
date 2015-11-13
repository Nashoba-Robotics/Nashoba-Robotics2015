package edu.nr.robotics.subsystems.drive;

public class Setpoint {
	public double pos, vel, acc;
	
	public Setpoint(double pos, double vel, double acc) {
		this.pos = pos;
		this.vel = vel;
		this.acc = acc;
	}
}
