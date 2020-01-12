package server;

import game.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;


public class Server implements Observer {
    private Socket socket;
    private final Vector<ClientHandler> clients;
    private ServerSocket serverSocket;
    private final Game game;

    private int port;
    private boolean listening;

    public Server() {
        this.clients = new Vector<>();
        this.game = new Game();
        this.port = 5000;
        this.listening = false;
    }


    public void startServer() {
        if (!listening) {
            StartServerThread serverThread = new StartServerThread();
            serverThread.start();
            this.listening = true;
            game.start();
        }
    }

    public void update(Observable observable, Object object) {
        this.clients.removeElement(observable);
    }


    /**
     * This inner class will keep listening to incoming connections,
     * and initiating a ClientThread object for each connection.
     */
    class StartServerThread extends Thread {
        private boolean listen;

        StartServerThread() {
            this.listen = true;
        }

        public void run() {
            try {
                Server.this.serverSocket = new ServerSocket(Server.this.port);
            } catch (IOException e) {
                System.out.println("Could not connect to port 5000");
                System.exit(-1);
            }
            try {
                System.out.println("Server is running");
                while (this.listen) {
                    Server.this.socket = Server.this.serverSocket.accept();
                    System.out.println("Client connected");
                    ClientHandler clientHandler = new ClientHandler(Server.this.socket, game);

                    Thread t = new Thread(clientHandler);
                    clientHandler.addObserver(Server.this);
                    Server.this.clients.addElement(clientHandler);
                    t.start();
                }
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
                this.stopServerThread();
            }
        }

        void stopServerThread() {
            this.listen = false;

            try {
                if (serverSocket != null) Server.this.serverSocket.close();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }


    }
}