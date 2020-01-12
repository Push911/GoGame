package Game.Logic;

import Game.GameManager;

import java.awt.Point;

public class OpponentSetDead implements GameLogic
{
	private final GameManager manager;

	public OpponentSetDead(GameManager manager)
	{
		this.manager = manager;
	}

	@Override
	public void nextTurn() 
	{
		manager.setLogic(new SetDead(manager));
		manager.getController().getOptionsPanel().activateTerritoriesBox(false);
	}

	@Override
	public void makeMove(int x, int y)
	{

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