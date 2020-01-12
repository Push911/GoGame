package translators;

import gameLogic.Game;
import server.ClientHandler;

public class ClientMessagesTranslator {

    private final ClientHandler clientHandler;
    private final Game game;

    public ClientMessagesTranslator(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        game = clientHandler.getGame();
    }

    public void processIncomingMessage(String message) {
    }

    public void sendInvitation(String player) {

    }

    public void sendRefusal() {

    }
}
