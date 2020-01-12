package Client.Game.Logic;

import Client.Game.GameManager;
import Client.Gui.DrawingMode;

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
		manager.getDrawingManager().mark(x, y, DrawingMode.DEAD);
		last = new Point(x,y);
	}
	
	@Override
	public void remove(int x, int y) 
	{
		manager.getDrawingManager().unmark(x, y, DrawingMode.DEAD);
		last = new Point(x,y);
	}

	@Override
	public void endMove(Point coords, boolean isAdding)
	{
		if (last != null) 
		{
			if (isAdding)
			{
				manager.getDrawingManager().markGroup(last, coords, DrawingMode.DEAD);
			}
			else
			{
				manager.getDrawingManager().unmarkGroup(last, coords, DrawingMode.DEAD);
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
		manager.getControler().getOptionsPanel().disactivateTeritoriesBox(false);
		manager.setLogic(new GameStateOpponentsChoosingDead(manager));
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