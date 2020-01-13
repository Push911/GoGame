package gameLogic.states;

import gameLogic.Colour;
import gameLogic.GameController;
import gameLogic.Player;
import gameLogic.goban.Field;
import gameLogic.goban.FieldType;
import gameLogic.goban.MoveState;

import java.util.Set;

public class GameControllerStateBMove implements GameControllerState {
    private final GameController gameController;


    public GameControllerStateBMove(GameController gameController) {
        this.gameController = gameController;
    }


    @Override
    synchronized public void makeMove(Player p, int x, int y) {
        if (p == gameController.getBlack()) {
            MoveState moveState = gameController.getGoban().checkIfMovePossible(Colour.BLACK, x, y);
            if (moveState.equals(MoveState.CONFIRMED)) {
                gameController.getGoban().putStone(Colour.BLACK, x, y);

                Set<Field> removed = gameController.getGoban().update(new Field(x, y, FieldType.BLACK, gameController.getGoban()));
                gameController.getTranslator().confirmMove(p);
                gameController.getTranslator().sendOpponentsMove(gameController.getWhite(), x, y, removed);
                gameController.getTranslator().sendRemovedStones(gameController.getBlack(), removed);
                gameController.getGoban().removeStones(removed);
                gameController.getTranslator().sendStats(gameController.getGoban().getBlackCaptured(), gameController.getGoban().getWhiteCaptured());
                gameController.setState(new GameControllerStateWMove(gameController));
            } else gameController.getTranslator().rejectMove(p, moveState);
        } else gameController.getTranslator().rejectMoveAttempt(p);
    }


    public boolean makeMove(Player p, boolean lastWasPass) {
        if (p == gameController.getBlack()) {
            if (lastWasPass) {
                gameController.getTranslator().sendChooseDead(gameController.getWhite());
                gameController.getTranslator().sendGameStopped(p);
                gameController.setState(new GameControllerStateWMarkDead(gameController));

            } else {
                gameController.getTranslator().sendOpponentsMove(gameController.getWhite());
                gameController.setState(new GameControllerStateWMove(gameController));
            }
            return true;
        } else {
            gameController.getTranslator().rejectMoveAttempt(p);
            return false;
        }
    }

    @Override
    public void sendProposal(Player player, String message) { }

    @Override
    public void reachAgreement(Player player) { }
}