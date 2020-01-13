package DriverMain;

import Exceptions.ComponentException;
import Exceptions.EmptyNameException;
import Exceptions.NameContainsSpaceException;
import Game.StoneType;

public class ProgramServerTranslator extends ServerTranslator
{
    private ProgramManager manager;
    private SocketClient socket;
    
    public ProgramServerTranslator(ProgramManager manager)
    {
       this.manager = manager;
    }
    
    public void processIncomingMessage(String input)
    {       
        
        if(input.startsWith("SETNAME"))
        {
            manager.askForName("Please choose your nickname.");
        }
        else if(input.startsWith("NAMETAKEN"))
        {
            manager.askForName("This nickname is already taken. Please choose another one.");
        }
        else if(input.startsWith("NAMEOK"))
        {
            sendListRequest();
        }
        else if(input.startsWith("LIST"))
        {
            input = input.replaceFirst("LIST ", "");
            manager.showPlayers(input, "Please choose your opponent");   
        }
        else if(input.startsWith("INVITATIONFROM"))
        {
            manager.invite(input.replaceFirst("INVITATIONFROM ", ""));
        }
        else if(input.startsWith("DECLINED")){
        	try {
				manager.askForList();
			} catch (ComponentException e) {
				e.printStackTrace();
			}
        }
        else if(input.startsWith("GAMESTART"))
        {
            input = input.replaceFirst("GAMESTART ", "").trim();
            if (input.equals("BLACK")) manager.startGame(StoneType.BLACK);
            else if (input.equals("WHITE")) manager.startGame(StoneType.WHITE);          
        }
        else if(input.startsWith("CHOOSEANOTHEROPPONENT ")){
        	input = input.replaceFirst("CHOOSEANOTHEROPPONENT " , "");
        	manager.showPlayers(input, "Enemy don't want to play with you. Better use your holidays free time for your own or choose another player");
        }
        else if(input.startsWith("CHOOSEOPPONENTAGAIN"))
        {
        	input = input.replaceFirst("CHOOSEOPPONENTAGAIN ", "");
        	manager.showPlayers(input, "Chosen player is no longer available. Please choose again.");
        }
        else
        {
            System.out.println("Unknown server response");
        }
    }

    public void sendName(String name) throws NameContainsSpaceException, EmptyNameException
    {
    	if(name.contains(" ")) throw new NameContainsSpaceException(name);
    	if(name.equals("")) throw new EmptyNameException();
        socket.send("USERNAME " + name);
    }

    public void sendOpponent(String oppname)
    {
        socket.send("OPPONENT " + oppname);
    }

    public void sendListRequest()
    {
       socket.send("LIST");
    }

    public void sendAgreement(String name)
    {
        socket.send("INVAGREE " + name);
    }
    
    public void sendDecline(String name)
    {
		socket.send("INVDECLINE " + name);
	}

    public void setSocket(SocketClient socket)
    {
        this.socket = socket;
    }
}