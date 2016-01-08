package tankbattle.model;

public class Config {
	private static Config _instance;
	private Config() {}
	
	public static Config getInstance() {
		if (_instance == null) {
			_instance = new Config();
		}
		return _instance;
	}
	
	public String TeamName;
	public String Password;
	public String MatchToken;
	public String GameServerIP;
}
