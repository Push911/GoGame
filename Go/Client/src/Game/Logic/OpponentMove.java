package Client.Game.Logic;

import Client.Game.GameManager;

import java.awt.Point;

public class OpponentMove implements GameLogic
{
    private final GameManager manager;

    public OpponentMove(GameManager manager)
    {
        this.manager = manager;
    }

    @Override
    public void makeMove(int x, int y)
	{

	}
    
    public void nextTurn() 
    { 
    	manager.setLogic(new MovingLogic(manager));
    	manager.getControler().getOptionsPanel().activateButtons();
    }
    
    @Override
    public void reset()
	{

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