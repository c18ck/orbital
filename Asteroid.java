package OrbitalCKK;

import java.awt.Color;

import java.util.ArrayList;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.DrawableShape;
import org.opensourcephysics.display.TextBox;
import org.opensourcephysics.display.Trail;
import org.opensourcephysics.frames.PlotFrame;

/**
 * 
 * The initial settings of this program present a collision between an asteroid and the earth.
 * 
 * The program models a suggested method of diverting an asteroid, called a "gravity tractor,"
 * 	which uses the force of gravity between a hovering "tractor" and the asteroid to change the course.
 * 
 * Although the actual proposed gravity tractor is lighter than my model, it takes 20 years (i.e. 20 earth orbits) 
 * 	to work, so I used a heavier tractor that can do the job in one year. 
 * 
 * @author Claire Keenan-Kurgan
 *
 */


public class Asteroid extends AbstractSimulation {

	ArrayList<Planet> masses1 = new ArrayList<Planet>(); //masses w/o tractor
	ArrayList<Trail> trails1 = new ArrayList<Trail>();	//trails w/o tractor
	PlotFrame frame1 = new PlotFrame("X", "Y", "No tractor");	
	double time1 = 0;
	TextBox timer1 = new TextBox("Timer"); //timer text box
	double timestep1;
	boolean collision1 = false;


	ArrayList<Planet> masses2 = new ArrayList<Planet>(); //masses w tractor
	ArrayList<Trail> trails2 = new ArrayList<Trail>();	//trails w tractor
	PlotFrame frame2 = new PlotFrame("X", "Y", "With tractor");	
	double time2 = 0;
	TextBox timer2 = new TextBox("Timer"); //timer text box
	double timestep2;
	boolean collision2 = false;

	DrawableShape tractor = DrawableShape.createRectangle(0, 0, 2e10, 2e10);
	
	TextBox collisionframe1 = new TextBox("Collision!");
	TextBox collisionframe2 = new TextBox("Collision!");

	
	//main
	public static void main(String[] args) {
		SimulationControl.createApp(new Asteroid());
	}

	//runs simulation every .01 seconds
	protected void doStep() {		

		//change timers
		time1 += timestep1; 
		time2 += timestep2;
		timer1.setText(Double.toString(time1) + " seconds");
		timer2.setText(Double.toString(time2) + " seconds");


		//Check collision btwn earth and asteroid without the gravity tractor
		if(masses1.get(2).x < (masses1.get(1).x + 6.37e6) && masses1.get(2).x > (masses1.get(1).x - 6.37e6)  
				&& masses1.get(2).y < (masses1.get(1).y + 6.37e6) && masses1.get(2).y > (masses1.get(1).y  - 6.37e6)){
			System.out.println("COLLISION!!! (no tractor)");
			if (collision1 == false){
				collisionframe1.setXY(masses1.get(1).x, masses1.get(1).y);
				frame1.addDrawable(collisionframe1);
			}
			collision1 = true;
		}

		//Check for collision btwn earth and asteroid with the gravity tractor
		if(masses2.get(2).x < (masses2.get(1).x + 6.37e6) && masses2.get(2).x > (masses2.get(1).x - 6.37e6)  
				&& masses2.get(2).y < (masses2.get(1).y + 6.37e6) && masses2.get(2).y > (masses2.get(1).y  - 6.37e6)){
			System.out.println("COLLISION!!! (with tractor)");
			//System.out.println("Made it inside");
			
			if (collision2 == false){
				collisionframe2.setXY(masses2.get(1).x, masses2.get(1).y);
				frame2.addDrawable(collisionframe2);
			}			
			collision2 = true;
		}


		//Move earth, asteroid, and sun in the simulation without the tractor
		for (int i = 0; i < masses1.size(); i++) {
			Planet p = masses1.get(i);
			p.newAx(sumOfForcesX(false, p, i));
			p.newAy(sumOfForcesY(false, p, i));
			p.newVX(p.Ax, timestep1);
			p.newVY(p.Ay, timestep1);
			p.newPositionX(p.Vx, timestep1); //moves X
			p.newPositionY(p.Vy, timestep1);	//moves Y
			trails1.get(i).addPoint(p.x, p.y); //adds point to trail	
		}

		//Move earth, asteroid, and sun in the simulation with the tractor
		for (int i = 0; i < masses2.size(); i++) {
			Planet p = masses2.get(i);
			p.newAx(sumOfForcesX(true, p, i));
			p.newAy(sumOfForcesY(true, p, i));
			p.newVX(p.Ax, timestep2);
			p.newVY(p.Ay, timestep2);
			p.newPositionX(p.Vx, timestep2); //moves X
			p.newPositionY(p.Vy, timestep2);	//moves Y
			trails2.get(i).addPoint(p.x, p.y); //adds point to trail
		}

		
		//tractor position, depends on what user set
		if((control.getString("Tractor position")).equals("above")){
			tractor.setXY(masses2.get(2).x, masses2.get(2).y+2e10);
		}
		else if((control.getString("Tractor position")).equals("below")){
			tractor.setXY(masses2.get(2).x, masses2.get(2).y-2e10);
		}
		else if((control.getString("Tractor position")).equals("right")){
			tractor.setXY(masses2.get(2).x+2e10, masses2.get(2).y);
		}
		else if((control.getString("Tractor position")).equals("left")){
			tractor.setXY(masses2.get(2).x-2e10, masses2.get(2).y);
		}
			

		//zoom in simulation without the tractor
		if(collision1 == false && masses1.get(1).x < masses1.get(2).x + 4e9 && masses1.get(1).x > masses1.get(2).x - 4e9
				&& masses1.get(1).y < masses1.get(2).y + 4e9 && masses1.get(1).y > masses1.get(2).y - 4e9){ //zoom if planets are within 4e9 of each other, except when they pass eachother it's only 100

			timestep1 = 100; //slow the motion to allow for a collision

			//new dimensions
			frame1.setPreferredMinMaxX(masses1.get(1).x - 1000000000, masses1.get(1).x + 1000000000); 
			frame1.setPreferredMinMaxY(masses1.get(1).y - 1000000000, masses1.get(1).y + 1000000000);
		}
		
		else{
			timestep1 = control.getDouble("Timestep"); //resets timestep
			frame1.setPreferredMinMax(2.5* -Math.pow(10, 11), 2.5*Math.pow(10, 11), 2.5* -Math.pow(10, 11), 2.5*Math.pow(10, 11)); //resets frame size
		}

		
		
		//zoom in simulation with the tractor
		if(collision2 == false && masses2.get(1).x < masses2.get(2).x + 1000 && masses2.get(1).x > masses2.get(2).x - 4e9 //1000 is for time's sake, bc they pass each other
				&& masses2.get(1).y < masses2.get(2).y + 4e9 && masses2.get(1).y > masses2.get(2).y - 4e9){ //zoom if planets are within 4e9 of each other

			timestep2 = 100; //slow the motion to allow for a collision

			//new dimensions
			frame2.setPreferredMinMaxX(masses2.get(1).x - 1000000000, masses2.get(1).x + 1000000000);
			frame2.setPreferredMinMaxY(masses2.get(1).y - 1000000000, masses2.get(1).y + 1000000000);
		}

		else{
			
			timestep2 = control.getDouble("Timestep"); //resets timestep
			frame2.setPreferredMinMax(2.5* -Math.pow(10, 11), 2.5*Math.pow(10, 11), 2.5* -Math.pow(10, 11), 2.5*Math.pow(10, 11)); // resets frame size
		}
	}



