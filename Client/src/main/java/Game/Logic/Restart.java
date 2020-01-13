package Game.Logic;

import java.awt.Point;

public class Restart implements GameLogic
{

	@Override
    public void makeMove(int x, int y) {}

	@Override
    public void reset() { }

	@Override
    public void nextTurn() { }

	@Override
	public void remove(int x, int y) { }

	@Override
	public void endMove(Point coords, boolean isAdding) { }

	@Override
	public void sendProposal() { }
}