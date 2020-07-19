package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.Player;
import unsw.dungeon.Treasure;
import unsw.dungeon.Component;
import unsw.dungeon.GoalTreasure;

public class TestTreasure {
    private Dungeon dungeon = new Dungeon(4, 4);
    private Player player = new Player(dungeon, 0, 0);
    private Treasure treasure1 = new Treasure(0, 1);
    private Treasure treasure2 = new Treasure(0, 2);
    private Component goalTreasure = new GoalTreasure();

    private void initialise() {
        dungeon.setPlayer(player);
        dungeon.setGoal(goalTreasure);
        dungeon.addEntity(treasure1);
        dungeon.addEntity(treasure2);
    }

    /**
     * Given a player picked up a treasure.
     * Then it disappears.
     */
    @Test
    public void testTreasurePickUp() {
        initialise();
        assertTrue(dungeon.getEntities().contains(treasure1));
        player.moveDown();
        assertEquals(player.getTreasure(), 1);
        assertFalse(dungeon.getEntities().contains(treasure1));
    }

    /**
     * Given a player has picked up all the treasure in a dungeon.
     * Then the goal of collecting all treasure is completed.
     */
    @Test
    public void testTreasureGoal() {
        testTreasurePickUp();
        assertFalse(dungeon.isComplete());
        player.moveDown();
        assertEquals(player.getTreasure(), 2);
        assertTrue(dungeon.isComplete());
    }
}