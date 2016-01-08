package tankbattle.client.stub;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import tankbattle.client.stub.GameState.Tank;
import tankbattle.client.stub.GameState.Tank.Projectile;

import com.google.gson.Gson;

import commands.Commands;

public class MovementController {
	private static MovementController _instance;
	private static GameState gamestate;
	private static Communication comm;
	private static String clientToken;
	
	private MovementController() {
	}
	
	public MovementController getInstance() {
		if (_instance == null) {
			_instance = new MovementController();
		}
		return _instance;
	}
	public static MovementController getInstance(Communication c, String ct, GameState gs) {
		if (_instance == null) {
			_instance = new MovementController();
		}
		gamestate = gs;
		comm = c;
		clientToken = ct;
		return _instance;
	}
	
	public void update() {
		Gson gson = new Gson();
		
		Tank[] friendlies = gamestate.getFriendlyTanks();
		Tank[] enemies = gamestate.getEnemyTanks();
		
		for(int i = 0; i < friendlies.length; i++)
		{
			Tank currentTank = friendlies[i];
			if (!currentTank.alive) {
				continue;
			}
			
			for (Tank enemy : enemies) {
				for (Projectile proj : enemy.projectiles) {
					if (willCollide())
				}
			}
			
			
			
			
		}							
	}
	
}
