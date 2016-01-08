package tankbattle.client.stub;

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
			protected double position[];
			protected String id;
			protected int speed;
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
	
	//private boolean lineOfSight(Tank shooter, Tank target) {
		
	//}
	
	//public boolean canFire(Tank shooter, Tank target) {
		
	//}
		
		

}
