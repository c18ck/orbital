package OrbitalCKK;
import java.awt.Color;
import java.util.ArrayList;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.TextBox;
import org.opensourcephysics.display.Trail;
import org.opensourcephysics.frames.PlotFrame;

/**
 * 1st LAW PROOF: purple dotted line is an ellipse, wait for the orbit to go around once,
 * then the right ellipse will show up and you can watch the orbit follow that trail
 * 
 * 2nd LAW PROOF: area per timestep prints as it goes around, it stays the same pretty much
 * 
 * 3rd LAW PROOF: graph submitted 
 * 
 * @author Claire Keenan-Kurgan
 *
 */


public class Orbital extends AbstractSimulation{

	ArrayList<Planet> masses = new ArrayList<Planet>(); //masses
	ArrayList<Trail> trails = new ArrayList<Trail>();	//trails
	
	PlotFrame frame = new PlotFrame("X", "Y", "Orbital");	//frame
	TextBox timer = new TextBox("Timer"); //timer text box
	
	double timestep;
	int counter = 0;
	double beforex = 0;
	double beforey = 0;
	double a, b = 0;
	boolean ellipsedrawn = false;

	//main
	public static void main(String[] args) {
		SimulationControl.createApp(new Orbital());
	}

	//runs simulation every .01 seconds
	protected void doStep() {

		timer.setText(Double.toString(counter*timestep) + " seconds");
		beforex = masses.get(1).x;
		beforey =  masses.get(1).y;

		for (int i = 0; i < masses.size(); i++) {
			Planet p = masses.get(i);			
			p.newAx(sumOfForcesX(p, i));
			p.newAy(sumOfForcesY( p, i));
			p.newVX(p.Ax, timestep);
			p.newVY(p.Ay, timestep);
			p.newPositionX(p.Vx, timestep); //moves X
			p.newPositionY(p.Vy, timestep);	//moves Y
			trails.get(i).addPoint(p.x, p.y); //adds point to trail	
			
		}

		//proves kepler's second law
		if (counter % 15 == 0){ //every 15 times the dostep runs
			timestepArea(beforex,beforey,masses.get(1).x, masses.get(1).y);
		}
		
		
		//calls ellipse
		if(ellipsedrawn == false && counter > 50 && masses.get(1).x > 0.99*(control.getDouble("Earth X")) && masses.get(1).x < 1.01*control.getDouble("Earth X")){
			drawEllipse(trails.get(1).getYMax(), trails.get(1).getYMin(), trails.get(1).getXMax(), trails.get(1).getXMin());
			ellipsedrawn = true;
		}

		counter++;

	}

	//initial state of the simulation
	public void initialize(){ 

		timestep = control.getDouble("Timestep");

		//add sun and earth to the array
		masses.add(new Planet(control.getDouble("Sun X"), control.getDouble("Sun Y"), control.getDouble("Sun Vx"), control.getDouble("Sun Vy"), control.getDouble("Sun Mass")));
		masses.get(0).color = Color.yellow;
		masses.get(0).pixRadius = 10;
		masses.add(new Planet(control.getDouble("Earth X"), control.getDouble("Earth Y"), control.getDouble("Earth Vx"), control.getDouble("Earth Vy"), control.getDouble("Earth Mass")));
		masses.get(1).color = Color.blue;

		//solarSystem(); //adds rest of solar system

		frame.setPreferredMinMax(2.5* -Math.pow(10, 11), 2.5*Math.pow(10, 11), 2.5* -Math.pow(10, 11), 2.5*Math.pow(10, 11)); //frame size

		//add planets and trails to the frame
		for (int i = 0; i < masses.size(); i++) {
			masses.get(i).setXY(masses.get(i).getX(), masses.get(i).getY());
			frame.addDrawable(masses.get(i));
			trails.add(new Trail()); 
			trails.get(i).color = masses.get(i).color;
			frame.addDrawable(trails.get(i));
		}

		//timer text box
		timer.setXY(-2*Math.pow(10, 11), 2*Math.pow(10, 11));
		frame.addDrawable(timer);

		frame.setVisible(true);
	}

	//set values
	public void reset(){
		control.setValue("Timestep", 200000); 
		control.setValue("Sun Mass", (1.989 * Math.pow(10, 30))); //sun
		control.setValue("Sun X", 0);
		control.setValue("Sun Y", 0);
		control.setValue("Sun Vx", 0);
		control.setValue("Sun Vy", 0);
		control.setValue("Earth Mass", (5.972* Math.pow(10, 24))); //earth
		control.setValue("Earth X", (1.5 * Math.pow(10, 11))); //earth sun distance
		control.setValue("Earth Y", 0);
		control.setValue("Earth Vx", 0);
		control.setValue("Earth Vy", -30000); //earth tangential velocity
	}

