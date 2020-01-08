import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.Socket;

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

    public void socket()
    {
        try
        {
            Socket server = new Socket("localhost", 5000);
            PrintWriter out = new PrintWriter(server.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        }
        catch(UnknownHostException e)
        {
            System.out.println("Unknown host: localhost");
            System.exit(1);
        }
        catch(IOException e)
        {
            System.out.println("No I/O");
            System.exit(1);
        }
    }

    public static void main(String[] args)
    {
        new Client();
    }
}
