package tankbattle.client.stub;

import com.google.gson.*;

import java.io.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.zeromq.ZMQ;

import tankbattle.client.stub.GameState.Tank;
import tankbattle.model.*;
import commands.Commands;
import commands.Commands.Direction;
import commands.Commands.TurretRotateCommand;

final class Client
{
	class Type
	{
		public static final String CREATE = "create";
		public static final String JOIN = "join";
	}
	

	public static void main(String[] args)
	{
		Client.run(args);
	}

	public static void run(String[] args)
	{	
		Gson gson = new Gson();
		Config config = null;
		Commands commands;
		
		
		// The name of the file to open.
        String fileName = "cardigan.config";
        
        StringBuilder sb = new StringBuilder();
        String line = null;
        // Read from config one file at a time
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }   
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
		
        try {
        	config = gson.fromJson(sb.toString(), Config.class);
        } catch (JsonSyntaxException e) {
        	System.out.println("Error Parsing Config File:");
        	System.out.println(e.getMessage().toString());
        	System.exit(-1);
        }
        
		System.out.println("Starting Battle Tank Client...");

		commands = new Commands(config.MatchToken);
		
		Command command = new Command();

		// retrieve the command to connect to the server
		while (true) {
			String connectCommand = command.getMatchConnectCommand(config.TeamName, config.Password, config.MatchToken);
	
			// retrieve the communication singleton
			Communication comm = Communication.getInstance(config.GameServerIP, config.MatchToken);
	
			// send the command to connect to the server
			System.out.println("Connecting to server...");
			String clientToken = comm.send(connectCommand, Command.Key.CLIENT_TOKEN);
			System.out.println("Received client token... " + clientToken);
			
			// Check to make sure we are connected
			if (null == clientToken)
			{
				System.out.println("Error: unable to connect!");
				System.exit(-1);
			}
	
			// the GameInfo object will hold the client's name, token, game type, etc.
			GameInfo gameInfo = new GameInfo(clientToken, config.TeamName);
	
			// We are now connected to the server. Let's do some stuff:
			System.out.println("Connected!");
			
			System.out.println("Waiting for initial game state...");
			
	
			/**** BEGIN THE GAME ****/
			
			JSONObject jsonState = comm.getJSONGameState(); // Blocking wait for game state example
			GameState gameState = gson.fromJson(jsonState.toString(), GameState.class);
			//GameState gameState = gson.fromJson(jsonState, GameState.class);
			
			String output = gson.toJson(gameState).toString();
			System.out.println(output);
			
			
			TurretController tc;
			MovementController mc;
			mc = MovementController.getInstance(comm, clientToken, gameState);
	
			for (Tank tank : gameState.getFriendlyTanks()) {
				if (tank.alive) {
					mc.turnPerpindicular(tank);
				}
			}
			
				while (gameState.timeRemaining > 1) {
					tc = TurretController.getInstance(comm, clientToken, gameState);
					mc = MovementController.getInstance(comm, clientToken, gameState);
					
					for (Tank tank : gameState.getFriendlyTanks()) {
						if (tank.alive) {
							tc.doAction(tank);
							mc.doAction(tank);
	
						}
						
						jsonState = comm.getJSONGameState(); // Blocking wait for game state example
						gameState = gson.fromJson(jsonState.toString(), GameState.class);
					}
					
				} 
				
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					// do nothing
				}
			
		}
		
		/**** END THE GAME ****/
		
		//System.out.println("Exiting...");
	}

	public static void printHelp()
	{
			System.out.println("usage: Client <ip address> <team-name> <password> <match-token>");
	}
}
