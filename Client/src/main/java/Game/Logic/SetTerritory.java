package Game.Logic;

import java.awt.Point;

import Game.GameManager;

public class SetTerritory implements GameLogic
{
	private final GameManager manager;
	private Point last = null;
	private boolean isSent = false;

	public SetTerritory(GameManager manager)
	{
		this.manager = manager;
	}
	
	@Override
	public void makeMove(int x, int y) 
	{
		manager.getDrawingManager().mark(x, y, manager.getDrawingManager().drawingMode);
		last = new Point(x,y);
	}
	
	@Override
	public void remove(int x, int y) 
	{
		manager.getDrawingManager().unmark(x, y, manager.getDrawingManager().drawingMode);
		last = new Point(x,y);
	}

	@Override
	public void endMove(Point coordinates, boolean newPointAdded)
	{
		if (last != null) 
		{
			if (newPointAdded)
			{
				manager.getDrawingManager().markGroup(last, coordinates, manager.getDrawingManager().drawingMode);
			}
			else
			{
				manager.getDrawingManager().unmarkGroup(last, coordinates, manager.getDrawingManager().drawingMode);
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
		manager.getController().getOptionsPanel().disactivateTeritoriesBox(false);
		manager.setState(new OpponentSetTerritory(manager));
	}

	@Override
	public void sendProposal() 
	{
		if (!isSent )
		{
			manager.getTranslator().sendTerritories(manager.getDrawingManager().getMyTerritory(),
			manager.getDrawingManager().getOpponentsTerritory());
		}
		isSent = true;
	}
}