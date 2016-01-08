package tankbattle.client.stub;

import tankbattle.client.stub.GameState.Map.Terrain;
import tankbattle.client.stub.GameState.Map.Terrain.BoundingBox;
import tankbattle.client.stub.GameState.Tank.Projectile;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

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
			protected BoundingBox boundingBox;
			
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
	
	public Tank getNearestEnemy(Tank t) {
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
	
	private ArrayList<Terrain> getProjectileImpassableTerrain() {
		ArrayList<Terrain> solids = new ArrayList<Terrain>();
		for (Terrain t : map.terrain) {
			if (t.type.equals("SOLID")) {
				solids.add(t);
			}
		}
		return solids;
	}
	/*
	 * 1. Check if projectile is heading towards us
	 * 2. Check if there are obstacles between us and the projectile
	 * 3. Check if the projectile is in 
	 */
	public boolean inDanger(Projectile projectile, Tank target) {
		if (inOurDirection(projectile, target) 
				&& lineOfSight(projectile.position, target.position) 
				&& inRange(projectile, target)) {
			return true;
		}
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
	
	/* Acquires the best enemy to target */
	public Tank acquireTarget(Tank tank) {
		
		Tank[] enemies = getEnemyTanks();
		Arrays.sort(enemies, tankComparator(tank.position[0], tank.position[1]));
		
		for (Tank enemy : enemies) {
			if (lineOfSight(tank.position, enemy.position)) {
				return enemy;
			}
		}
		return getNearestEnemy(tank);
	}
	
	public boolean canShoot(Tank shooter, Tank target) {
		if (lineOfSight(shooter.position, target.position)) {
			return true;
		}
		return false;
	}
	
	/* Used to sort tanks by shortest distance from the given tank */
	private static Comparator<Tank> tankComparator(double x, double y)
    {
        final Point2D finalP = new Point2D.Double(x, y);
        return new Comparator<Tank>()
        {
            @Override
            public int compare(Tank t0, Tank t1)
            {
            	
            	Point2D p0 = new Point2D.Double(t0.position[0], t0.position[1]);
            	Point2D p1 = new Point2D.Double(t1.position[0], t1.position[1]);
                double ds0 = p0.distanceSq(finalP);
                double ds1 = p1.distanceSq(finalP);
                return Double.compare(ds0, ds1);
            }

        };
    }
	
	/* Checks if a line of sight exists between the two positions */
	public boolean lineOfSight(double[] shooter, double[] target) {
		Line2D lineOfSight = new Line2D.Double(shooter[0], shooter[1], 
				target[0], target[1]);
		
		ArrayList<Terrain> solids = getProjectileImpassableTerrain();
		for (Terrain t : solids) {
			Rectangle r = new Rectangle(t.boundingBox.corner[0],t.boundingBox.corner[1],t.boundingBox.size[0],t.boundingBox.size[1]);
			if (lineOfSight.intersects(r)) {
				return false;
			}
		}
		Tank[] friendlies = getFriendlyTanks();
		int coll_rad;
		for (Tank t : friendlies) {
			coll_rad = t.collisionRadius;
			Rectangle r = new Rectangle((int)t.position[0] - coll_rad, (int)t.position[1]-coll_rad,
					(int)t.position[0] + coll_rad, (int)t.position[1]+coll_rad);
			if (lineOfSight.intersects(r)) {
				return false;
			}
		}
		return true;
	}
}
