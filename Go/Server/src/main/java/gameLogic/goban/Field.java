package gameLogic.goban;

import exceptions.FieldOutOfGobanException;

public class Field implements GobanUpdater{
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

    }
}
