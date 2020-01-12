package gameLogic;

import gameLogic.goban.Goban;
import gameLogic.states.*;
import gameLogic.states.GameControllerStateBMove;
import gameLogic.states.GameControllerStateWMove;
import translators.GamePlayTranslator;

import java.util.Random;

import java.util.Random;

public class GameController {

    private static final int n = 19;
    private static final double komi = 6.5;
    private final Player black;
    private final Player white;
    private final Goban goban;
    private final GamePlayTranslator translator;
    private boolean wasPassed = false;
    private GameControllerState state;

    public GameController(Player first, Player second) {
        goban = new Goban(n);
        Random r = new Random();
        boolean firstBlack = r.nextBoolean();
        black = firstBlack ? first : second;
        white = firstBlack ? second : first;

        black.setGamePlay(this);
        white.setGamePlay(this);

        translator = new GamePlayTranslator(black, white);
    }

    public void makeMove(Player player, int x, int y) {
    }

    public void passMove(Player player) {

    }

    public void sendProposal(Player player, String message) {

    }

    public void acceptProposal(Player player) {

    }

    public void start() {

    }
}
