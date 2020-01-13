package gameLogic.states;

import gameLogic.GameController;
import gameLogic.Player;
import gameLogic.goban.Field;

import java.util.HashSet;

public class GameControllerStateBMarkDead implements GameControllerState {

    private final GameController gameController;

    public GameControllerStateBMarkDead(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void makeMove(Player p, int x, int y) {

    }

    @Override
    public boolean makeMove(Player player, boolean wasPassed) {
        return false;
    }

    @Override
    public void sendProposal(Player player, String message) {
        if (player == gameController.getBlack() && message.startsWith("DEADSUGGESTION")) {
            gameController.getTranslator().setLastDeadSuggestion(message);
            gameController.getWhite().sendMessage(message);
            gameController.setState(new GameControllerStateWMarkDead(gameController));
        }
    }

    @Override
    public void reachAgreement(Player player) {
        if (player == gameController.getBlack()) {
            HashSet<Field> toRemove = gameController.getTranslator().getLastDeadSuggestion(gameController.getGoban());
            if (toRemove != null) {
                gameController.getGoban().removeStones(toRemove);
                gameController.setState(new GameControllerStateWSuggestTerritory(gameController));
                gameController.getTranslator().sendChooseTerritory(gameController.getWhite());
                gameController.getTranslator().sendDeadOK(player);
                gameController.getTranslator().sendStats(gameController.getGoban().getBlackCaptured(), gameController.getGoban().getWhiteCaptured());
            }
        }
    }
}
