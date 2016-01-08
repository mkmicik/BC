package tankbattle.client.stub;


import commands.Commands;
import tankbattle.client.stub.GameState;
import tankbattle.client.stub.GameState.Tank;


public class TurretController {
	private  TurretController _instance;
	public GameState gamestate;

	public TurretController(GameState gs) {
		gamestate = gs;
	}
	
	public TurretController getInstance() {
		if (_instance == null) {
			_instance = new TurretController(null);
		}
		return _instance;
	}
	public TurretController getInstance(GameState gs) {
		if (_instance == null) {
			_instance = new TurretController(gs);
		}
		gamestate = gs;
		return _instance;
	}
	
	public void update(){
	// For each of our tanks
		// If its a slow tank aim for where the target currently is.
		// Find location of target if our tank is the origin.
		Tank target = gamestate.players[1].tanks[0];
		/*System.out.println("Target Tank x = " + target.position[0]);
		System.out.println("Target Tank y = " + target.position[1]);
		System.out.println("");*/

		for(int i = 0; i < gamestate.players[0].tanks.length; i++)
		{
			Tank currentTank = gamestate.players[0].tanks[i];
			double angleToTarget;
			if (currentTank.type.equals("TankSlow")){
				double relativeX = target.position[0] - currentTank.position[0];
				double relativeY = target.position[1] - currentTank.position[1];
				angleToTarget = (Math.atan2(relativeY, relativeX));
//				System.out.println("Current Tank x = " + currentTank.position[0]);
//				System.out.println("Current Tank y = " + currentTank.position[1]);
//				System.out.println("Relative X = " + relativeX);
//				System.out.println("Relative X = " + relativeY);
//				System.out.println("Current angle = " + currentTank.turret);
				System.out.println("Slow tank angle = \n" + angleToTarget);
//				System.out.println("");
				
			} else {
				angleToTarget = calculateLeadLocation(currentTank, target);
//				System.out.println("Current Tank x = " + currentTank.position[0]);
//				System.out.println("Current Tank y = " + currentTank.position[1]);
//				System.out.println("Current angle = " + currentTank.turret);
				System.out.println("Fast Tank angle = " + angleToTarget);
//				System.out.println("");

			}
			if ((currentTank.turret - angleToTarget) > 3.14) {
				//send a rotate counterclockwise command of tank.angle + angletoTarget
				Commands.TurretRotateCommand cmd = new Commands.TurretRotateCommand(currentTank.id, Commands.Direction.CCW, currentTank.turret + angleToTarget);
			} else {
				//send a rotate clockwise command of tank.angle - angletotarget
				Commands.TurretRotateCommand cmd = new Commands.TurretRotateCommand(currentTank.id, Commands.Direction.CW, currentTank.turret - angleToTarget);
			}
		}							
	}

// http://stackoverflow.com/questions/2248876/2d-game-fire-at-a-moving-target-by-predicting-intersection-of-projectile-and-u
/*
* Return the firing solution for a projectile starting at 'src' with
 * velocity 'v', to hit a target, 'dst'.
 *
 * @param Object src position of shooter
 * @param Object dst position & velocity of target
 * @param Number v   speed of projectile
 * @return Object Coordinate at which to fire (and where intercept occurs)
 *
 * E.g.
 * >>> intercept({x:2, y:4}, {x:5, y:7, vx: 2, vy:1}, 5)
 * = {x: 8, y: 8.5}
 */
	public double calculateLeadLocation(Tank currentTank, Tank target) {
					
		double relativeX = target.position[0] - currentTank.position[0];
		double relativeY = target.position[1] - currentTank.position[1];
		
		double targetVelocityX = Math.cos(target.tracks) * target.speed;
		double targetVelocityY = Math.sin(target.tracks) * target.speed;

		// Get quadratic equation components
		double a = targetVelocityX*targetVelocityX + targetVelocityY*targetVelocityY - target.speed*target.speed;
		double b = 2 * (targetVelocityX * relativeX + targetVelocityY * relativeY);
		double c = relativeX*relativeX + relativeY*relativeY;    

		// Solve quadratic
		double[] ts = quad(a, b, c); // See quad(), below

		double interceptLocationX = 0;
    	double interceptLocationY = 0;
    	
		// Find smallest positive solution
		if (ts != null) {
		    double t0 = ts[0];
		    double t1 = ts[1];
		    double t = Math.min(t0, t1);
		    if (t < 0)
		    	t = Math.max(t0, t1);    
		    if (t > 0) {
		    	interceptLocationX = target.position[0] + targetVelocityX*t;
		    	interceptLocationY = target.position[1] + targetVelocityY*t;
		    }
		}

		// get angle between your location and the new interception location
		double relativeAngleX = interceptLocationX - currentTank.position[0];
		double relativeAngleY = interceptLocationY - currentTank.position[1];
		return Math.atan2(relativeAngleY, relativeAngleX);
	}


	/**
	 * Return solutions for quadratic
	 */
	public double[] quad(double a, double b, double c) {
	  double [] sol = null;
	  if (Math.abs(a) < 1e-6) {
	    if (Math.abs(b) < 1e-6) {
	      sol = (Math.abs(c) < 1e-6) ? new double[]{0,0} : null;
	    } else {
	      sol = new double[]{(-c/b), (-c/b)};
	    }
	  } else {
	    double disc = b*b - 4*a*c;
	    if (disc >= 0) {
	      disc = Math.sqrt(disc);
	      a = 2*a;
	      sol = new double[]{(-b-disc)/a, (-b+disc)/a};
	    }
	  }
	  return sol;
	}
}