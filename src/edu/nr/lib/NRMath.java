package edu.nr.lib;

public class NRMath {

	public static double squareWithSign(double x) {
		return x*x*Math.signum(x);
	}
	
	public static double limit(double x) {
		if (x > 1.0) {
            return 1.0;
        }
        if (x < -1.0) {
            return -1.0;
        }
        return x;
	}
	
}
