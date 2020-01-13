package Gui;

import Game.Stone;
import Game.StoneType;
import Exceptions.WrongCoordinateException;
import Exceptions.ComponentException;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel
{
    private final GuiController parent;
    private int n;
    private int fieldSize;
    private int stoneRadius;
    private Point[][] fields;
    private Point[] dots;
    private final ArrayList<Stone> stones;
    
    private BufferedImage blackStone;
	private BufferedImage whiteStone;

    public BoardPanel(GuiController parent)
    {
        this.parent = parent;
        stones = new ArrayList<>();
        initComponents();
    }

    private void initComponents()
    {
        Dimension panelSize = new Dimension(500, 500);
        setMaximumSize(panelSize);
        setPreferredSize(panelSize);
        setMinimumSize(panelSize);
        setSize(panelSize);

        setBackground(new Color(220, 179, 92));
        createBoard();

        try
        {
            InputStream isInputB = new FileInputStream("black.png");
            blackStone = ImageIO.read(isInputB);
            InputStream isInputW = new FileInputStream("white.png");
            whiteStone = ImageIO.read(isInputW);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void createBoard()
    {
        n = 19;
        fields = new Point[n][n];
        
        int panelSize = getWidth();  
        this.fieldSize = panelSize / (n + 2);
        
        int xPosition = 7 * fieldSize / 4;
        for (int x = 0; x < n; x++)
        {
            int yPosition = 7 * fieldSize / 4;
            for (int y = 0; y < n; y++)
            {
                fields[x][y] = new Point(xPosition, yPosition);
                yPosition += fieldSize;                   
            }
            xPosition += fieldSize;
        }
        
        dots = new Point[9];
        dots[0] = new Point((int)fields[3][3].getX(), (int)fields[3][3].getY());
        dots[1] = new Point((int)fields[n-4][3].getX(), (int)fields[n-4][3].getY());
        dots[2] = new Point((int)fields[3][n-4].getX(), (int)fields[3][n-4].getY());
        dots[3] = new Point((int)fields[n-4][n-4].getX(), (int)fields[n-4][n-4].getY());
        dots[4] = new Point((int)fields[n/2][3].getX(), (int)fields[n/2][3].getY());
        dots[5] = new Point((int)fields[n-4][n/2].getX(), (int)fields[n-4][n/2].getY());
        dots[6] = new Point((int)fields[n/2][n-4].getX(), (int)fields[n/2][n-4].getY());
        dots[7] = new Point((int)fields[3][n/2].getX(), (int)fields[3][n/2].getY());
        dots[8] = new Point((int)fields[n/2][n/2].getX(), (int)fields[n/2][n/2].getY());
        stoneRadius = fieldSize / 2;
    }

    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        drawBoard(g);
        drawStone(g);
        drawDeadSigns(g);
        drawTerritories(g);
    }

    private void drawBoard(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setPaint(Color.black);
        char column = 'A';        
        for (int i = 0; i < n; i++)
        {
            g.drawLine((int)fields[i][0].getX(), (int)fields[i][0].getY() - fieldSize / 2, (int)fields[i][n-1].getX(), (int)fields[i][n-1].getY() + fieldSize / 2);
            g2d.drawString(String.valueOf(column), (int)fields[i][0].getX() - fieldSize / 4, (int)fields[i][0].getY() - fieldSize );
            g2d.drawString(String.valueOf(column), (int)fields[i][n-1].getX() - fieldSize / 4, (int)fields[i][n-1].getY() + (5 * fieldSize) / 4);
            column++;
        }
        
        int radius = fieldSize / 5;
        
        int row = n;
        for (int i = 0; i < n; i++)
        {
            g.drawLine((int)fields[0][i].getX() - fieldSize / 2, (int)fields[0][i].getY(), (int)fields[n-1][i].getX() + fieldSize / 2, (int)fields[n-1][i].getY());
            g2d.drawString(String.valueOf(row), (int)fields[0][i].getX() - fieldSize * 3/2, (int)fields[0][i].getY() + fieldSize / 4);
            g2d.drawString(String.valueOf(row), (int)fields[n-1][i].getX() + fieldSize, (int)fields[n-1][i].getY() + fieldSize / 4);
            row--;
        }
        
        for (Point point : dots)
        {
            g.fillOval(point.x - radius, point.y - radius, 2 * radius, 2 * radius);
        } 
    }

    private void drawStone(Graphics g)
    {        
        for (Stone stone : stones)
        {
            BufferedImage image = (stone.type == StoneType.BLACK) ? blackStone : whiteStone;
            g.drawImage(image, fields[stone.getX()][stone.getY()].x - stoneRadius, fields[stone.getX()][stone.getY()].y - stoneRadius, stoneRadius * 2, stoneRadius * 2, null);
        }  
    }

    private void drawDeadSigns(Graphics g) 
    {
    	 Graphics2D g2d = (Graphics2D) g;
         g2d.setPaint(Color.red);
         Stroke s = g2d.getStroke();
         g2d.setStroke(new BasicStroke(3));
         try
         {
			Set<Point> dead = parent.getGameManager().getDrawingManager().getDead();
			for (Point point : dead) 
			{
				int signSize = fieldSize/3;
				 g.drawLine(fields[point.x][point.y].x - signSize, fields[point.x][point.y].y - signSize, fields[point.x][point.y].x + signSize, fields[point.x][point.y].y + signSize);
				 g.drawLine(fields[point.x][point.y].x + signSize, fields[point.x][point.y].y - signSize, fields[point.x][point.y].x - signSize, fields[point.x][point.y].y + signSize);
			}
		}
         catch (ComponentException e)
         {
             return;
         }
         g2d.setStroke(s);
	}

    private void drawTerritories(Graphics g)
    {         
		 try 
		 {
			 Set<Point> my = parent.getGameManager().getDrawingManager().getMyTerritory();
			 drawRectangles(g, new Color(0f,1f,0f,.5f), my);
			 Set<Point> oppo = parent.getGameManager().getDrawingManager().getOpponentsTerritory();
			 drawRectangles(g, new Color(0f,0f,1f,.5f), oppo);
		} 
		catch (ComponentException e)
        {
        }
    }

    private void drawRectangles(Graphics g, Color c, Set<Point> points)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(c);
		int signSize = fieldSize/2;
		for (Point p : points) 
		{
	        g.fillRect(fields[p.x][p.y].x - signSize, fields[p.x][p.y].y - signSize, 2 * signSize, 2 * signSize);
		}
    }

    public Point pullToGrid(Point p)
    {
        double x = p.getX();
        double y = p.getY();

        int x0 = fields[0][0].x;
        int y0 = fields[0][0].y;
        
        int xn = fields[n-1][n-1].x;
        int yn = fields[n-1][n-1].y;
        int xSize = xn-x0 + fieldSize;
        int ySize = yn-y0 + fieldSize;
       
        double gridX= (x - x0) / xSize * n;
        double gridY= (y - y0) / ySize * n;
       
        int gridYrounded = (int)Math.round(gridY);
        int gridXrounded = (int)Math.round(gridX);
        
        double deltaX = gridX - gridXrounded;
        double deltaY = gridY - gridYrounded;
    
        double precision = 0.25;
       
        if (deltaX > precision || deltaY > precision) return null;
    
        return new Point(gridXrounded, gridYrounded);
    }

   synchronized public void addStone(StoneType stoneType, int x, int y) throws WrongCoordinateException
   {
       if (x >= 0 && x < n && y >= 0 && y < n)
       {
           stones.add(new Stone(x, y, stoneType));
       }
       else
       {
           throw new WrongCoordinateException("Chosen coordinates do not exist on board.");
       }
       repaint();
   }

    synchronized public void removeStone(int x, int y)
    {
    	Iterator<Stone> iter = stones.iterator();
    	while (iter.hasNext()) 
    	{
    	    Stone s = iter.next();

    	    if ((s.getX() == x) && (s.getY() == y)) iter.remove();
    	}
        repaint();
    }
}