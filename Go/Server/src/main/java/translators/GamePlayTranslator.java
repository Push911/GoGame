package translators;

import gameLogic.Colour;
import gameLogic.Player;
import gameLogic.goban.Goban;
import gameLogic.goban.Field;
import gameLogic.goban.FieldType;
import gameLogic.goban.MoveState;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


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


    public void confirmMove(Player p) {
        p.sendMessage("MOVEOK");
    }

    public void rejectMove(Player p, MoveState ms) {
        String reason = "";
        if (ms.equals(MoveState.KO)) reason = "KO";
        else if (ms.equals(MoveState.SUICIDMOVE)) reason = "SUICIDAL";
        else if (ms.equals(MoveState.OCCUPIEDFIELD)) reason = "NOTEMPTY";
        p.sendMessage("WRONGMOVE " + reason);
    }

    public void rejectMoveAttempt(Player p) {
        p.sendMessage("CANNOTMOVENOW");
    }

    public void sendOpponentsMove(Player p, int x, int y, Set<Field> removed) {
        String message = "OPPOMOVE " + String.valueOf(x) + "," + String.valueOf(y) +
                ":" + createRemovedStonesMessage(removed);
        p.sendMessage(message);
    }


    private String createRemovedStonesMessage(Set<Field> removed) {
        StringBuilder message = new StringBuilder("REMOVED ");
        if (removed != null && !removed.isEmpty()) {

            for (Field field : removed) {
                message.append(String.valueOf(field.getX())).append(",").append(String.valueOf(field.getY())).append(" ");
            }
        } else message.append("NONE");
        return message.toString();
    }


    public void sendRemovedStones(Player p, Set<Field> removed) { p.sendMessage(createRemovedStonesMessage(removed)); }

    public void sendOpponentsMove(Player p) {
        p.sendMessage("OPPOPASS");
    }

    public void sendChooseDead(Player p) {
        p.sendMessage("CHOOSEDEAD");
    }

    public void sendGameStopped(Player p) {
        p.sendMessage("GAMESTOPPED");
    }

    public HashSet<Field> getLastDeadSuggestion(Goban b) {
        if (lastDeadSuggestion != null) {
            HashSet<Field> points = new HashSet<>();
            String input = lastDeadSuggestion;
            input = input.replaceFirst("DEADSUGGESTION ", "");
            if (!input.trim().equals("NONE")) {
                String[] pairs = input.split(" ");
                for (String string : pairs) {
                    String[] pair = string.split(",");
                    Field p = new Field(Integer.parseInt(pair[0].trim()), Integer.parseInt(pair[1].trim()), FieldType.EMPTY, b);
                    points.add(p);
                }
            }
            return points;
        }
        return null;
    }

    public HashMap<Point, Colour> getLastTerritorySuggestion() {
        HashMap<Point, Colour> fields = new HashMap<>();

        String input = lastTerritorySuggestion;
        String[] inputs = input.replaceFirst("TERRITORYSUGGESTION ", "").split(":");

        if (!inputs[0].replaceFirst("BLACK", "").trim().startsWith("NONE")) {
            String[] blackFields = inputs[0].replaceFirst("BLACK", "").trim().split(" ");
            for (String pair : blackFields) {
                String[] coords = pair.split(",");
                Point p = new Point(Integer.parseInt(coords[0].trim()), Integer.parseInt(coords[1].trim()));
                fields.put(p, Colour.BLACK);
            }
        }

        if (!inputs[1].replaceFirst("WHITE", "").trim().startsWith("NONE")) {
            String[] whiteFields = inputs[1].replaceFirst("WHITE", "").trim().split(" ");
            for (String pair : whiteFields) {
                String[] coords = pair.split(",");
                Point p = new Point(Integer.parseInt(coords[0].trim()), Integer.parseInt(coords[1].trim()));
                fields.put(p, Colour.WHITE);
            }
        }

        return fields;
    }

    public void setLastTerritorySuggestion(String message) {
        lastTerritorySuggestion = message;
    }

    public void sendChooseTerritory(Player p) {
        p.sendMessage("SETTERRITORY");
    }

    public void sendDeadOK(Player player) {
        String message = lastDeadSuggestion.replaceFirst("DEADSUGGESTION", "DEADOK");
        player.sendMessage(message);
    }

    public void setLastDeadSuggestion(String message) {
        lastDeadSuggestion = message;

    }

    public void sendResults(double black, double white) {
        notifyBoth("THEEND BLACK " + String.valueOf(black) + " : " + "WHITE " + String.valueOf(white));
    }

    public void sendStats(int black, int white) {
        notifyBoth("CAPTURED BLACK " + String.valueOf(black) + " : " + "WHITE " + String.valueOf(white));
    }

    public void sendSurrender(Player p) {
        if (p == black) white.sendMessage("YOULOOSE");
        else black.sendMessage("YOULOOSE");
    }

    public void sendResume(Player p) {
        p.sendMessage("RESUMEGAME");
    }

}