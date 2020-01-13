package gameLogic.states;

import gameLogic.Player;


public class GameControllerStateEndGame implements GameControllerState {

    @Override
    public void makeMove(Player p, int x, int y) { }

    public void makeMove() { }

    @Override
    public boolean makeMove(Player player, boolean wasPassed) {
        return false;
    }

    @Override
    public void sendProposal(Player player, String message) { }

    @Override
    public void reachAgreement(Player player) { }
}