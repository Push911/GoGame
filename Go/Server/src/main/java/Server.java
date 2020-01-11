import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server extends Gui
{
    ServerSocket server = null;
    Socket client = null;
    BufferedReader in = null;
    PrintWriter out = null;
    public Server()
    {
        socket();
    }
    public void socket()
    {
        try
        {
            server = new ServerSocket(5000);
            System.out.println("Now open client");
            client = server.accept();
            System.out.println("New client added");
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        }
        catch(UnknownHostException e)
        {
            System.out.println("Unknown host: localhost");
            System.exit(1);
        }
        catch(IOException e)
        {
            System.out.println("No I/O,please open client");
            System.exit(1);
        }

        //commit
        //commit
        //commit
    }
    public static void main(String[] args)
    {
        new Server();
    }
}

