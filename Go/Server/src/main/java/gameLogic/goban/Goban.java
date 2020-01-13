package gameLogic.goban;

import gameLogic.Colour;
import exceptions.FieldOutOfGobanException;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class Goban {
    private final int size;
    private final Field[][] goban;
    private final StoneGroupSet StoneGroups;
    private Field lastCaptured = null;
    private Field lastMove = null;
    private int whiteCaptured = 0;
    private int blackCaptured = 0;
    private int whiteTerritory = 0;
    private int blackTerritory = 0;


    public Goban(int n) {
        goban = new Field[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                goban[i][j] = new Field(i, j, FieldType.EMPTY, this);
        }
        this.size = n;
        StoneGroups = new StoneGroupSet();
    }


    public MoveState checkIfMovePossible(Colour c, int x, int y) {
        if (isOnGoban(x, y)) {
            if (isOnGoban(x, y) && !goban[x][y].getType().equals(FieldType.EMPTY))
                return MoveState.OCCUPIEDFIELD;
            Field move = new Field(x, y, ((c.equals(Colour.BLACK)) ? FieldType.BLACK : FieldType.WHITE), this);
            if (StoneGroups.checkIfSuicidal(move)) {
                return MoveState.SUICIDMOVE;
            }

            if (lastCaptured == null) return MoveState.CONFIRMED;
            Field f = StoneGroups.checkForKo(move);
            if (f == null) return MoveState.CONFIRMED;
            if (f.equals(lastMove)) return MoveState.KO;
            return MoveState.CONFIRMED;
        }
        return MoveState.OCCUPIEDFIELD;
    }


    public void putStone(Colour c, int x, int y) {
        if (checkIfMovePossible(c, x, y) == MoveState.CONFIRMED) {
            goban[x][y].setType((c == Colour.BLACK) ? FieldType.BLACK : FieldType.WHITE);
        }
    }


    public FieldType getFieldType(int x, int y) throws FieldOutOfGobanException {
        if (!isOnGoban(x, y)) throw new FieldOutOfGobanException("Field would be outside goban");
        return goban[x][y].getType();
    }


    public Field getField(int x, int y) {
        if (!isOnGoban(x, y)) return null;
        return goban[x][y];
    }


    private boolean isOnGoban(int x, int y) {
        if (x >= 0 && x < size && y >= 0 && y < size)
            return true;
        return false;
    }


    public Set<Field> update(Field lastMove) {
        this.lastMove = lastMove;
        return StoneGroups.updateGroupsAfterMove(lastMove);
    }


    private void updateStatistics(Field field) {
        if (field.getType().equals(FieldType.BLACK)) blackCaptured++;
        else if (field.getType().equals(FieldType.WHITE)) whiteCaptured++;
    }


    synchronized public void removeStones(Set<Field> fields) {
        Iterator<Field> it = fields.iterator();
        Field f;

        if (fields.size() == 1) {
            f = it.next();
            lastCaptured = f;
            updateStatistics(goban[f.getX()][f.getY()]);
            goban[f.getX()][f.getY()].setEmpty();
        } else lastCaptured = null;

        while (it.hasNext()) {
            f = it.next();
            updateStatistics(goban[f.getX()][f.getY()]);
            goban[f.getX()][f.getY()].setEmpty();
        }
    }


    public void setTerritories(HashMap<Point, Colour> territories) {
        Set<Point> points = territories.keySet();
        for (Point point : points) {
            if (territories.get(point).equals(Colour.BLACK)) blackTerritory++;
            else whiteTerritory++;
        }
    }


    public int getWhiteTerritory() {
        return whiteTerritory;
    }

    public int getBlackTerritory() {
        return blackTerritory;
    }

    public int getWhiteCaptured() {
        return whiteCaptured;
    }

    public int getBlackCaptured() {
        return blackCaptured;
    }


}