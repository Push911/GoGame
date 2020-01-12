package gameLogic.goban;

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
}
