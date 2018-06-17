package OrbitalCKK;

import Projectile.Particle;

public class Planet extends Particle {

	double mass;
	double Ax;
	double Ay;

	//CONSTRUCTORS

	public Planet (double x, double y, double vx, double vy, double mass){
		this.x = x;
		this.y = y;
		this.Vx = vx;
		this.Vy = vy;
		this.mass = mass;
	}

	public Planet(){

	}

	//FORCE STUFF
	
	/**
	 * Calculates the distance on the coordinate plane between this class's planet and another planet
	 * @param mass2			second mass 
	 * @return distance		distance between two masses
	 */
	public double distance(Planet mass2){

		double x1 = this.getX();
		double y1 = this.getY();
		double x2 = mass2.getX();
		double y2 = mass2.getY();

		//distance formula
		double distance = Math.sqrt( Math.pow( (x2-x1) , 2) + Math.pow( (y2-y1) , 2) );

		return distance;

	}

	/**
	 * Calculates angle between this class's planet and another planet
	 * Angle above the horizontal
	 * @param mass2
	 * @return angle in radians
	 */
	public double angle(Planet mass2){

		double x1 = this.getX();
		double y1 = this.getY();
		double x2 = mass2.getX();
		double y2 = mass2.getY();

		double opposite = Math.abs(y2-y1);
		double adjacent = Math.abs(x2-x1);

		double angle = Math.atan(opposite/adjacent);

		return angle;
	}

	/**
	 * Calculates the force of gravity FROM mass2 on this class's planet
	 * 
	 * NOTE: this does NOT take into account negative or positive direction, 
	 * this happens in the sumOfForces function of Orbital through the
	 * positiveX and positiveY functions of this class
	 * 
	 * @param mass2		second mass
	 * @return force	the force of gravity from mass2 on mass1
	 * 
	 */
	public Force forceOfGravity(Planet mass2){
		double distance = this.distance(mass2); //gets distance
		double angle = this.angle(mass2);
		double mag = (6.6726*(Math.pow(10, -11)) * this.mass * mass2.mass) / (distance*distance); //FORMULA
			
		Force f = new Force(mag,angle);

		return f;
	}

	
	/**
	 * Decides whether the force of gravity in the x-direction FROM mass2 on this mass should be positive
	 * @param mass2
	 * @return
	 */
	public boolean positiveX(Planet mass2){
		if(this.x < mass2.x){
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Decides whether the force of gravity in the y-direction FROM mass2 on this mass should be positive
	 * @param mass2
	 * @return
	 */
	public boolean positiveY(Planet mass2){
		if(this.y < mass2.y){
			return true;
		}
		else {
			return false;
		}
	}

	//SETTING VALUES DURING DOSTEP
	
	/**
	 * Sets this planet's acceleration in the x-direction for a given sum of the forces in the x-direction 
	 * @param sumofforces	sum of the forces in the x-direction
	 */
	public void newAx(double sumforces){
		this.Ax = sumforces / this.mass; // f = ma, a = f/m
	}

	/**
	 * Sets this planet's acceleration in the y-direction for a given sum of the forces in the x-direction 
	 * @param sumofforces	sum of the forces in the y-direction
	 */
	public void newAy(double sumforces){
		this.Ay = sumforces / this.mass; // f = ma, a = f/m
	}
	
	
	/**
	 * Sets a new velocity in the X direction
	 * @param ax		acceleration in the x direction
	 * @param timestep	timestep
	 */
	public void newVX(double ax, double timestep){
		this.Vx = this.Vx + (ax*timestep); // v = vo + at
	}


	/**
	 * Sets a new velocity in the Y direction
	 * @param ax		acceleration in the y direction
	 * @param timestep	timestep
	 */
	public void newVY(double ay, double timestep){
		this.Vy = this.Vy + (ay*timestep); // v = vo + at
	}

	/**
	 * Sets a new x-position
	 * @param vx		velocity in the x-direction
	 * @param timestep	timestep
	 */
	public void newPositionX(double vx, double timestep){
		this.x = this.x + (vx*timestep); //x = xo + vt
		this.setXY(this.x, this.y);
	}

	/**
	 * Sets a new y-position
	 * @param vy		velocity in the y-direction
	 * @param timestep	timestep
	 */
	public void newPositionY(double vy, double timestep){
		this.y = this.y + vy*timestep; //y = yo + vt
		this.setXY(this.x, this.y);
	}

	
	//MASS

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}


}
