package test;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.GoalBoulders;
import unsw.dungeon.Player;
import unsw.dungeon.Boulder;
import unsw.dungeon.Switch;
import unsw.dungeon.Treasure;

public class TestSwitch {
    private Dungeon dungeon = new Dungeon(4, 4);
    private Player player = new Player(dungeon, 0, 1);
    private GoalBoulders goal = new GoalBoulders();
    Switch s = new Switch(2, 1);

    public void initilise() {
        dungeon.setGoal(goal);
        dungeon.setPlayer(player);
        dungeon.addEntity(s);
    }

    /**
     * Given a boulder is pushed onto a floor switch. 
     * Then the floor switch is triggered. 
     * 
     * Given a boulder is pushed off a floor switch. 
     * Then the floor switch is untriggered
     */
    @Test
    public void testSwitchTriggered() {
        initilise();
        Boulder b = new Boulder(1, 1);
        dungeon.addEntity(b);
        assertEquals(s.isTriggered(), false);
        player.moveBoulder("right");
        assertEquals(s.isTriggered(), true);
        
        player.moveRight();
        player.moveBoulder("right");
        assertEquals(s.isTriggered(), false);
    }

    /**
     * Given a floor switch has no entities on it. 
     * When a player tries to go over the floor switch. 
     * Then the player goes through.
     */
    @Test
    public void testPlayerOnSwitch() {
        initilise();
        player.moveRight();
        player.moveRight();
        assertEquals(player.isOn(s), true);
        player.moveRight();
        assertEquals(player.getX(), 3);
        assertEquals(player.getY(), 1);
    }

    /**
     * Given a floor switch has no entities on it. When an enemy tries to go over the floor switch. Then the enemy goes through.
     */
    //TODO: add tests

    /**
     * There are other entities appearing on top of a floor switch. 
     */
    //TODO: add entities test

    @Test
    public void testSwitchGoal() {
        initilise();
        Boulder b = new Boulder(1, 1);
        dungeon.addEntity(b);
        player.moveBoulder("right");
        player.complete();
        assertEquals(dungeon.getComplete(), true);
    }

}