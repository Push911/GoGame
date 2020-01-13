package gameLogic.states;

import gameLogic.Colour;
import gameLogic.GameController;
import gameLogic.Player;
import gameLogic.goban.Field;
import gameLogic.goban.FieldType;
import gameLogic.goban.MoveState;

import java.util.Set;


public class GameControllerStateWMove implements GameControllerState {
    private final GameController gameController;


    public GameControllerStateWMove(GameController gameController) {
        this.gameController = gameController;
    }


    @Override
    synchronized public void makeMove(Player p, int x, int y) {
        if (p == gameController.getWhite()) {
            MoveState moveState = gameController.getGoban().checkIfMovePossible(Colour.WHITE, x, y);
            if (moveState.equals(MoveState.CONFIRMED)) {
                gameController.getGoban().putStone(Colour.WHITE, x, y);

                Set<Field> removed = gameController.getGoban().update(new Field(x, y, FieldType.WHITE, gameController.getGoban()));
                gameController.getTranslator().confirmMove(p);
                gameController.getTranslator().sendOpponentsMove(gameController.getBlack(), x, y, removed);
                gameController.getTranslator().sendRemovedStones(gameController.getWhite(), removed);
                gameController.getGoban().removeStones(removed);
                gameController.getTranslator().sendStats(gameController.getGoban().getBlackCaptured(), gameController.getGoban().getWhiteCaptured());
                gameController.setState(new GameControllerStateBMove(gameController));
            } else gameController.getTranslator().rejectMove(p, moveState);
        } else gameController.getTranslator().rejectMoveAttempt(p);
    }

    public boolean makeMove(Player p, boolean lastWasPass) {
        if (p == gameController.getWhite()) {
            if (lastWasPass) {
                gameController.getTranslator().sendChooseDead(gameController.getBlack());
                gameController.getTranslator().sendGameStopped(p);
                gameController.setState(new GameControllerStateBMarkDead(gameController));
            } else {
                gameController.getTranslator().sendOpponentsMove(gameController.getBlack());
                gameController.setState(new GameControllerStateBMove(gameController));
            }
            return true;
        } else {
            gameController.getTranslator().rejectMoveAttempt(p);
            return false;
        }

    }

    @Override
    public void sendProposal(Player player, String message) {
    }

    @Override
    public void reachAgreement(Player player) {
    }

}