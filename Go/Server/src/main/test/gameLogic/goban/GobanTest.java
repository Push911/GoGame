package gameLogic.goban;

import exceptions.FieldOutOfGobanException;
import gameLogic.Colour;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class GobanTest {

    private Goban gobanUnderTest;

    @Before
    public void setUp() {
        gobanUnderTest = new Goban(0);
    }

    @Test
    public void testCheckIfMovePossible() {
        // Setup

        // Run the test
        final MoveState result = gobanUnderTest.checkIfMovePossible(Colour.BLACK, 0, 0);

        // Verify the results
        assertEquals(MoveState.CONFIRMED, result);
    }

    @Test
    public void testPutStone() {
        // Setup

        // Run the test
        gobanUnderTest.putStone(Colour.BLACK, 0, 0);

        // Verify the results
    }

    @Test
    public void testGetFieldType() throws Exception {
        // Setup

        // Run the test
        final FieldType result = gobanUnderTest.getFieldType(0, 0);

        // Verify the results
        assertEquals(FieldType.BLACK, result);
    }

    @Test(expected = FieldOutOfGobanException.class)
    public void testGetFieldType_ThrowsFieldOutOfGobanException() throws Exception {
        // Setup

        // Run the test
        gobanUnderTest.getFieldType(0, 0);
    }

    @Test
    public void testGetField() {
        // Setup
        final Field expectedResult = new Field(0, 0, FieldType.BLACK, new Goban(0));

        // Run the test
        final Field result = gobanUnderTest.getField(0, 0);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testUpdate() {
        // Setup
        final Field lastMove = new Field(0, 0, FieldType.BLACK, new Goban(0));
        final Set<Field> expectedResult = new HashSet<>();

        // Run the test
        final Set<Field> result = gobanUnderTest.update(lastMove);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testRemoveStones() {
        // Setup
        final Set<Field> fields = new HashSet<>();

        // Run the test
        gobanUnderTest.removeStones(fields);

        // Verify the results
    }

    @Test
    public void testSetTerritories() {
        // Setup
        final HashMap<Point, Colour> territories = new HashMap<>();

        // Run the test
        gobanUnderTest.setTerritories(territories);

        // Verify the results
    }
}