	/** 
	 * Proves kepler's first law that planets orbit in ellipses
	 * Calculates a and b values from the max and min values of the plotted orbital trail
	 * Then draws the ellipse
	 * 
	 */
	public void drawEllipse(double ymax, double ymin, double xmax, double xmin){

		Trail ellipsebottom = new Trail();
		ellipsebottom.color = Color.magenta;
		ellipsebottom.setDashedStroke(3, 10);
		
		Trail ellipsetop = new Trail();
		ellipsetop.color = Color.magenta;
		ellipsetop.setDashedStroke(3, 10);
		
		double a = (ymax - ymin)/2; //y axis
		double b = (xmax - xmin)/2; //x axis

		double centerx = xmax - b;
		double centery = ymax - a;

		double differencex = centerx; //how far the ellipse has to be moved over (left or right)
		double differencey = centery; //how far the ellipse has to be moved over (up or down)

		//GRAPH
		double y = 0;
		for (double i = -b + differencex; i <= b + differencex; i+= 1E8) {
			y = a  *   Math.sqrt( 1 - (Math.pow((i - differencex), 2))/Math.pow(b, 2)    )  + differencey; //formula of upper part of an ellipse
			ellipsetop.addPoint(i, y);
			//frame.append(1, i, y);
		}

		for (double i = -b + differencex ; i <= b + differencex; i+= 1E8) {
			y = -a  *   Math.sqrt( 1 - (Math.pow((i - differencex), 2))/Math.pow(b, 2)    ) + differencey; //formula of lower part of an ellipse
			ellipsebottom.addPoint(i, y);
			//frame.append(1, i, y);
		}

		frame.addDrawable(ellipsetop);
		frame.addDrawable(ellipsebottom);	

	}

	/**
	 * Proves Kepler's second law
	 * Takes in the position of the planet before a timestep, and then after a timestep
	 * Assuming the sun's position is zero (gets a good/consistent estimate)
	 * Uses Heron's law to find triangle area, and since the arc is so small,
	 * 	the triangle area is very close to what the slice area would be
	 * 
	 * @param xi		initial x-coor
	 * @param yi		initial y-coor
	 * @param xi		final x-coor
	 * @param yi		final y-coor
	 */
	public void timestepArea(double xi, double yi, double xf, double yf){

		double A = Math.sqrt( Math.pow( (xf-0) , 2) + Math.pow( (yf - 0) , 2) ); //distance between planet final and sun
		double B = Math.sqrt( Math.pow( (xf-xi) , 2) + Math.pow( (yf-yi) , 2) ); //distance between planet final and planet initial
		double C = Math.sqrt( Math.pow( (xi-0) , 2) + Math.pow( (yi-0) , 2) ); //distance between planet initial and sun

		//HERON'S FORMULA
		double S = (A+B+C)/2;
		double area = Math.sqrt( S*(S-A)*(S-B)*(S-C) );

		System.out.println("area per timestep: " + area);

	}

	/**
	 * Adds the planets in our solar system to the frame
	 * 	Assumes sun and earth were added as 0 and 1
	 */
	public void solarSystem(){

		//OTHER PLANETS
		masses.add(new Planet(2.278E11, 0, 0, -24077, 6.4171E23)); //mars
		masses.add(new Planet(1.082E11, 0, 0, -35020, 4.8675E24)); //venus
		masses.get(3).color =  Color.cyan;
		masses.add(new Planet(5.7904E10, 0, 0, -47362, 3.3011E23)); //mercury
		masses.get(4).color = Color.lightGray;
		masses.add(new Planet(7.786E11, 0, 0, -13070, 1.8986E27)); //jupiter
		masses.get(5).color = Color.cyan;
		masses.add(new Planet(1.429E12, 0, 0, -9690, 5.6836E26)); //saturn
		masses.get(6).color = Color.black;
		masses.add(new Planet(2.871E12, 0, 0, -6800, 8.681E25)); //uranus
		masses.get(7).color = Color.cyan;
		masses.add(new Planet(4.498E12, 0, 0, -5430, 1.0243E26)); //neptune
		masses.get(8).color = Color.green;

	}


	/**
	 * Calculates the sum of all the forces in the universe on a mass in the x-direction
	 * @param mass1 	the planet
	 * @param index		which planet it is in the array of planets
	 * @return sum 		the sum of the forces in the x direction
	 */
	public double sumOfForcesX(Planet mass1, int index){
		double sum = 0;
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

		return sum;		
	}

	/**
	 * Calculates the sum of all the forces in the universe on a mass in the y-direction
	 * @param mass1 	the planet
	 * @param index		which planet it is in the array of planets
	 * @return sum 		the sum of the forces in the y-direction
	 */
	public double sumOfForcesY(Planet mass1, int index){
		double sum = 0;
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
		return sum;		
	}
}
