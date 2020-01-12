package Game.Logic;

import Game.GameManager;
import Gui.DrawStates;

import java.awt.Point;

public class SetDead implements GameLogic
{
	private final GameManager manager;
	private Point last = null;
	private boolean Sent = false;

	public SetDead(GameManager manager)
    {
       this.manager = manager;
    }
	
	@Override
	public void makeMove(int x, int y) 
	{
		manager.getDrawingManager().mark(x, y, DrawStates.DeadStone);
		last = new Point(x,y);
	}
	
	@Override
	public void remove(int x, int y) 
	{
		manager.getDrawingManager().unmark(x, y, DrawStates.DeadStone);
		last = new Point(x,y);
	}

	@Override
	public void endMove(Point coords, boolean isAdding)
	{
		if (last != null) 
		{
			if (isAdding)
			{
				manager.getDrawingManager().markGroup(last, coords, DrawStates.DeadStone);
			}
			else
			{
				manager.getDrawingManager().unmarkGroup(last, coords, DrawStates.DeadStone);
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
		manager.setLogic(new OpponentSetDead(manager));
	}

	@Override
	public void sendProposal() 
	{
		if (!Sent)
		{
			manager.getTranslator().sendDead(manager.getDrawingManager().getDead());
		}
		Sent = true;
	}
}