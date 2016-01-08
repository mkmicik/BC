package tankbattle.client.stub;


import java.util.HashMap;
import java.util.Map;
import java.util.Date;

import commands.Commands;
import commands.Commands.FireCommand;
import tankbattle.client.stub.GameState;
import tankbattle.client.stub.GameState.Tank;

import com.google.gson.*;

public class TurretController {
	private static TurretController _instance;
	private Map<String, Date> lastFired;
	private static GameState gamestate;
	private static Communication comm;
	private static String clientToken;
	
	public TurretController() {
		lastFired = new HashMap<String, Date>();
	}
	
	private boolean canFire(Tank tank) {
		if (!lastFired.containsKey(tank.id)) {
			return true;
		} else {
			Date now = new Date();
			Date last = lastFired.get(tank.id);
			long seconds = (now.getTime()-last.getTime())/1000;
			if (tank.type.equals("TankSlow")) {
				return seconds > 3;
			} else {
				return seconds > 5;
			}
		}
	}

	public TurretController getInstance() {
		if (_instance == null) {
			_instance = new TurretController();
		}
		return _instance;
	}
	public static TurretController getInstance(Communication c, String ct, GameState gs) {
		if (_instance == null) {
			_instance = new TurretController();
		}
		gamestate = gs;
		comm = c;
		clientToken = ct;
		return _instance;
	}
	
	public void update(){
		
		Gson gson = new Gson();
	// For each of our tanks
		// If its a slow tank aim for where the target currently is.
		// Find location of target if our tank is the origin.
		/*System.out.println("Target Tank x = " + target.position[0]);
		System.out.println("Target Tank y = " + target.position[1]);
		System.out.println("");*/
		
		for(int i = 0; i < gamestate.getFriendlyTanks().length; i++)
		{
			Tank currentTank = gamestate.getFriendlyTanks()[i];
			Tank target = gamestate.GetNearestEnemy(currentTank);
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
			if (angleToTarget < 0) {
				angleToTarget = 2*Math.PI + angleToTarget;
			}
			Commands.TurretRotateCommand cmd = null;
			Commands.FireCommand firecmd = null;
			if (canFire(currentTank) && Math.abs(currentTank.turret - angleToTarget) < 0.05) {
				// Need to check if shoot is off cooldown first.
				firecmd = new Commands.FireCommand(clientToken, currentTank.id);
				lastFired.put(currentTank.id, new Date());
			} else {
				if ((currentTank.turret - angleToTarget) > 0) {
					//send a rotate counterclockwise command of tank.angle + angletoTarget
					cmd = new Commands.TurretRotateCommand(clientToken, currentTank.id, Commands.Direction.CW, currentTank.turret - angleToTarget);
				} else {
					//send a rotate clockwise command of tank.angle - angletotarget
					cmd = new Commands.TurretRotateCommand(clientToken, currentTank.id, Commands.Direction.CCW, angleToTarget - currentTank.turret);
				}
			}
			String json_cmd;
			if (firecmd != null) {
				json_cmd = gson.toJson(firecmd);
			} else {
				json_cmd = gson.toJson(cmd);
			}
			String response = comm.send(json_cmd);
			System.out.println(response);

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