package translators;

import exceptions.EmptyNameException;
import exceptions.NameContainsSpaceException;
import gameLogic.Game;
import server.ClientHandler;

import java.util.Objects;


public class ClientMessagesTranslator {
    private final ClientHandler clientHandler;
    private final Game game;

    public ClientMessagesTranslator(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        game = clientHandler.getGame();
    }


    public void processIncomingMessage(String message) {
        String response = "";
        if (message.startsWith("CONNECTION OK")) {
            clientHandler.send("NewPlayerNickname");
            return;
        } else if (message.startsWith("USERNAME")) {
            try {
                if (game.addPlayer(message.replaceFirst("USERNAME ", ""), clientHandler))
                    response = "NickNameIsChecked";
                else
                    response = "NameIsTaken";
            } catch (NameContainsSpaceException | EmptyNameException e) {
                return;
            }
        } else if (message.startsWith("PlayerList")) {
            response = "PlayerList " + getListOfNotInGamePlayers();
        } else if (message.startsWith("OPPONENT")) {
            message = message.replaceFirst("OPPONENT ", "");
            if (game.inviteOpponent(message, clientHandler.getPlayer().getName())) return;
            else response = "CHOOSEOPPONENTAGAIN " + getListOfNotInGamePlayers();
        } else if (message.startsWith("INVAGREE")) {
            message = message.replaceFirst("INVAGREE ", "");
            if (game.chooseOpponent(message, clientHandler.getPlayer())) return;
            else response = "CHOOSEOPPONENTAGAIN " + getListOfNotInGamePlayers();
        } else if (message.startsWith("INVDECLINE")) {
            message = message.replaceFirst("INVDECLINE ", "");
            if (game.refuseInvitation(message)) return;
            else response = "CHOOSEOPPONENTAGAIN " + getListOfNotInGamePlayers();
        } else if (message.startsWith("MOVE")) {
            String[] coords = message.replaceFirst("MOVE ", "").split(" ");
            if (coords.length == 2) {
                clientHandler.getPlayer().makeMove(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
                return;
            } else if (coords[0].equals("PASS")) {
                clientHandler.getPlayer().passMove();
                return;
            }
        } else if (message.startsWith("SURRENDER ")) {
            clientHandler.getPlayer().getGameController().endGame(clientHandler.getPlayer());
            return;
        } else if (message.startsWith("DEADSUGGESTION")) {
            clientHandler.getPlayer().sendProposal(message);
            return;
        } else if (message.startsWith("TERRITORYSUGGESTION")) {
            clientHandler.getPlayer().sendProposal(message);
            return;
        } else if (message.startsWith("ACCEPT")) {
            clientHandler.getPlayer().acceptSuggestion();
            return;
        } else if (message.startsWith("DELETE ")) {
            message = message.replaceFirst("DELETE ", "");
            game.deletePlayer(game.getPlayerByName(message));
            return;
        } else if (message.startsWith("RESUME")) {
            clientHandler.getPlayer().getGameController().resumeGame(clientHandler.getPlayer());
        } else response = "UNKNOWNCOMMAND";
        clientHandler.send(response);
    }


    private String getListOfNotInGamePlayers() {
        StringBuilder b = new StringBuilder();
        String myName = clientHandler.getPlayer().getName();
        for (String name : game.getNotInGamePlayersNames()) {
            if (!Objects.equals(name, myName)) b.append(name).append(" ");
        }

        return b.toString();
    }


    public void sendInvitation(String player) {
        clientHandler.send("InviteFrom " + player);
    }

    public void sendRefusal() {
        clientHandler.send("PlayerDeclined");
    }


}