package gameLogic;

import server.ClientHandler;
import translators.ClientMessagesTranslator;

public class Player {
    private final ClientHandler handler;
    private final ClientMessagesTranslator translator;
    private GameController gameController = null;
    private final String name;
    private boolean inGame = false;


    public Player(String name, ClientHandler handler) {
        this.name = name;
        this.handler = handler;
        this.translator = handler.getTranslator();
    }

    public String getName() {
        return name;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame() {
        inGame = true;
    }

    public void setNotInGame() {
        inGame = false;
    }

    public void makeMove(int x, int y) {
        if (gameController != null) gameController.makeMove(this, x, y);
    }

    public void passMove() {
        if (gameController != null) gameController.passMove(this);
    }

    public void sendMessage(String message) {
        handler.send(message);
    }

    public void beInvited(String player) {
        translator.sendInvitation(player);
    }

    public void beRefused() {
        translator.sendRefusal();
    }

    public void sendProposal(String message) {
        gameController.sendProposal(this, message);
    }

    public void acceptSuggestion() {
        gameController.acceptProposal(this);
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}