package Game.Logic;

import java.awt.Point;

import Game.GameManager;

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
    	manager.setState(new GameStateMyMove(manager)); 
    	manager.getController().getOptionsPanel().activateButtons();
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