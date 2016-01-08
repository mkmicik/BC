package commands;

public class Commands {
	
	private static String ClientToken;
	
	public Commands(String ClientToken) {
		this.ClientToken = ClientToken;
	}
	
	public enum Direction {
		CW,
		CCW
	}

	public static class TurretRotateCommand {
		public TurretRotateCommand(String client_token, String tank_id, Direction direction, double rads) {
			this.client_token = client_token;
			this.tank_id = tank_id;
			this.direction = direction;
			this.rads = rads;
		}
		private String tank_id;
		private String comm_type = "ROTATE_TURRET";
		private Direction direction;
		private double rads;
		private String client_token;
	}
	public static class FireCommand {
		public FireCommand(String client_token, String tank_id) {
			this.client_token = client_token;
			this.tank_id = tank_id;
		}
		private String tank_id;
		private String comm_type = "FIRE";
		private String client_token;
	}
	
}
