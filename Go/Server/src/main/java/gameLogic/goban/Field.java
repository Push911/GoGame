package gameLogic.goban;

import exceptions.FieldOutOfGobanException;

import java.util.HashSet;
import java.util.Set;


public class Field implements GobanUpdater {
    private static final int[] xDirections = {1, 0, -1, 0};
    private static final int[] yDirections = {0, 1, 0, -1};
    private final int x;
    private final int y;
    private FieldType type;
    private final Goban goban;


    public Field(int x, int y, FieldType type, Goban goban) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.goban = goban;
    }


    FieldType getType() {
        return type;
    }

    void setType(FieldType type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    @Override
    public int checkLiberties() {
        int liberties = 0;
        for (int i = 0; i < 4; i++) {
            FieldType t;
            try {
                t = goban.getFieldType(x + xDirections[i], y + yDirections[i]);
                if (t == FieldType.EMPTY) liberties++;
            } catch (FieldOutOfGobanException ignored) { }
        }
        return liberties;
    }


    @Override
    public void setEmpty() {
        type = FieldType.EMPTY;
    }

    public Set<Field> getNeighbours() {
        Set<Field> neighbours = new HashSet<>();

        for (int i = 0; i < 4; i++) {
            Field f = goban.getField(x + xDirections[i], y + yDirections[i]);
            if (f != null) neighbours.add(f);
        }
        return neighbours;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Field other = (Field) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return type.equals(other.type);
    }
}