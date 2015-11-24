package edu.nr.robotics.custom;

public class Setpoint {

	private double[] velocities; 
	private double[] accelerations;
	//Holds values for all the velocity and acceleration setpoints, they are at 10ms intervals
	
	public Setpoint(double distance) {
		calculateSetpoints();
	}
	
	private void calculateSetpoints() {
		
	}
	
	public double getVelocity(double time) {
		int time_int = (int) time*100;
		return velocities[time_int];
	}
	
	public double getAcceleration(double time) {
		int time_int = (int) time*100;
		return accelerations[time_int];
	}
}
