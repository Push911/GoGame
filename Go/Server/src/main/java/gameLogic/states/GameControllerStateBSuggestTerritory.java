package gameLogic.states;

import gameLogic.Colour;
import gameLogic.GameController;
import gameLogic.Player;

import java.awt.*;
import java.util.HashMap;

public class GameControllerStateBSuggestTerritory implements GameControllerState {

    private final GameController gameController;

    public GameControllerStateBSuggestTerritory(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void makeMove(Player p, int x, int y) { }

    public void makeMove() { }

    @Override
    public boolean makeMove(Player player, boolean wasPassed) { return false; }

    @Override
    public void sendProposal(Player player, String message) {
        if (player == gameController.getBlack() && message.startsWith("TERRITORYSUGGESTION")) {
            gameController.getTranslator().setLastTerritorySuggestion(message);
            gameController.getWhite().sendMessage(message);
            gameController.setState(new GameControllerStateWSuggestTerritory(gameController));
        }
    }

    @Override
    public void reachAgreement(Player player) {
        if (player == gameController.getBlack()) {
            HashMap<Point, Colour> territories = gameController.getTranslator().getLastTerritorySuggestion();
            if (territories != null) {
                gameController.getGoban().setTerritories(territories);
                gameController.endGame();
            }
        }
    }

}