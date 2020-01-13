package gameLogic;

import gameLogic.goban.Goban;
import gameLogic.states.*;
import gameLogic.states.GameControllerStateBMove;
import gameLogic.states.GameControllerStateWMove;
import translators.GamePlayTranslator;

import java.util.Random;


public class GameController extends Thread {
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


    @Override
    public void run() {
        translator.notifyGameStart();
        state = new GameControllerStateBMove(this);
    }


    public void setState(GameControllerState state) {
        this.state = state;
    }


    public void makeMove(Player p, int x, int y) {
        state.makeMove(p, x, y);
        wasPassed = false;
    }


    public void passMove(Player player) {
        if (state.makeMove(player, wasPassed))
            wasPassed = true;
    }


    public void sendProposal(Player player, String message) {
        state.sendProposal(player, message);
    }


    public void acceptProposal(Player player) {
        state.reachAgreement(player);
    }


    public void resumeGame(Player player) {
        translator.setLastDeadSuggestion(null);
        translator.setLastTerritorySuggestion(null);
        translator.setLastDeadSuggestion(null);
        if (player == black) {
            state = new GameControllerStateWMove(this);
            translator.sendResume(white);
        } else {
            state = new GameControllerStateBMove(this);
            translator.sendResume(black);
        }
    }


    private double[] calculateResults() {
        double blackPoints = goban.getBlackTerritory() - goban.getBlackCaptured();
        double whitePoints = goban.getWhiteTerritory() - goban.getWhiteCaptured() + komi;

        return new double[]{blackPoints, whitePoints};
    }


    public void endGame(Player p) {
        setState(new GamePlayStateGameEnd());
        translator.sendSurrender(p);
        black.setNotInGame();
        white.setNotInGame();
    }


    public void endGame() {
        setState(new GamePlayStateGameEnd());

        double[] results = calculateResults();
        getTranslator().sendResults(results[0], results[1]);

        black.setNotInGame();
        white.setNotInGame();
    }


    public Player getBlack() {
        return black;
    }


    public Player getWhite() {
        return white;
    }


    public GamePlayTranslator getTranslator() {
        return translator;
    }


    public Goban getGoban() {
        return goban;
    }

}