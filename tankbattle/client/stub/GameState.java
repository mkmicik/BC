package tankbattle.client.stub;

public class GameState {
	
	public double timestamp;
	public Player[] players;
	public String match_id;
	public String comm_type;
	
	public Map map;
	public double timeRemaining;
	
	public Tank[] getFriendlyTanks() {
		for (Player p : players) {
			System.out.println(p.name);
			if (p.name.equals("BeigeCardigan")) {
				return p.tanks;
			}
		}
		// should never reach this, but if there are no tanks we return nothing
		return new Tank[0];
	}
	
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

}
