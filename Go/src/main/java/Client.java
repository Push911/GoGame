import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Client extends Server implements MouseListener
{
    public void MouseComponent()
    {
        addMouseListener(this);
    }
    Point point;
    int x, y;

    @Override
    public void mouseClicked(MouseEvent mouse)
    {
        x = mouse.getX();
        y = mouse.getY();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent)
    {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent)
    {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent)
    {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent)
    {

    }

    public static void main(String[] args)
    {
        new Gui();
    }
}
