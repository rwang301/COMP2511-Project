package test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.Player;
import unsw.dungeon.Treasure;
import unsw.dungeon.Component;
import unsw.dungeon.GoalTreasure;

public class TestTreasure {
    private Dungeon dungeon = new Dungeon(4, 4);
    private Player player = new Player(dungeon, 0, 0);
    private Treasure treasure = new Treasure(0, 1);
    private Treasure treasure2 = new Treasure(0, 2);
    private Component goalTreasure = new GoalTreasure();

    private void initilise() {
        dungeon.setPlayer(player);
        dungeon.setGoal(goalTreasure);
        dungeon.addEntity(treasure);
    }

    /**
     * Given a player picked up a treasure. Then it disappears. 
     */
    @Test
    public void testTreasurePickUp() {
        initilise();
        assertEquals(dungeon.getEntities().contains(treasure), true);
        player.moveDown();
        assertEquals(dungeon.getEntities().contains(treasure), false);
    }

    /**
     * Given a player has picked up all the treasure in a dungeon. 
     * Then the goal of collecting all treasure is completed. 
     */
    @Test
    public void testTreasureGoal() {
        initilise();
        dungeon.addEntity(treasure2);
        assertEquals(dungeon.getComplete(), false);
        player.moveDown();
        assertEquals(dungeon.getComplete(), false);
        player.moveDown();
        assertEquals(dungeon.getComplete(), true);
    }
}