package Client.Game.Logic;

import Client.Game.GameManager;

import java.awt.Point;

public class MovingLogic implements GameLogic
{
    private final GameManager manager;
    private boolean moveSent = false;

    public MovingLogic(GameManager manager)
    {
       this.manager = manager;
    }

    @Override
    public void makeMove(int x, int y)
    {
        if (!moveSent)
        {
        	if(x == -1 && y == -1)
        	{
        		manager.sendMove(x, y);
        		moveSent = true;
        		nextTurn();
        	}
        	else if(manager.checkIfMovePossible(x,y))
	        {
	            manager.sendMove(x, y); 
	            moveSent = true;
	            manager.saveWaitingMove(x,y);
	        }
        }
    }
    
    public void reset()
	{
		moveSent = false;
	}
    
    public void nextTurn() 
    { 
    	manager.setLogic(new OpponentMove(manager));
    	manager.getControler().getOptionsPanel().disactivateButtons(false);
    }

	@Override
	public void remove(int x, int y)
	{

	}
	@Override
	public void endMove(Point coords, boolean isAdding)
	{

	}
	@Override
	public void sendProposal()
	{

	}
}