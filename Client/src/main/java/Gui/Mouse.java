package Gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import Exceptions.ComponentException;

class Mouse implements MouseListener
{
    private final GuiController parent;
    
    public Mouse(GuiController parent)
    {
        this.parent = parent;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
       Point coordinates = e.getPoint();

        handleMouseEvent(e, coordinates);
    }

    private void handleMouseEvent(MouseEvent e, Point coordinates) {
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            coordinates = parent.getGamePanel().getBoardPanel().pullToGrid(coordinates);
            if (coordinates != null) try
            {
                parent.getGameManager().makeMove(coordinates.x, coordinates.y);
            }
            catch (ComponentException e1)
            {
                System.out.println(e1.getMessage());
            }
        }
        else if (e.getButton() == MouseEvent.BUTTON3)
        {
            coordinates = parent.getGamePanel().getBoardPanel().pullToGrid(coordinates);
            if (coordinates != null) try
            {
                 parent.getGameManager().remove(coordinates.x, coordinates.y);
            }
            catch (ComponentException e1)
            {
                System.out.println(e1.getMessage());
            }
        }
    }


    @Override
    public void mousePressed(MouseEvent e)
    {
    	Point coordinates = e.getPoint();
        handleMouseEvent(e, coordinates);
    }
    
    @Override
    public void mouseReleased(MouseEvent me)
    {
    	Point coordinates =me.getPoint();
    	coordinates = parent.getGamePanel().getBoardPanel().pullToGrid(coordinates);
    	if (coordinates != null)
    	{
    		try 
    		{
    			if (me.getButton() == MouseEvent.BUTTON1)
                {
                    parent.getGameManager().getState().endMove(coordinates, true);
                }
    			else if (me.getButton() == MouseEvent.BUTTON3)
                {
                    parent.getGameManager().getState().endMove(coordinates, false);
                }
			}
    		catch (ComponentException e1)
            {
                System.out.println(e1.getMessage());
            }
    	}
	}
    
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }

}