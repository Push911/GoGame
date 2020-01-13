package gameLogic.goban;

import java.util.HashSet;
import java.util.Set;


public class StoneGroup implements GobanUpdater {
    private final Set<Field> group;


    public StoneGroup(Field field) {
        group = new HashSet<>();
        group.add(field);
        FieldType type = field.getType();
    }


    boolean contains(Field field) {
        for (Field f : group) {
            if (f.equals(field)) return true;
        }
        return false;
    }


    void add(Field field) {
        group.add(field);
    }


    Set<Field> getFields() {
        return group;
    }


    @Override
    public int checkLiberties() {
        int liberties = 0;
        for (Field field : group) {
            liberties += field.checkLiberties();
        }

        return liberties;
    }


    @Override
    public void setEmpty() {
        group.forEach(Field::setEmpty);
    }


}