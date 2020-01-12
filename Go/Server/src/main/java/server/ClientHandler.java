package server;

import game.ClientMessagesTranslator;
import game.Game;
import game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;


public class ClientHandler extends Observable implements Runnable {
    private PrintWriter writer;

    private final Socket socket;
    private final Game game;
    private Player player;
    private ClientMessagesTranslator clientMessagesTranslator;

    public ClientHandler(Socket socket, Game game) {
        this.socket = socket;
        this.game = game;
    }

    public ClientMessagesTranslator getTranslator() {
        return clientMessagesTranslator;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }


    @Override
    public void run() {
        String message;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            clientMessagesTranslator = new ClientMessagesTranslator(this);
            writer.println("WELCOME");
            while ((message = reader.readLine()) != null) {
                System.out.println("Client sent: " + message);
                clientMessagesTranslator.processIncomingMessage(message);
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        try {
            System.out.println("Closing connection");
            game.deletePlayer(player);

            this.socket.close();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        this.setChanged();
        this.notifyObservers(this);
    }

    public void send(String message) {
        if (writer != null)
            writer.println(message);
        System.out.println("Sending: " + message);
    }

}