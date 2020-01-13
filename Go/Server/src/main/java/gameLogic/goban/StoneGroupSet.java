package gameLogic.goban;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


class StoneGroupSet {

    private final Set<StoneGroup> StoneGroups;

    public StoneGroupSet() {
        StoneGroups = new HashSet<>();
    }

    private StoneGroup findGroupByStone(Field field) {
        for (StoneGroup group : StoneGroups) {
            if (group.contains(field)) return group;
        }
        return null;
    }


    public Set<Field> updateGroupsAfterMove(Field lastMove) {
        addFieldToGroup(lastMove);
        return handleConsequences(lastMove);
    }

    private Set<Field> handleConsequences(Field lastMove) {
        Set<Field> neighbours = lastMove.getNeighbours();
        Set<StoneGroup> groupsToCheck = new HashSet<>();
        Set<Field> removedGroup = new HashSet<>();

        for (Field neighbour : neighbours) {
            if (!neighbour.getType().equals(lastMove.getType()) && !neighbour.getType().equals(FieldType.EMPTY)) {
                StoneGroup g = findGroupByStone(neighbour);
                if (g != null) groupsToCheck.add(g);
            }
        }
        for (StoneGroup groupToRemove : groupsToCheck) {
            if (groupToRemove.checkLiberties() == 0) {
                removedGroup.addAll(groupToRemove.getFields());
                groupToRemove.setEmpty();
                StoneGroups.remove(groupToRemove);
            }
        }
        return removedGroup;
    }

    private void createNewGroup(Field field) {
        StoneGroup g = new StoneGroup(field);
        StoneGroups.add(g);
    }


    private void joinGroups(StoneGroup firstGroup, StoneGroup secondGroup) {
        firstGroup.getFields().addAll(secondGroup.getFields());
        StoneGroups.remove(secondGroup);
    }


    private void addFieldToGroup(Field field) {
        Set<Field> neighbours = field.getNeighbours();
        Set<StoneGroup> neighbourGroups = new HashSet<>();

        for (Field neighbour : neighbours) {
            if (neighbour.getType().equals(field.getType())) {
                StoneGroup g = findGroupByStone(neighbour);
                if (g != null) neighbourGroups.add(g);
            }
        }

        if (neighbourGroups.isEmpty()) createNewGroup(field);
        else {
            Iterator<StoneGroup> stoneGroupIterator = neighbourGroups.iterator();
            StoneGroup groupsToJoin = stoneGroupIterator.next();
            groupsToJoin.add(field);
            while (stoneGroupIterator.hasNext()) {
                StoneGroup nextStoneGroup = stoneGroupIterator.next();
                joinGroups(groupsToJoin, nextStoneGroup);
            }
        }
    }


    public Field checkForKo(Field move) {
        Set<Field> neighbours = move.getNeighbours();
        Set<StoneGroup> suspected = new HashSet<>();
        Set<Field> toRemove = new HashSet<>();

        for (Field neighbour : neighbours) {
            if (!neighbour.getType().equals(move.getType()) && !neighbour.getType().equals(FieldType.EMPTY)) {
                StoneGroup g = findGroupByStone(neighbour);
                if (g != null && g.getFields().size() == 1) suspected.add(g);
            }
        }
        for (StoneGroup stoneGroup : suspected) {
            if (stoneGroup.checkLiberties() == 1) {
                toRemove.addAll(stoneGroup.getFields());
            }
        }

        if (toRemove.size() == 1) {
            Iterator<Field> it = toRemove.iterator();
            return it.next();
        }
        return null;
    }


    public boolean checkIfSuicidal(Field lastMove) {
        if (lastMove.checkLiberties() > 0) return false;

        Set<Field> neighbours = lastMove.getNeighbours();
        Set<StoneGroup> myGroups = new HashSet<>();
        HashMap<StoneGroup, Integer> opponentsGroups = new HashMap<>();
        int myColorNeighbours = 0;

        for (Field neighbour : neighbours) {
            if (neighbour.getType().equals(lastMove.getType())) {
                myColorNeighbours++;
                StoneGroup g = findGroupByStone(neighbour);
                if (g != null) myGroups.add(g);
            } else if (!neighbour.getType().equals(FieldType.EMPTY)) {
                StoneGroup g = findGroupByStone(neighbour);
                if (opponentsGroups.containsKey(g))
                    opponentsGroups.put(g, opponentsGroups.get(g) + 1);
                if (g != null) opponentsGroups.put(g, 1);
            }
        }
        int myGroupsLiberties = 0;
        for (StoneGroup g : myGroups) myGroupsLiberties += g.checkLiberties();
        if (myGroupsLiberties == myColorNeighbours) {
            for (StoneGroup opponentGroup : opponentsGroups.keySet()) {
                if (opponentGroup.checkLiberties() <= opponentsGroups.get(opponentGroup)) return false;
            }
            return true;
        }
        return false;
    }
}