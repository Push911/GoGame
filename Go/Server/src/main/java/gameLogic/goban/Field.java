package gameLogic.goban;

public class Field {
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
}
