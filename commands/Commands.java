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
	public enum MoveDirection {
		FWD,
		REV
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
	public static class MoveCommand {
        public MoveCommand(String client_token, String tank_id, MoveDirection direc, double dist) {
            this.client_token = client_token;
            this.tank_id = tank_id;
            this.direction = direc;
            this.distance = dist;
        }
        private String tank_id;
        private String comm_type = "MOVE";
        private String client_token;
        private MoveDirection direction;
        private double distance;
    }
	/*
	    "tank_id" : "eb49f487-92dc-4f66-ac83-91ae04a4cc16",
	    "comm_type" : "ROTATE",
	    "direction" : "CW",
	    "rads" : 3.14,
	    "client_token" : "dc8d75a6-448d-4108-8bfa-52470b97f35f"
	   */
	public static class RotateCommand {
        public RotateCommand(String client_token, String tank_id, Direction direc, double rads) {
            this.client_token = client_token;
            this.tank_id = tank_id;
            this.direction = direc;
            this.rads = rads;
        }
        private String tank_id;
        private String comm_type = "ROTATE";
        private String client_token;
        private Direction direction;
        private double rads;
    }
	
}
