package Game.Logic;

import java.awt.Point;

import Game.GameManager;

public class GameStateMyMove implements GameLogic
{
    private final GameManager manager;
    private boolean turnSent = false;

    public GameStateMyMove(GameManager manager)
    {
       this.manager = manager;
    }

    @Override
    public void makeMove(int x, int y)
    {
        if (!turnSent)
        {
        	if(x == -1 && y == -1)
        	{
        		manager.sendMove(x, y);
        		turnSent = true;
        		nextTurn();
        	}
        	else if( manager.checkIfMovePossible(x,y)) 
	        {
	            manager.sendMove(x, y); 
	            turnSent = true;
	            manager.saveWaitingMove(x,y);
	        }
        }
    }
    
    public void reset()
	{
		turnSent = false;
	}
    
    public void nextTurn() 
    { 
    	manager.setState(new OpponentMove(manager));
    	manager.getController().getOptionsPanel().disactivateButtons(false);
    }

	@Override
	public void remove(int x, int y) { }

	@Override
	public void endMove(Point coordinates, boolean newPointAdded)
	{

	}

	@Override
	public void sendProposal()
	{

	}
}