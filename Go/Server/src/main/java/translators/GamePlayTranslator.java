package translators;

import gameLogic.Player;

public class GamePlayTranslator {
    private final Player black;
    private final Player white;
    private String lastDeadSuggestion;
    private String lastTerritorySuggestion;

    public GamePlayTranslator(Player black, Player white) {
        this.black = black;
        this.white = white;
    }

    private void notifyBoth(String message) {
        black.sendMessage(message);
        white.sendMessage(message);
    }

    public void notifyGameStart() {
        black.sendMessage("GAMESTART BLACK");
        white.sendMessage("GAMESTART WHITE");
    }



}
