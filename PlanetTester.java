package OrbitalCKK;

public class PlanetTester {

	public static void main(String[] args) {
		Planet claire = new Planet(2,3,0,0,1);
		Planet lily = new Planet(7,5,0,0,1);

		System.out.println(claire.distance(lily)); //distance works
		System.out.println(claire.angle(lily)); //angle works

		Force tom = new Force(Math.sqrt(2), Math.PI / 4); //45-45-90, 1-1-root2
		System.out.println(tom.componentX()); //works
		System.out.println(tom.componentY()); //works, rounding issue

		Planet hello = new Planet(0,5,0,0,2);
		Planet goodbye = new Planet(0,0,0,0,6);
		Planet hola = new Planet(0,-5, 0, 0, 6);
		System.out.println("force from goodbye on hello: "+hello.forceOfGravity(goodbye).magnitude); //force eq works
		System.out.println("force from hola on hello: "+hello.forceOfGravity(hola).magnitude); 
		System.out.println("force from hello on goodbye: "+goodbye.forceOfGravity(hello).magnitude);
		
		
		Orbital world = new Orbital();
		world.masses.add(hello);
		world.masses.add(goodbye);
		world.masses.add(hola);
		System.out.println("forces in the X on hello: " + world.sumOfForcesX(hello, 0)); //works
		System.out.println("forces in the Y on hello: " + world.sumOfForcesY(hello, 0)); //works


	}
}
