package Game;

import Exceptions.ComponentException;
import Exceptions.WrongCoordinatesException;
import Game.Logic.GameLogic;
import Game.Logic.MovingLogic;
import Game.Logic.OpponentMove;
import Gui.DrawStates;
import Gui.DrawingManager;
import Gui.GuiController;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class GameManager
{
    GameLogic logic;
    final GuiController controller;
    final DrawingManager drawingManager;
    final int boardSize;
    public final StoneType myColor;
    private GameServerTranslator translator;
    public enum Field {BLACK, WHITE, EMPTY}

    private final Field[][] board;
    private int waitingX;
    private int waitingY;
   
    // Creates board, sets area EMPTY and gives player color.
    // Checks if move is possible from side.
    // Manages which stones should be painted or removed.
    public GameManager(int boardSize, GuiController controller, StoneType myColor)
    {
        this.boardSize = boardSize;
        this.controller = controller;
        this.myColor = myColor;
        this.drawingManager = new DrawingManager(controller);
        board = new Field[boardSize][boardSize];
        
        for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++) board [i][j] = Field.EMPTY;
        }
        
        logic = (myColor == StoneType.BLACK) ? new MovingLogic(this) : new OpponentMove(this);
        displayMessage("Welcome to go. You play as " + myColor + "\n");
    }
        
    public void makeMove(int x, int y)
    {
        logic.makeMove(x, y);
    }

    // Allows to show message in GUI window
    public void displayMessage(String input)
    {
        controller.displayMessage(input);
    }

    // Checks if move is possible from client side
    public boolean checkIfMovePossible(int x, int y)
    {
    	if(x < 0 || y < 0 || x >= boardSize || boardSize <= y)
    	{
    		displayMessage("Clicked outside the board");
    		return false;
    	}
        if (board[x][y] != Field.EMPTY) 
        {
            displayMessage("This field is already occupied");
            return false;
        }
        return true;
    }

    public void sendMove(int x, int y)
    {
        if(x == -1 && y == -1)
        {
            translator.sendPassMove();
        }
        else
        {
            translator.sendMove(x,y);
        }
    }

    public void setTranslator(GameServerTranslator gt)
    {
        this.translator = gt;
    }

    public void setLogic(GameLogic logic)
    {
        this.logic = logic;
    }

    public GameLogic getLogic()
    {
        return logic;
    }

    public GuiController getController()
    {
        return controller;
    }

    public DrawingManager getDrawingManager()
    {
    	return drawingManager;
    }

    public GameServerTranslator getTranslator()
    {
    	return translator;
    }

    // Adds move to board panel
    public void addMyMove() throws WrongCoordinatesException
    {
        controller.getGamePanel().getBoardPanel().addStone(myColor, waitingX, waitingY);
        board[waitingX][waitingY] = (myColor == StoneType.BLACK) ? Field.BLACK : Field.WHITE;
    }

    // Saves move waiting for response from server
    public void saveWaitingMove(int x, int y)
    {
        waitingX = x;
        waitingY = y;
    }

    // Displays message of reason, why user could not put stone
    public void resetMyMove(String reason)
    {
    	String explanation = "";
    	StringBuilder message = new StringBuilder();

    	message.append("Your move to [").append(String.valueOf(waitingX)).append(", ").append(String.valueOf(waitingY)).append("] was incorrect because ");
    	if(reason.contains("SUICIDAL"))
        {
            explanation = "it was suicidal";
        }
    	else if(reason.contains("KO"))
        {
            explanation = "of the KO rule";
        }
    	else if(reason.contains("NOT EMPTY"))
        {
            explanation = "the field was already occupied";
        }
    	message.append(explanation);
    	message.append("Please try again.\n");
    	displayMessage(message.toString());
        waitingX = -1;
        waitingY = -1;
        logic.reset();
    }

    // Adds opponent move to user board
    public void addOpponentsMove(Integer x, Integer y) throws WrongCoordinatesException
    {
        controller.getGamePanel().getBoardPanel().addStone(myColor.other(), x, y);
        board[x][y] = (myColor == StoneType.BLACK) ? Field.WHITE : Field.BLACK;
    }
    
    // Tells translator to send information about user surrender
    public void surrender() throws ComponentException
    {
    	if (translator == null)
        {
            throw new ComponentException("Translator not set in ProgramManager");
        }
    	translator.sendSurrender();
    }

    // Passes move
	public void missTurn() 
	{
		logic.makeMove(-1, -1);
	}
    
	// Removes stones in area
    synchronized public void removeStones(Set<Point> area)
    {
    	for (Point point : area)
        {
            controller.getGamePanel().getBoardPanel().removeStone(point.x, point.y);
            board[point.x][point.y] = Field.EMPTY;
        }
        logic.nextTurn();
    }

    private boolean isAppropriate(int x, int y, DrawStates mode)
    {
    	if (mode.equals(DrawStates.DeadStone))
        {
            return !board[x][y].equals(Field.EMPTY);
        }
    	else
        {
            return board[x][y].equals(Field.EMPTY);
        }
    }
    
	public HashSet<Point> getField(int upperLeftX, int upperLeftY, int width, int height, DrawStates mode)
	{
		HashSet<Point> area = new HashSet<Point>();

		for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++) 
            {
            	if (isAppropriate(i, j, mode) && i >= upperLeftX && i <= upperLeftX + width && j >= upperLeftY && j <= upperLeftY + height)
                {
                    area.add(new Point(i, j));
                }
            }
        }
		return area;
	}
	
	public boolean isFieldTypeAppropriate(int x, int y, DrawStates mode)
	{
		return isAppropriate(x, y, mode);
	}

	// Removes stones
	public void remove(int x, int y) 
	{
		logic.remove(x, y);
	}

	public void addDeadStoneSuggestion(Set<Point> dead)
	{
		drawingManager.setDeadStones(dead);
		logic.nextTurn();
	}
	
	public void addTerritorySuggestion(HashSet<Point> my, HashSet<Point> opp)
	{
		drawingManager.setMyTerritory(my);
		drawingManager.setOpponentsTerritory(opp);
		logic.nextTurn();
	}

	public void resumeGame(StoneType color) 
	{
		drawingManager.removeAllSigns();
		controller.getOptionsPanel().deactivateTerritoriesBox(true);
		if (color.equals(myColor))
        {
            logic = new MovingLogic(this);
        }
		else
		{
			logic = new OpponentMove(this);
			translator.sendResume();
		}
	}

	// When player is content, he can send proposition to enemy player
	public void sendProposition() 
	{
		logic.sendProposal();
	}

	//Accept proposition
	public void acceptProposition()
	{
		translator.sendAcceptance();
	}

	// Counts which player won Game; send it to controller
	public void manageResults(double black, double white) 
	{
		boolean blackWon = black > white;
		boolean Win;
		if (myColor.equals(StoneType.BLACK) && blackWon)
        {
            Win = true;
        }
		else
        {
            Win = myColor.equals(StoneType.WHITE) && !blackWon;
        }
		controller.manageGameEnd(black, white, Win, false);
	}
}