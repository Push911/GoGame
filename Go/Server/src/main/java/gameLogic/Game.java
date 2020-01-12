package gameLogic;

import server.ClientHandler;
import exceptions.EmptyNameException;
import exceptions.NameContainsSpaceException;

import java.util.Vector;

public class Game extends Thread{
    final Vector<Player> players;

    public Game() {
        this.players = new Vector<>();
    }

    public synchronized void deletePlayer(Player player) {
        players.remove(player);
    }

}
