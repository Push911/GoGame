package gameLogic.states;

import gameLogic.GameController;
import gameLogic.Player;
import gameLogic.goban.Field;

import java.util.HashSet;

public class GameControllerStateWMarkDead implements GameControllerState {
    private final GameController gameController;

    public GameControllerStateWMarkDead(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void makeMove(Player p, int x, int y) { }

    @Override
    public boolean makeMove(Player player, boolean wasPassed) {
        return false;
    }

    @Override
    public void sendProposal(Player player, String message) {
        if (player == gameController.getWhite() && message.startsWith("DEADSUGGESTION")) {
            gameController.getTranslator().setLastDeadSuggestion(message);
            gameController.getBlack().sendMessage(message);
            gameController.setState(new GameControllerStateBMarkDead(gameController));
        }
    }

    @Override
    public void reachAgreement(Player player) {
        if (player == gameController.getWhite()) {
            HashSet<Field> toRemove = gameController.getTranslator().getLastDeadSuggestion(gameController.getGoban());
            if (toRemove != null) {
                gameController.getGoban().removeStones(toRemove);
                gameController.setState(new GameControllerStateBSuggestTerritory(gameController));
                gameController.getTranslator().sendChooseTerritory(gameController.getBlack());
                gameController.getTranslator().sendDeadOK(player);
                gameController.getTranslator().sendStats(gameController.getGoban().getBlackCaptured(), gameController.getGoban().getWhiteCaptured());
            }
        }
    }

}
