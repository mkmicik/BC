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
			protected int position[];
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
