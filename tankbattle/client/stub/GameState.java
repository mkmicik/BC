package tankbattle.client.stub;

import tankbattle.client.stub.GameState.Map.Terrain;
import tankbattle.client.stub.GameState.Map.Terrain.BoundingBox;
import tankbattle.client.stub.GameState.Tank.Projectile;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class GameState {
	
	public double timestamp;
	public Player[] players;
	public String match_id;
	public String comm_type;
	
	public Map map;
	public double timeRemaining;
	
	class Tank {
		protected double position[];
		protected String id;
		protected int collisionRadius;
		protected int speed;
		
		protected Projectile projectiles[];
		
		protected int hitRadius;
		protected double tracks;
		protected String type;
		protected int health;
		protected boolean alive;
		protected double turret;
		
		class Projectile {
			protected String id;
			protected double position[];
			protected double direction;
			protected int speed;
			protected int damage;
			protected double range;
		}
	}
	
	class Player {
		protected String name;
		protected int score;
		protected Tank tanks[];
	}
	
	class Map {
		protected Terrain terrain[];
		protected int size[];
		
		class Terrain {
			protected String type;
			protected BoundingBox bounding[];
			
			class BoundingBox {
				protected int corner[];
				protected int size[];
			}
		}
	}
	
	/* Get all my tanks */
	public Tank[] getFriendlyTanks() {
		for (Player p : players) {
			if (p.name.equals("BeigeCardigan")) {
				return p.tanks;
			}
		}
		// should never reach this, but if there are no tanks we return nothing
		return new Tank[0];
	}
	
	/* Get all the enemies tanks */
	public Tank[] getEnemyTanks() {
		for (Player p : players) {
			if (!p.name.equals("BeigeCardigan")) {
				return p.tanks;
			}
		}
		// should never reach this, but if there are no tanks we return nothing
		return new Tank[0];
	}
	
	/* Cardinal - TODO: change to real distance around obstacles and such */
	private double getDistance(Tank t1, Tank t2) {
		double x_dist = Math.abs(t1.position[0] - t2.position[0]);
		double y_dist = Math.abs(t1.position[1] - t2.position[1]);
		
		return Math.sqrt(square(x_dist)+square(y_dist));
	}
	private double square(double d) { return d*d; }
	
	public Tank GetNearestEnemy(Tank t) {
		Tank enemies[] = getEnemyTanks();
		double min_dist  = Double.MAX_VALUE;
		Tank nearest = null;
		for (Tank enemy : enemies) {
			double dist;
			if ((dist = getDistance(t, enemy)) < min_dist) {
				min_dist = dist;
				nearest = enemy;
			}
		}
		return nearest;
	}
	
	private BoundingBox[] getProjectileImpassableTerrain(Map m) {
		for (Terrain t : m.terrain) {
			if (t.type.equals("SOLID")) {
				return t.bounding;
			}
		}
		return new BoundingBox[0];
	}
	/*
	 * 1. Check if projectile is heading towards us
	 * 2. Check if there are obstacles between us and the projectile
	 * 3. Check if the projectile is in 
	 */
	public boolean inDanger(Map m, Projectile projectile, Tank target) {
		System.out.println("ID: " + projectile.id + " Range: " + projectile.range);
		
		System.out.println("Projectile Pos: " + projectile.position.length);
		System.out.println("Target Pos: " + target.position.length);
		
//		if (inOurDirection(projectile, target) 
//				&& lineOfSight(map, projectile.position, target.position) 
//				&& inRange(projectile, target)) {
//			return true;
//		}
		return false;
	}
	
	private boolean inOurDirection(Projectile projectile, Tank target) {
		double relativeX = target.position[0] - projectile.position[0];
		double relativeY = target.position[1] - projectile.position[1];
		double angleToTarget = (Math.atan2(relativeY, relativeX));
		if (angleToTarget < 0) {
			angleToTarget = 2*Math.PI + angleToTarget;
		}
		
		if (Math.abs(angleToTarget - projectile.direction) < 0.05) {
			return true;
		}
		return false;
	}
	
	private boolean inRange(Projectile projectile, Tank target) {
		return true;
	}
	/*
	public boolean lineOfSight(Tank shooter, Tank target) {
		Line2D lineOfSight = new Line2D.Double(shooter.position[0], shooter.position[1], 
				target.position[0], target.position[1]);
		
		BoundingBox[] solids = getProjectileImpassableTerrain();
		for (BoundingBox bb : solids) {
			Rectangle r = new Rectangle(bb.corner[0],bb.corner[1],bb.size[0],bb.size[1]);
			if (lineOfSight.intersects(r)) {
				return false;
			}
		}
		return true;
	}
	*/
	public boolean lineOfSight(Map map, double[] shooter, double[] target) {
		Line2D lineOfSight = new Line2D.Double(shooter[0], shooter[1], 
				target[0], target[1]);
		
		BoundingBox[] solids = getProjectileImpassableTerrain(map);
		for (BoundingBox bb : solids) {
			Rectangle r = new Rectangle(bb.corner[0],bb.corner[1],bb.size[0],bb.size[1]);
			if (lineOfSight.intersects(r)) {
				return false;
			}
		}
		return true;
	}
}
