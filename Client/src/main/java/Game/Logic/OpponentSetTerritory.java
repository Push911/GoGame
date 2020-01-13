package Game.Logic;

import java.awt.Point;

import Game.GameManager;

public class OpponentSetTerritory implements GameLogic
{

	private final GameManager manager;

	public OpponentSetTerritory(GameManager manager)
	{
		this.manager = manager;
	}
	@Override
	public void nextTurn() 
	{ 
		manager.getController().getOptionsPanel().activateTeritoriesBox(true);
		manager.setState(new SetTerritory(manager));
	}

	@Override
	public void makeMove(int x, int y) {}

	@Override
	public void reset() {}

	@Override
	public void remove(int x, int y) { }

	@Override
	public void endMove(Point coords, boolean isAdding) { }

	@Override
	public void sendProposal() {	}
}