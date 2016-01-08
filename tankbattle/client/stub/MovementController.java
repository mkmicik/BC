package tankbattle.client.stub;

import commands.Commands;
import commands.Commands.MoveCommand;
import commands.Commands.RotateCommand;
import commands.Commands.StopCommand;
import tankbattle.client.stub.GameState.Tank;
import tankbattle.client.stub.GameState.Tank.Projectile;

import com.google.gson.Gson;


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
	public void turnPerpindicular(Tank tank) {
	    Tank target = gamestate.acquireTarget(tank);
        RotateCommand turncmd = null;
        StopCommand stopcmd = null;

        double relativeX = target.position[0] - tank.position[0];
        double relativeY = target.position[1] - tank.position[1];
        double angleToTarget = (Math.atan2(relativeY, relativeX));
        if (angleToTarget < 0) {
            angleToTarget = 2*Math.PI + angleToTarget;
        }
        // If youre already perpinicular dont turn
        if (Math.abs(tank.tracks - angleToTarget) < 0.05) {
        	stopcmd = new StopCommand(clientToken, tank.id, "ROTATE");
        } else {
            if ((tank.tracks - angleToTarget) > 0) {
                //send a rotate counterclockwise command of tank.angle + angletoTarget
                turncmd = new RotateCommand(clientToken, tank.id, Commands.Direction.CW, tank.tracks - angleToTarget);
            } else {
                //send a rotate clockwise command of angletotarget - tank.angle 
                turncmd = new RotateCommand(clientToken, tank.id, Commands.Direction.CCW, angleToTarget - tank.tracks);
            }
        }
        String json_cmd = null;
        if (stopcmd != null) {
        	json_cmd = gson.toJson(stopcmd);
        } else if (turncmd != null) {
            json_cmd = gson.toJson(turncmd);
        }
        String response = comm.send(json_cmd);
        System.out.println(response);
    }  
	
	public void doAction(Tank tank) {
		Gson gson = new Gson();
		MoveCommand moveCommand = null;

		Tank[] friendlies = gamestate.getFriendlyTanks();
		Tank[] enemies = gamestate.getEnemyTanks();
		
		turnPerpindicular(tank);
		
		for (Tank enemy : enemies) {
			for (Projectile proj : enemy.projectiles) {
				if (gamestate.inDanger(proj, tank)) {
					moveCommand = new MoveCommand(clientToken, tank.id, Commands.MoveDirection.FWD, 10);
				}
			}
		}
		
		for (Tank friendly : friendlies) {
			for (Projectile proj : friendly.projectiles) {
				if (gamestate.inDanger(proj, tank)) {
					if (moveCommand == null){
						moveCommand = new MoveCommand(clientToken, tank.id, Commands.MoveDirection.FWD, 10);
					}
				}
			}
		}
		String json_cmd = new String();
		if (moveCommand != null) {
			json_cmd = gson.toJson(moveCommand);
			String response = comm.send(json_cmd);
			//System.out.println(response);
		}
									
	}
}
