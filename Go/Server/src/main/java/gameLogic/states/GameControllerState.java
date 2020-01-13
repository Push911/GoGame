package gameLogic.states;

import gameLogic.Player;


public interface GameControllerState {

    void makeMove(Player p, int x, int y);

    boolean makeMove(Player player, boolean wasPassed);

    void sendProposal(Player player, String message);

    void reachAgreement(Player player);
}
