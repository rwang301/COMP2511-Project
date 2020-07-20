package test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.Player;
import unsw.dungeon.Exit;
import unsw.dungeon.GoalExit;
import unsw.dungeon.GoalTreasure;
import unsw.dungeon.Or;
import unsw.dungeon.GoalBoulders;
import unsw.dungeon.GoalEnemies;
import unsw.dungeon.Switch;
import unsw.dungeon.Treasure;
import unsw.dungeon.Boulder;
import unsw.dungeon.Component;
import unsw.dungeon.Composite;
import unsw.dungeon.And;

public class TestExit {
    private Dungeon dungeon = new Dungeon(4, 4);
    private Player player = new Player(dungeon, 0, 0);
    private Exit exit = new Exit(0, 1);
    private Switch floorSwitch = new Switch(2, 1);
    private Boulder boulder = new Boulder(1, 1);
    private Treasure treasure = new Treasure(0, 2);
    private Component goalExit = new GoalExit();
    private Component goalBoulders = new GoalBoulders();
    private Component goalTreasure = new GoalTreasure();
    private Component goalEnemies = new GoalEnemies();

    private void initialise() {
        dungeon.setPlayer(player);
        dungeon.setGoal(goalExit);
        dungeon.addEntity(exit);
        dungeon.addEntity(floorSwitch);
        dungeon.addEntity(boulder);
        dungeon.addEntity(treasure);
    }

    /**
     * Given getting to an exit is the only goal in a dungeon.
     * When a player goes through the exit.
     * Then the game ends.
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
     * When the player goes through the exit.
     * Then nothing happens.
     */
    @Test
    public void testAndGoalNotComplete() {
        initialise();
        Composite and = new And();
        and.add(goalBoulders);
        and.add(goalExit);
        dungeon.setGoal(and);
        dungeon.setGoal(goalBoulders);
        player.moveDown();
        assertFalse(dungeon.isComplete());
    }

    /**
     * Given a player has completed all the other goals.
     * When the player goes through the exit.
     * Then the game ends.
     */
    @Test
    public void testAndGoalComplete() {
        testAndGoalNotComplete();
        player.moveBoulder("right");
        assertTrue(dungeon.isComplete());
    }

    @Test
    public void testOrGoalComplete() {
        initialise();
        Composite or = new Or();
        or.add(goalBoulders);
        or.add(goalExit);
        dungeon.setGoal(or);
        assertFalse(dungeon.isComplete());
        player.moveDown();
        assertTrue(dungeon.isComplete());
    }

    @Test
    public void testAndComplexGoal() {
        initialise();
        Composite and = new And();
        Composite or = new Or();
        and.add(goalTreasure);
        and.add(or);
        or.add(goalExit);
        or.add(goalBoulders);
        dungeon.setGoal(and);
        assertFalse(dungeon.isComplete());
        player.moveDown();
        assertFalse(dungeon.isComplete());
        player.moveBoulder("right");
        assertFalse(dungeon.isComplete());
        player.moveDown(); // Pick up treasure
        assertTrue(dungeon.isComplete());
    }

    @Test
    public void testOrComplexGoal() {
        initialise();
        Composite and1 = new And();
        Composite and2 = new And();
        Composite or = new Or();
        or.add(and1);
        or.add(and2);
        and1.add(goalExit);
        and1.add(goalBoulders);
        and2.add(goalTreasure);
        and2.add(goalEnemies);
        dungeon.setGoal(or);
        assertFalse(dungeon.isComplete());
        player.moveDown();
        assertFalse(dungeon.isComplete());
        player.moveBoulder("right");
        assertTrue(dungeon.isComplete());
    }
}