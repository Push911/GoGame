package Game;

import java.awt.Point;

public class Stone
{
    private final int x;
    private final int y;
    public final StoneType type;

    public Stone(int x, int y, StoneType type)
    {
        this.x = x;
        this.y = y;
        this.type = type;
    }
    
    public Stone(Point p, StoneType type)
    {
        this.x = p.x;
        this.y = p.y;
        this.type = type;
    }
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
}