	//initial state of the simulation
	public void initialize(){ 

		timestep1 = control.getDouble("Timestep");
		timestep2 = control.getDouble("Timestep");


		//FRAME 1 SETUP

		//add sun and earth to the array
		masses1.add(new Planet(0, 0, 0, 0, 1.989 * Math.pow(10, 30))); //sun
		masses1.get(0).color = Color.yellow;
		masses1.get(0).pixRadius = 10;
		masses1.add(new Planet(1.5 * Math.pow(10, 11), 0, 0, 29780, 5.972* Math.pow(10, 24))); //earth
		masses1.get(1).color = Color.blue;
		masses1.add(new Planet(control.getDouble("Asteroid X"), control.getDouble("Asteroid Y"), control.getDouble("Asteroid Vx"), control.getDouble("Asteroid Vy"), control.getDouble("Asteroid Mass")));
		masses1.get(2).color = Color.gray;
		frame1.setPreferredMinMax(2.5* -Math.pow(10, 11), 2.5*Math.pow(10, 11), 2.5* -Math.pow(10, 11), 2.5*Math.pow(10, 11)); //frame size

		//add planets and trails to the frame
		for (int i = 0; i < masses1.size(); i++) {
			masses1.get(i).setXY(masses1.get(i).getX(), masses1.get(i).getY());
			frame1.addDrawable(masses1.get(i));
			trails1.add(new Trail()); 
			trails1.get(i).color = masses1.get(i).color;
			frame1.addDrawable(trails1.get(i));
		}

		//timer text box
		timer1.setXY(-2*Math.pow(10, 11), 2*Math.pow(10, 11));
		//frame1.addDrawable(timer1);

		frame1.setVisible(true);




		//FRAME 2 SETUP

		//add sun and earth to the second array
		masses2.add(new Planet(0, 0, 0, 0, 1.989 * Math.pow(10, 30))); //sun
		masses2.get(0).color = Color.yellow;
		masses2.get(0).pixRadius = 10;
		masses2.add(new Planet(1.5 * Math.pow(10, 11), 0, 0, 29780, 5.972* Math.pow(10, 24))); //earth
		masses2.get(1).color = Color.blue;
		masses2.add(new Planet(control.getDouble("Asteroid X"), control.getDouble("Asteroid Y"), control.getDouble("Asteroid Vx"), control.getDouble("Asteroid Vy"), control.getDouble("Asteroid Mass")));
		masses2.get(2).color = Color.gray;
		frame2.setPreferredMinMax(2.5* -Math.pow(10, 11), 2.5*Math.pow(10, 11), 2.5* -Math.pow(10, 11), 2.5*Math.pow(10, 11)); //frame size

		//add planets and trails to the second frame
		for (int i = 0; i < masses2.size(); i++) {
			masses2.get(i).setXY(masses2.get(i).getX(), masses2.get(i).getY());
			frame2.addDrawable(masses2.get(i));
			trails2.add(new Trail()); 
			trails2.get(i).color = masses2.get(i).color;
			frame2.addDrawable(trails2.get(i));
		}

		//timer text box
		timer2.setXY(-2*Math.pow(10, 11), 2*Math.pow(10, 11));
		//frame2.addDrawable(timer2);

		frame2.addDrawable(tractor);
		
		frame2.setVisible(true);
	}

