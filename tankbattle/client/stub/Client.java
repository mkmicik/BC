package tankbattle.client.stub;

import com.google.gson.*;

import messages.MatchConnectResponse;
import messages.Message;

import org.json.JSONException;
import org.json.JSONObject;
import org.zeromq.ZMQ;

import tankbattle.model.*;

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
		String ipAddress = null;
		String teamName = null;
		String password = null;
		String matchToken = null;

		if(args.length != 4) {
			Client.printHelp();
			return;
		}

		ipAddress = args[0];
		teamName = args[1];
		password = args[2];
		matchToken = args[3];

		System.out.println("Starting Battle Tank Client...");

		Command command = new Command();

		// retrieve the command to connect to the server
		String connectCommand = command.getMatchConnectCommand(teamName, password, matchToken);

		// retrieve the communication singleton
		Communication comm = Communication.getInstance(ipAddress, matchToken);

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
		GameInfo gameInfo = new GameInfo(clientToken, teamName);

		// We are now connected to the server. Let's do some stuff:
		System.out.println("Connected!");
		
		System.out.println("Waiting for initial game state...");
		

		/**** BEGIN THE GAME ****/
		
		Gson gson = new Gson();
		JSONObject jsonState = comm.getJSONGameState(); // Blocking wait for game state example
		GameState gameState = gson.fromJson(jsonState.toString(), GameState.class);
		//GameState gameState = gson.fromJson(jsonState, GameState.class);
		
		String output = gson.toJson(gameState).toString();
		System.out.println(output);
		
		//while (gameState.timeRemaining > 0) {
			// make decisions
			
		//} 
		
		/**** END THE GAME ****/
		
		System.out.println("Exiting...");
	}

	public static void printHelp()
	{
			System.out.println("usage: Client <ip address> <team-name> <password> <match-token>");
	}
}
