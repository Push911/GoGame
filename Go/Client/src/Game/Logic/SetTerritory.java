package Game.Logic;

import Game.GameManager;

import java.awt.Point;

public class SetTerritory implements GameLogic
{
	private final GameManager manager;
	private Point last = null;
	private boolean alreadySent = false;

	public SetTerritory(GameManager manager)
	{
		this.manager = manager;
	}
	
	@Override
	public void makeMove(int x, int y) 
	{
		manager.getDrawingManager().mark(x, y, manager.getDrawingManager().drawStates);
		last = new Point(x,y);
	}
	
	@Override
	public void remove(int x, int y) 
	{
		manager.getDrawingManager().unmark(x, y, manager.getDrawingManager().drawStates);
		last = new Point(x,y);
	}

	@Override
	public void endMove(Point coords, boolean isAdding)
	{
		if (last != null) 
		{
			if (isAdding)
			{
				manager.getDrawingManager().markGroup(last, coords, manager.getDrawingManager().drawStates);
			}
			else
			{
				manager.getDrawingManager().unmarkGroup(last, coords, manager.getDrawingManager().drawStates);
			}
		}
		last = null;
	}

	@Override
	public void reset()
	{

	}
	
	@Override
	public void nextTurn() 
	{ 
		manager.getController().getOptionsPanel().deactivateTerritoriesBox(false);
		manager.setLogic(new OpponentSetTerritory(manager));
	}

	@Override
	public void sendProposal() 
	{
		if (!alreadySent )
		{
			manager.getTranslator().sendTerritories(manager.getDrawingManager().getMyTerritory(), manager.getDrawingManager().getOpponentsTerritory());
		}
		alreadySent = true;
	}
}