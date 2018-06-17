package OrbitalCKK;

public class Force {
	double magnitude;
	double angle;
	
	//constructor
	public Force (double magnitude, double direction){
		this.magnitude = magnitude;
		this.angle = direction;
	}
	
	/**
	 * Uses trig function cos to find the x-component of this force
	 * @return x-component of the force
	 */
	public double componentX () {
		double component = 0;
		component = this.magnitude*Math.cos(this.angle);
		return component;
	}
	
	/**
	 * Uses trig function sin to find the y-component of this force
	 * @return y-component of the force
	 */
	public double componentY () {
		double component = 0;
		component = this.magnitude*Math.sin(this.angle);
		return component;
	}
}
