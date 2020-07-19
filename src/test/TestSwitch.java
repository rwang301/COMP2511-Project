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
import unsw.dungeon.Component;

public class TestSwitch {
    private Dungeon dungeon = new Dungeon(4, 4);
    private Player player = new Player(dungeon, 0, 1);
    private Component goal = new GoalBoulders();
    Switch s = new Switch(2, 1);

    public void initialise() {
        dungeon.setGoal(goal);
        dungeon.setPlayer(player);
        dungeon.addEntity(s);
    }

    /**
     * Given a boulder is pushed onto a floor switch.
     * Then the floor switch is triggered.
     */
    @Test
    public void testSwitchTriggered() {
        initialise();
        Boulder b = new Boulder(1, 1);
        dungeon.addEntity(b);
        assertFalse(s.isTriggered());
        player.moveBoulder("right");
        assertTrue(s.isTriggered());
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
        assertFalse(s.isTriggered());
    }

    /**
     * Given a floor switch has no entities on it.
     * When a player tries to go over the floor switch.
     * Then the player goes through
     */
    @Test
    public void testPlayerOnSwitch() {
        initialise();
        player.moveRight();
        player.moveRight();
        assertTrue(player.isOn(s));
        player.moveRight();
        assertEquals(player.getX(), 3);
        assertEquals(player.getY(), 1);
    }

    /**
     * Given a floor switch has no entities on it.
     * When an enemy tries to go over the floor switch.
     * Then the enemy goes through.
     */
    //TODO: add tests

    /**
     * There are other entities appearing on top of a floor switch. 
     */
    @Test
    public void testSwitchGoal() {
        initialise();
        Boulder b = new Boulder(1, 1);
        dungeon.addEntity(b);
        assertFalse(dungeon.isComplete());
        player.moveBoulder("right");
        assertTrue(dungeon.isComplete());

        //TODO: add entities test
    }
}