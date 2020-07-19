package test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.Player;
import unsw.dungeon.Exit;
import unsw.dungeon.GoalExit;
import unsw.dungeon.GoalBoulders;
import unsw.dungeon.Switch;
import unsw.dungeon.Boulder;
import unsw.dungeon.Component;
import unsw.dungeon.Composite;
import unsw.dungeon.And;

public class TestExit {
    private Dungeon dungeon = new Dungeon(4, 4);
    private Player player = new Player(dungeon, 0, 0);
    private Component goalExit = new GoalExit();
    private Component goalBoulders = new GoalBoulders();
    Exit exit = new Exit(0, 1);
    Switch floorSwitch = new Switch(2, 1);
    Boulder boulder = new Boulder(1, 1);

    private void initialise() {
        dungeon.setPlayer(player);
        dungeon.setGoal(goalExit);
        dungeon.addEntity(exit);
    }

    private void initialiseGoals() {
        Composite and = new And(); 
        and.add(goalBoulders);
        and.add(goalExit);
        dungeon.addEntity(boulder);
        dungeon.addEntity(floorSwitch);
        dungeon.setGoal(and);
    }
    /**
     * Given getting to an exit is the only goal in a dungeon.
     * When a player goes through the exit. Then the game ends.  
    */

    @Test
    public void testExitGoal() {
        initialise();
        assertFalse(dungeon.isComplete());
        player.moveDown();
        assertTrue(dungeon.isComplete());
    }

    /**
     * Given a player has not completed all the other goals. 
     * When the player goes through the exit. Then nothing happens. 
     */
    @Test
    public void testGoalOrderNotComplete() {
        initialise();
        initialiseGoals();
        dungeon.setGoal(goalBoulders);
        player.moveDown();
        assertFalse(dungeon.isComplete());
        //TODO: add more goals
    }  

    /**
     * Given a player has completed all the other goals. 
     * When the player goes through the exit. Then the game ends.
     */
    @Test
    public void testGoalOrderComplete() {
        testGoalOrderNotComplete();
        player.moveBoulder("right");
        assertTrue(dungeon.isComplete());
    }
}