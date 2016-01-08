package tankbattle.client.stub;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import commands.Commands;


import tankbattle.client.stub.GameState.Tank;
import tankbattle.client.stub.GameState.Tank.Projectile;

import com.google.gson.Gson;

import commands.Commands;

public class MovementController {
	private static MovementController _instance;
	private static GameState gamestate;
	private static Communication comm;
	private static String clientToken;
	private static Gson gson;
	
	private MovementController() {
		gson = new Gson();
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
					if (gamestate.inDanger(proj, currentTank)) {
						// do something
					}
				}
			}
			
			for (Tank friendly : friendlies) {
				for (Projectile proj : friendly.projectiles) {
					if (gamestate.inDanger(proj, currentTank)) {
						// do something
					}
				}
			}
			
			
			
			
		}							
	}
	
	public void doAction(Tank tank) {
		Gson gson = new Gson();
		Commands.MoveCommand moveCommand = null;

		Tank[] friendlies = gamestate.getFriendlyTanks();
		Tank[] enemies = gamestate.getEnemyTanks();
		
		for (Tank enemy : enemies) {
			for (Projectile proj : enemy.projectiles) {
				//if (gamestate.inDanger(proj, tank)) {
					moveCommand = new Commands.MoveCommand(clientToken, tank.id, Commands.MoveDirection.FWD, 10);
				//}
			}
		}
		
		for (Tank friendly : friendlies) {
			for (Projectile proj : friendly.projectiles) {
				//if (gamestate.inDanger(proj, tank)) {
					if (moveCommand == null){
						moveCommand = new Commands.MoveCommand(clientToken, tank.id, Commands.MoveDirection.FWD, 10);
					}
				//}
			}
		}
		String json_cmd = new String();
		if (moveCommand != null) {
			json_cmd = gson.toJson(moveCommand);
			String response = comm.send(json_cmd);
		}
									
	}
}
