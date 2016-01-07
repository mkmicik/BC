package tankbattle.model;

public abstract class Tank {

	protected String id;
	
	protected int pos_x;
	protected int pos_y;
	protected double turretAngle;
	protected double tracksAngle;
	
	protected double movementSpeed;
	protected double trackRotation;
	protected double turretRotation;
	protected double fireRate;
	protected double health;
	
	/* Getters and Setters */
	public int getPos_x() {
		return pos_x;
	}
	public int getPos_y() {
		return pos_y;
	}
	public void setPos_x(int pos_x) {
		this.pos_x = pos_x;
	}
	public void setPos_y(int pos_y) {
		this.pos_y = pos_y;
	}
	public double getTurretAngle() {
		return turretAngle;
	}
	public void setTurretAngle(double turretAngle) {
		this.turretAngle = turretAngle;
	}
	public double getTracksAngle() {
		return tracksAngle;
	}
	public void setTracksAngle(double tracksAngle) {
		this.tracksAngle = tracksAngle;
	}
	public double getTurretRotation() {
		return turretRotation;
	}
	public void setTurretRotation(double turretRotation) {
		this.turretRotation = turretRotation;
	}
	public double getMovementSpeed() {
		return movementSpeed;
	}
	public void setMovementSpeed(double movementSpeed) {
		this.movementSpeed = movementSpeed;
	}
	
	class Projectile {
		private double speed;
		private double damage;
		
		public Projectile() {
			speed = 30;
			damage = 100;
		}
	}

}
