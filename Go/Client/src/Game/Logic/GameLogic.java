package Game.Logic;

import java.awt.Point;

public interface GameLogic
{
    void makeMove(int x, int y);
    void reset();
    void nextTurn();
	void remove(int x, int y);
	void endMove(Point coords, boolean isAdding);
	void sendProposal();
}

