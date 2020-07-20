package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.GoalBoulders;
import unsw.dungeon.Player;
import unsw.dungeon.Boulder;
import unsw.dungeon.Switch;
import unsw.dungeon.Treasure;
import unsw.dungeon.Component;

public class TestSwitch {
    private Dungeon dungeon = new Dungeon(4, 4);
    private Player player = new Player(dungeon, 0, 1);
    private Boulder boulder = new Boulder(1, 1);
    private Switch floorSwitch = new Switch(2, 1);
    private Component goalBoulders = new GoalBoulders();

    public void initialise() {
        dungeon.setGoal(goalBoulders);
        dungeon.setPlayer(player);
        dungeon.addEntity(player);
        dungeon.addEntity(boulder);
        dungeon.addEntity(floorSwitch);
    }

    /**
     * Given a boulder is pushed onto a floor switch.
     * Then the floor switch is triggered.
     */
    @Test
    public void testSwitchTriggered() {
        initialise();
        assertFalse(floorSwitch.isTriggered());
        assertFalse(dungeon.isComplete());
        player.moveBoulder("right");
        assertTrue(floorSwitch.isTriggered());
        assertTrue(dungeon.isComplete());
    }

    /**
     * Given a boulder is pushed off a floor switch.
     * Then the floor switch is untriggered.
     */
    @Test
    public void testSwitchUntriggered() {
        testSwitchTriggered();
        player.moveRight();
        player.moveBoulder("right");
        assertFalse(floorSwitch.isTriggered());
    }

    /**
     * Given a floor switch has no entities on it.
     * When a player tries to go over the floor switch.
     * Then the player goes through
     */
    @Test
    public void testPlayerOnSwitch() {
        initialise();
        player.moveDown();
        player.moveRight();
        player.moveRight();
        player.moveUp();
        assertTrue(player.isOn(floorSwitch));

        player.moveRight();
        assertEquals(player.getX(), 3);
        assertEquals(player.getY(), 1);
    }

    /**
     * There are other entities appearing on top of a floor switch.
     */
    @Test
    public void testSwitchGoal() {
        initialise();
        Treasure treasure = new Treasure(2, 1);
        dungeon.addEntity(treasure);
        player.moveBoulder("right");
        assertEquals(player.getX(), 0);
        assertEquals(player.getY(), 1);
        assertEquals(boulder.getX(), 1);
        assertEquals(boulder.getY(), 1);
    }
}