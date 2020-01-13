package gameLogic.states;

import gameLogic.Colour;
import gameLogic.GameController;
import gameLogic.Player;

import java.awt.*;
import java.util.HashMap;


public class GameControllerStateWSuggestTerritory implements GameControllerState {

    private final GameController gameController;

    public GameControllerStateWSuggestTerritory(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void sendProposal(Player player, String message) {
        if (player == gameController.getWhite() && message.startsWith("TERRITORYSUGGESTION")) {
            gameController.getTranslator().setLastTerritorySuggestion(message);
            gameController.getBlack().sendMessage(message);
            gameController.setState(new GameControllerStateBSuggestTerritory(gameController));
        }
    }

    @Override
    public void reachAgreement(Player player) {
        if (player == gameController.getWhite()) {
            HashMap<Point, Colour> territories = gameController.getTranslator().getLastTerritorySuggestion();
            if (territories != null) {
                gameController.getGoban().setTerritories(territories);
                gameController.endGame();
            }
        }
    }

    @Override
    public void makeMove(Player p, int x, int y) {
    }

    @Override
    public boolean makeMove(Player player, boolean wasPassed) {
        return false;
    }


}