package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import unsw.dungeon.Component;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Enemy;
import unsw.dungeon.GoalEnemies;
import unsw.dungeon.Player;
import unsw.dungeon.Sword;

public class TestSword {
    private Dungeon dungeon = new Dungeon(4, 4);
    private Player player = new Player(dungeon, 0, 0);
    private Sword sword1 = new Sword(1, 0);
    private Sword sword2 = new Sword(2, 0);
    private Component goalEnemies = new GoalEnemies();
    private Enemy enemy1 = new Enemy(dungeon, 1, 1);
    private Enemy enemy2 = new Enemy(dungeon, 1, 2);
    private Enemy enemy3 = new Enemy(dungeon, 1, 3);
    private Enemy enemy4 = new Enemy(dungeon, 2, 1);
    private Enemy enemy5 = new Enemy(dungeon, 2, 2);
    private Enemy enemy6 = new Enemy(dungeon, 2, 3);

    public void initialise() {
        dungeon.setPause();
        dungeon.setGoal(goalEnemies);
        dungeon.setPlayer(player);
        dungeon.addEntity(player);
        dungeon.addEntity(sword1);
        dungeon.addEntity(sword2);
        dungeon.addEntity(enemy1);
        dungeon.addEntity(enemy2);
        dungeon.addEntity(enemy3);
        dungeon.addEntity(enemy4);
        dungeon.addEntity(enemy5);
        dungeon.addEntity(enemy6);
        player.attach(enemy1);
        player.attach(enemy2);
        player.attach(enemy3);
        player.attach(enemy4);
        player.attach(enemy5);
        player.attach(enemy6);
        enemy1.initialise(player);
        enemy2.initialise(player);
        enemy3.initialise(player);
        enemy4.initialise(player);
        enemy5.initialise(player);
        enemy6.initialise(player);
    }

    /**
     * Given a player does not already hold a sword.
     * When the player tries to pick up a sword.
     * Then the sword disappears.
     */
    @Test
    public void testCanPickup() {
        initialise();
        assertTrue(dungeon.getEntities().contains(sword1));
        player.moveRight();
        assertFalse(dungeon.getEntities().contains(sword1));
        assertTrue(player.getSword() != null);
    }

    /**
     * Given a player already holds a sword.
     * When the player tries to pick up a sword.
     * Then nothing happens.
     */
    @Test
    public void testCannotPickup() {
        testCanPickup();
        assertTrue(dungeon.getEntities().contains(sword2));
        player.moveRight();
        assertTrue(dungeon.getEntities().contains(sword2));
    }

    /**
     * Given a player holds a sword.
     * When the player collides with an enemy.
     * Then the enemy disappears.
     */
    @Test
    public void testKill() {
        testCanPickup();
        player.moveDown();
        assertFalse(dungeon.getEntities().contains(enemy1));
    }

    /**
     * Given a player has destroyed all the enemies in a dungeon.
     * Then the goal of destroying all enemies is completed.
     */
    @Test
    public void testGoal() {
        testKill();
        player.moveDown();
        player.moveDown();
        player.moveRight();
        player.moveUp();

        player.moveRight();
        player.moveUp();
        player.moveUp();
        player.moveLeft(); // Pick up a second sword
        assertFalse(dungeon.isComplete());
        player.moveDown();
        assertTrue(dungeon.isComplete());
    }

    /**
     * Given a player holds a sword but no potion.
     * When the player collides with 6 enemies.
     * Then the player dies upon the sixth collision.
     */
    @Test
    public void testCapbility() {
        testKill();
        player.moveDown();
        player.moveDown();
        player.moveRight();
        player.moveUp();
        player.moveUp();
        assertFalse(dungeon.isComplete());
    }
}