package Client.ProgramDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient extends Thread
{
    private Socket socket = null;
    private PrintWriter out = null;
    private ServerInput input;

    public SocketClient(ServerInput input)
    {
        this.input = input;
    }

    // Listens to the socket, sends user queries and prints out server responses.
    private void listenSocket()
    {
        try 
        {
            socket = new Socket("localhost", 5000);
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            System.out.println(in.readLine());
            out.println("Client launched successfully");
            String serverLine;
            while((serverLine = in.readLine()) != null)
            {
                System.out.println("Server: " + serverLine);
                input.incomingMessageHandler(serverLine);
            }
        }
        catch (UnknownHostException e) 
        {
           System.out.println("Unknown host: localhost"); 
           System.exit(1);
        }
        catch  (IOException e)
        {
            System.out.println("No I/O"); 
            System.exit(1);
        }
    }
    
    /**
     * Gets the PrintWriter to write to the server.
     * @return PrintWriter.
     */
    public void send(String text) 
    { 
        out.println(text);
    }
    
    // Socket listener
    @Override
    public void run() 
    { 
        try
        {
            sleep(100);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        listenSocket(); 
    }

    public void setTranslator(ServerInput input)
    {
        this.input = input;
    }
}