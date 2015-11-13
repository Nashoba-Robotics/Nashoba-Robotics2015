package edu.nr.robotics.subsystems.drive;

public class Setpoint {
	public double xpos, ypos, vel, acc;
	
	public Setpoint(double xpos, double ypos, double vel, double acc) {
		this.xpos = xpos;
		this.ypos = ypos;
		this.vel = vel;
		this.acc = acc;
	}
}