	//set values
	public void reset(){
		control.setValue("Timestep", 100000); 
		control.setValue("Asteroid Mass", 5e7);
		control.setValue("Asteroid X", 0);
		control.setValue("Asteroid Y", 5e10);
		control.setValue("Asteroid Vx", 65227.22); //COLLISION SPOT
		control.setValue("Asteroid Vy", 0);
		control.setValue("Tractor position", "above");
		control.setValue("Tractor mass", 5e7);

	}


	/**
	 * Calculates the sum of all the forces in the universe on a mass in the x-direction
	 * @param tractor	whether or not this is the simulation with the tractor
	 * @param mass1 	the planet
	 * @param index		which planet it is in the array of planets
	 * @return sum 		the sum of the forces in the x direction
	 */
	public double sumOfForcesX(boolean tractor, Planet mass1, int index){
		double sum = 0;
		ArrayList<Planet> masses = new ArrayList<Planet>();
		if (tractor == false) masses = masses1;
		if (tractor == true) masses = masses2;


		for (int i = 0; i < masses.size(); i++) {
			if(i!=index){ //all except the mass itself

				if(mass1.positiveX(masses.get(i))){
					sum += mass1.forceOfGravity(masses.get(i)).componentX(); //adds the x-component of the specific mass (index i)
				}

				else{
					sum -= mass1.forceOfGravity(masses.get(i)).componentX();
				}

			}
		}

		
		//change in gravity due to tractor to the right or to the left of the asteroid
		if(tractor == true && control.getString("Tractor position").equals("right") && index == 2){ //only the asteroid, only if tractor is to the right
			sum+= (6.6726*(Math.pow(10, -11)) * control.getDouble("Asteroid Mass") * control.getDouble("Tractor mass")) / (250*250); //gravity formula, distance 250m (appropriate for a 200 m diameter asteroid)
		}

		else if(tractor == true && control.getString("Tractor position").equals("left") && index == 2){ //only the asteroid, only if tractor is to the left
			sum-= (6.6726*(Math.pow(10, -11)) * control.getDouble("Asteroid Mass") * control.getDouble("Tractor mass")) / (250*250); //gravity formula, distance 250m (appropriate for a 200 m diameter asteroid)
		}

		return sum;		
	}

	/**
	 * Calculates the sum of all the forces in the universe on a mass in the y-direction
	 * @param tractor	whether or not this is the simulation with the tractor
	 * @param mass1 	the planet
	 * @param index		which planet it is in the array of planets
	 * @return sum 		the sum of the forces in the y-direction
	 */
	public double sumOfForcesY(boolean tractor, Planet mass1, int index){
		double sum = 0;
		ArrayList<Planet> masses = new ArrayList<Planet>();
		if (tractor == false) masses = masses1;
		if (tractor == true) masses = masses2;

		for (int i = 0; i < masses.size(); i++) {
			if(i!=index){ //all except the mass itself

				if(mass1.positiveY(masses.get(i))){
					sum += mass1.forceOfGravity(masses.get(i)).componentY(); //adds the y-component of the specific mass (index i)
				}
				else{
					sum -= mass1.forceOfGravity(masses.get(i)).componentY();
				}
			}
		}

		//change in gravity due to tractor above or below the asteroid
		if(tractor == true && control.getString("Tractor position").equals("above") && index == 2){ //only the asteroid, only if tractor is in above
			sum+= (6.6726*(Math.pow(10, -11)) * control.getDouble("Asteroid Mass") *control.getDouble("Tractor mass")) / (250*250); //gravity formula, distance 250m (appropriate for a 200 m diameter asteroid)
		}

		else if(tractor == true && control.getString("Tractor position").equals("below") && index == 2){ //only the asteroid, only if tractor is below
			sum-= (6.6726*(Math.pow(10, -11)) * control.getDouble("Asteroid Mass") *control.getDouble("Tractor mass")) / (250*250); //gravity formula, distance 250m (appropriate for a 200 m diameter asteroid)
		}

		return sum;		
	}

}