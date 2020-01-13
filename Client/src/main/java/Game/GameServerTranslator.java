package Game;

import Game.Logic.SetDead;
import Game.Logic.SetTerritory;
import Game.Logic.GameStateMyMove;
import Game.Logic.OpponentSetDead;
import Game.Logic.OpponentSetTerritory;
import DriverMain.ServerTranslator;
import DriverMain.SocketClient;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class GameServerTranslator extends ServerTranslator
{
    private final GameManager manager;
    private final SocketClient socket;

    public GameServerTranslator(GameManager manager, SocketClient socket) 
    {
       this.manager = manager;
       this.socket = socket;
    }
    
    public void processIncomingMessage(String input)
    {
        if (input.startsWith("SERVERMESSAGE: "))
        {
            input = input.replaceFirst("SERVERMESSAGE: ", "[Server]: ");
            manager.displayMessage(input);
        }
        else if (input.startsWith("MOVEOK"))
        {
            manager.addMyMove();
        }
        else if (input.startsWith("WRONGMOVE"))
        {
        	String[] inputs = input.split(" ");
        	if (inputs.length > 1) manager.resetMyMove(inputs[1]);
        	else manager.resetMyMove(null);
        }
        else if (input.startsWith("OPPOMOVE"))
        {
        	String[] inputs = input.split(":");
        	inputs[0] = inputs[0].replaceFirst("OPPOMOVE ", "");
            String[] coords = inputs[0].split(",");
            manager.addOpponentsMove(Integer.valueOf(coords[0].trim()), Integer.valueOf(coords[1].trim()));
            if (inputs.length > 1) handleRemoved(inputs[1].trim());
            
        }
        else if (input.startsWith("OPPOPASS")){
        	manager.displayMessage("Your opponent has passed. Now it is your turn\n");
        	manager.setState(new GameStateMyMove(manager));
        	manager.getController().getOptionsPanel().activateButtons();
        }
        else if (input.startsWith("CAPTURED"))
        {
        	String[] inputs = input.replaceFirst("CAPTURED ", "").split(":");
        	int black = Integer.valueOf(inputs[0].replaceFirst("BLACK ", "").trim());
        	int white = Integer.valueOf(inputs[1].replaceFirst("WHITE ", "").trim());
        	manager.getController().getOptionsPanel().displayStatistics(black, white);
        }
        else if (input.startsWith("YOULOOSE")){
        	manager.getController().manageGameEnd(0, 0, true, true);
        }
        else if(input.trim().startsWith("REMOVED"))
        {
        	handleRemoved(input.trim());
        }
        else if(input.startsWith("CHOOSEDEAD"))
        {
        	manager.displayMessage("Use left mouse button to mark stones as dead and right to unmark them.\n");
        	manager.getController().displayDialog("<HTML>You both passed. The game is stopped.\n Now it is time to choose which stones are dead. \n Use left mouse button to mark stones as dead and right to unmark them. \n When you are ready, press Send proposition button");
        	manager.setState(new SetDead(manager));
        	manager.getController().getOptionsPanel().activateTeritoriesBox(false);
        }
        else if (input.startsWith("GAMESTOPPED"))
        {
        	manager.getController().displayDialog("<HTML>You both passed. The game is stopped.\n Now your opponent is choosing which stones are dead. \n  When they is ready, it will be your turn. \n Use left mouse button to mark stones as dead and right to unmark them. \n When you are ready, press Send proposition button");
        	manager.setState(new OpponentSetDead(manager));
        }
        else if (input.startsWith("DEADSUGGESTION"))
        {
        	manager.displayMessage("Opponent suggested dead stones. Now it is your turn.\n");

        	input = input.replaceFirst("DEADSUGGESTION ", "");
            Set<Point> dead = GetFieldsCoords(input);

			manager.addDeadStoneSuggestion(dead);
        	manager.setState(new SetDead(manager));
        	manager.getController().getOptionsPanel().activateTeritoriesBox(false);
        }
        else if (input.startsWith("SETTERRITORY"))
        {
        	manager.displayMessage("You have reached an agreement on dead stones. Now please suggest territories.\n");
        	manager.removeStones(manager.getDrawingManager().getDead());
        	manager.getDrawingManager().removeAllSigns();
        	manager.setState(new SetTerritory(manager));
        	manager.getController().getOptionsPanel().activateTeritoriesBox(true);
        }
        else if (input.startsWith("DEADOK"))
        {
        	
        	manager.displayMessage("You have reached agreement on dead stones. Now your opponent is suggesting territories.\n");

        	input = input.replaceFirst("DEADOK ", "");
            Set<Point> dead = GetFieldsCoords(input);
			manager.getDrawingManager().removeAllSigns();
        	manager.removeStones(dead);
        	manager.setState(new OpponentSetTerritory(manager));
        	manager.getController().getOptionsPanel().disactivateTeritoriesBox(false);
        	
        }
        else if (input.startsWith("TERRITORYSUGGESTION"))
        {
        	String[] inputs = input.replaceFirst("TERRITORYSUGGESTION ", "").split(":");
        	String[] fields0;
        	String[] fields1;
        	HashSet<Point> f0 = new HashSet<>();
        	HashSet<Point> f1 = new HashSet<>();
        	if(!inputs[0].replaceFirst("BLACK ", "").trim().startsWith("NONE"))
        	{
        		fields0 = inputs[0].replaceFirst("BLACK ", "").trim().split(" ");
            	for (String pair : fields0) 
            	{
                    String[] coords = pair.split(",");
                    Point p = new Point(Integer.valueOf(coords[0].trim()), Integer.valueOf(coords[1].trim()));
                    f0.add(p);     
    			}
        	}
        	if(!inputs[1].replaceFirst("WHITE ", "").trim().startsWith("NONE"))
        	{
        		fields1 = inputs[1].replaceFirst("WHITE ", "").trim().split(" ");
            	for (String pair : fields1) 
            	{
                    String[] coords = pair.split(",");
                    Point p = new Point(Integer.valueOf(coords[0].trim()), Integer.valueOf(coords[1].trim()));
                    f1.add(p);     
    			}
        	}
        	HashSet<Point> myF;
        	HashSet<Point> theirF;
        	if(manager.stoneColor.equals(StoneType.BLACK))
        	{
        		myF = f0;
        		theirF = f1;
        	}
        	else
        	{
        		myF = f1;
        		theirF = f0;
        	}

        	manager.addTerritorySuggestion(myF, theirF);
        }
        else if (input.startsWith("RESUMEGAME"))
        {
        	manager.displayMessage("Game resumed. Your move.");
        	manager.getController().displayDialog("<HTML>Your opponent requested resuming the game. Your move. </html>");
        	manager.resumeGame(manager.stoneColor);
        }
        else if (input.startsWith("THEEND"))
        {
        	String[] inputs = input.replaceFirst("THEEND ", "").split(":");
        	double black = Double.valueOf(inputs[0].replaceFirst("BLACK ", "").trim());
        	double white = Double.valueOf(inputs[1].replaceFirst("WHITE ", "").trim());
        	manager.manageResults(black, white);
        }
        else System.out.println("Unknown system command");
    }

	private Set<Point> GetFieldsCoords(String input) {
    	Set<Point> points = new HashSet<>();
		if (!input.trim().equals("NONE"))
		{
			String[] pairs = input.split(" ");

			for (String pair : pairs)
			{
				String[] coords = pair.split(",");
				Point p = new Point(Integer.valueOf(coords[0].trim()), Integer.valueOf(coords[1].trim()));
				points.add(p);
			}
		}
		return points;
	}

    public void sendMove(int x, int y)
    {
        socket.send("MOVE " + String.valueOf(x) + " " + String.valueOf(y));
    }
    
    public void sendSurrender(){
    	
    	socket.send("SURRENDER " + manager.stoneColor);
    }

    
    public void sendPassMove() 
    {
		socket.send("MOVE PASS");
    }
    
    public void sendResume() 
	{
		socket.send("RESUME");
	}
	
	private void handleRemoved(String input)
	{
        input = input.replaceFirst("REMOVED ", "");
        System.out.println(input);
        Set<Point> points = GetFieldsCoords(input);
		manager.removeStones(points);
	}

	public void sendTerritories(Set<Point> myTeritory, Set<Point> opponentsTeritory)
	{
		StringBuilder message = new StringBuilder("TERRITORYSUGGESTION ");
		message.append("BLACK ");
		if(manager.stoneColor.equals(StoneType.BLACK)) message.append(createFieldsList(myTeritory));
		else message.append(createFieldsList(opponentsTeritory));
		message.append(" : WHITE ");
		if(manager.stoneColor.equals(StoneType.WHITE)) message.append(createFieldsList(myTeritory));
		else message.append(createFieldsList(opponentsTeritory));
		socket.send(message.toString());
	}
	
	public void sendDead(Set<Point> dead)
	{
        socket.send("DEADSUGGESTION " + createFieldsList(dead));
	} 
	
    private String createFieldsList(Set<Point> fields)
    {
    	StringBuilder message = new StringBuilder();
    	if(fields != null && !fields.isEmpty())
        {
            for (Point p : fields)
            {
                message.append(String.valueOf(p.x)).append(",").append(String.valueOf(p.y)).append(" ");
            }       
        }
    	else message.append("NONE");
    	return message.toString();   
    }

	public void sendAcceptance() 
	{
		socket.send("ACCEPT");
	}



}