package gameLogic;

import server.ClientHandler;
import exceptions.EmptyNameException;
import exceptions.NameContainsSpaceException;

import java.util.Vector;


public class Game extends Thread {
    final Vector<Player> players;


    public Game() {
        this.players = new Vector<>();
    }


    public synchronized Vector<String> getNotInGamePlayersNames() {
        Vector<String> notInGamePlayersNames = new Vector<>();

        for (Player player : players) {
            if (!player.isInGame())
                notInGamePlayersNames.add(player.getName());
        }
        return notInGamePlayersNames;
    }



    public boolean addPlayer(String name, ClientHandler handler) throws NameContainsSpaceException, EmptyNameException {

        checkIfNameCorrect(name);

        if (!isNameTaken(name)) {
            Player p = new Player(name, handler);
            players.add(p);
            handler.setPlayer(p);
            return true;
        }
        return false;
    }


    synchronized private boolean isNameTaken(String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) return true;
        }
        return false;
    }


    public Player getPlayerByName(String name) {
        for (Player player : players) {
            if (player.getName().equals(name))
                return player;
        }

        return null;
    }


    synchronized public boolean chooseOpponent(String opponentsName, Player player) {
        if (getNotInGamePlayersNames().contains(opponentsName)) {
            Player opponent = getPlayerByName(opponentsName);
            GameController gp = new GameController(player, opponent);
            gp.start();
            player.setInGame();
            opponent.setInGame();
            return true;
        }
        return false;
    }


    public synchronized boolean inviteOpponent(String opponentsName, String inviter) {
        if (getNotInGamePlayersNames().contains(opponentsName)) {
            Player opponent = getPlayerByName(opponentsName);
            if (opponent == null) return false;
            opponent.beInvited(inviter);
            return true;
        }
        return false;
    }


    public synchronized void deletePlayer(Player player) {
        players.remove(player);
    }


    private void checkIfNameCorrect(String name) throws NameContainsSpaceException, EmptyNameException {
        if (name.contains(" ")) throw new NameContainsSpaceException(name);
        if (name.equals("")) throw new EmptyNameException();
    }


    public boolean refuseInvitation(String opponentsName) {
        if (getNotInGamePlayersNames().contains(opponentsName)) {
            Player opponent = getPlayerByName(opponentsName);
            if (opponent == null) return false;
            opponent.beRefused();
            return true;
        }
        return false;
    }

}