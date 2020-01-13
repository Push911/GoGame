package Game;

import Exceptions.WrongCoordinateException;
import Game.Logic.GameLogic;
import Game.Logic.GameStateMyMove;
import Game.Logic.Restart;
import Game.Logic.OpponentMove;
import Gui.DrawingManager;
import Gui.DrawingMode;
import Gui.GuiController;
import Exceptions.ComponentException;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class GameManager
{
    private GameLogic logic;
    private final GuiController controller;
    private final DrawingManager drawingManager;
    private final int boardSize;
    public final StoneType stoneColor;
    private GameServerTranslator translator;
    public enum Field {BLACK, WHITE, EMPTY}

    private final Field[][] board;
    private int waitingX;
    private int waitingY;

    public GameManager(int boardSize, GuiController controller, StoneType stoneColor)
    {
        this.boardSize = boardSize;
        this.controller = controller;
        this.stoneColor = stoneColor;
        this.drawingManager = new DrawingManager(controller);
        board = new Field[boardSize][boardSize];
        
        for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++) board [i][j] = Field.EMPTY;
        }
        
        logic = (stoneColor == StoneType.BLACK) ? new GameStateMyMove(this) : new OpponentMove(this);
        displayMessage("Welcome to go. You play as " + stoneColor + ".\n");
    }
        
    public void makeMove(int x, int y)
    {
        logic.makeMove(x, y);
    }

    public void displayMessage(String input)
    {
        controller.displayMessage(input);
    }

    public boolean checkIfMovePossible(int x, int y)
    {
    	if(x < 0 || y < 0 || x >= boardSize || boardSize <= y)
    	{
    		displayMessage("Clicked outside the board");
    		return false;
    	}
        if (board[x][y] != Field.EMPTY) 
        {
            displayMessage("This field is already ocuppied. ");
            return false;
        }
        return true;
    }

    public void sendMove(int x, int y)
    {
        if(x == -1 && y == -1) translator.sendPassMove();
        else translator.sendMove(x,y);
    }

    public void setTranslator(GameServerTranslator gt)
    {
        this.translator = gt;
    }

    public void setState(GameLogic logic)
    {
        this.logic = logic;
    }

    public GameLogic getState()
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


    public void addMyMove()
    {
        try
        {
            controller.getGamePanel().getBoardPanel().addStone(stoneColor, waitingX, waitingY);
            board[waitingX][waitingY] = (stoneColor == StoneType.BLACK) ? Field.BLACK : Field.WHITE;
        }
        catch (WrongCoordinateException e)
        {
            e.printStackTrace();
        }
    }

    public void saveWaitingMove(int x, int y)
    {
        waitingX = x;
        waitingY = y;
    }

    public void resetMyMove(String reason)
    {
    	String explanation = "";
    	StringBuilder message = new StringBuilder();
    	
    	message.append("Your move to [").append(String.valueOf(waitingX)).append(", ").append(String.valueOf(waitingY)).append("] was incorrect because ");
    	if(reason.contains("SUICIDAL"))
        {
            explanation = "it was suicidal. ";
        }
    	else if(reason.contains("KO"))
        {
            explanation = "of the KO rule. ";
        }
    	else if(reason.contains("NOT EMPTY"))
        {
            explanation = "the field was already occupied. ";
        }
    	message.append(explanation);
    	message.append("Please try again.\n");
    	displayMessage(message.toString());
        waitingX = -1;
        waitingY = -1;
        logic.reset();
    }

    public void addOpponentsMove(Integer x, Integer y)
    {
        try
        {
            controller.getGamePanel().getBoardPanel().addStone(stoneColor.other(), x, y);
            board[x][y] = (stoneColor == StoneType.BLACK) ? Field.WHITE : Field.BLACK;
        }
        catch (WrongCoordinateException e)
        {
            e.printStackTrace();
        }
    }    

    public void sendWhiteFlag() throws ComponentException
    {
    	if (translator == null)
        {
            throw new ComponentException("Translator not set in ProgramManager");
        }
    	translator.sendSurrender();
    }

	public void missTurn() 
	{
		logic.makeMove(-1, -1);
	}

    synchronized public void removeStones(Set<Point> fields)
    {
    	for (Point point : fields)
        {
            controller.getGamePanel().getBoardPanel().removeStone(point.x, point.y);
            board[point.x][point.y] = Field.EMPTY;
        }
        logic.nextTurn();
    }

    private boolean isAppropriate(int x, int y, DrawingMode mode)
    {
    	if (mode.equals(DrawingMode.DEAD))  return !board[x][y].equals(Field.EMPTY);
    	else  return board[x][y].equals(Field.EMPTY);
    }
    
	public HashSet<Point> getAppropriateFieldsInArea(int upperLeftX, int upperLeftY, int width, int height, DrawingMode mode) 
	{
		HashSet<Point> fields = new HashSet<Point>(); 
		
		for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++) 
            {
            	if (isAppropriate(i, j, mode) && 
            		i >= upperLeftX && i <= upperLeftX + width &&
            		j >= upperLeftY && j <= upperLeftY + height) fields.add(new Point(i, j));
            }
        }
		return fields;
	}
	
	public boolean isFieldTypeAppropriate(int x, int y, DrawingMode mode)
	{
		return isAppropriate(x, y, mode);
	}

	public void remove(int x, int y) 
	{
		logic.remove(x, y);
	}

	public void addDeadStoneSuggestion(Set<Point> dead)
	{
		drawingManager.setDeadStones(dead);
		logic.nextTurn();
	}
	
	public void addTerritorySuggestion(HashSet<Point> my, HashSet<Point> oppo) 
	{
		drawingManager.setMyTerritory(my);
		drawingManager.setOpponentsTerritory(oppo);
		logic.nextTurn();
	}

	public void resumeGame(StoneType color) 
	{
		drawingManager.removeAllSigns();
		controller.getOptionsPanel().disactivateTeritoriesBox(true);
		if (color.equals(stoneColor))
        {
            logic = new GameStateMyMove(this);
        }
		else
		{
			logic = new OpponentMove(this);
			translator.sendResume();
		}
	}

	public void sendProposition() 
	{
		logic.sendProposal();
	}

	public void acceptProposition()
	{
		translator.sendAcceptance();
	}

	public void manageResults(double black, double white) 
	{
		boolean blackWon = black > white;
		boolean Win;
		if (stoneColor.equals(StoneType.BLACK) && blackWon)
        {
            Win = true;
        }
		else
        {
            Win = stoneColor.equals(StoneType.WHITE) && !blackWon;
        }
		logic = new Restart();
		controller.manageGameEnd(black, white, Win, false);
	}
}