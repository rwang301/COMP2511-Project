package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import unsw.dungeon.Component;
import unsw.dungeon.Enemy;
import unsw.dungeon.Dungeon;
import unsw.dungeon.GoalEnemies;
import unsw.dungeon.Potion;
import unsw.dungeon.Player;

public class TestPotion {
    private Dungeon dungeon = new Dungeon(3, 3);
    private Player player = new Player(dungeon, 0, 0);
    private Potion potion1 = new Potion(0, 1);
    private Potion potion2 = new Potion(1, 0);
    private Component goalEnemies = new GoalEnemies();
    private Enemy enemy1 = new Enemy(dungeon, 0, 2);
    private Enemy enemy2 = new Enemy(dungeon, 1, 2);

    public void initialise() {
        dungeon.setPlayer(player);
        dungeon.setGoal(goalEnemies);
        dungeon.addEntity(potion1);
        dungeon.addEntity(potion2);
        dungeon.addEntity(enemy1);
        dungeon.addEntity(enemy2);
        enemy1.initialise(player);
        enemy2.initialise(player);
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
        }
    }

    /**
     * Given a player does not already hold a potion.
     * When the player tires to pick up a potion.
     * Then the potion disappears.
     */
    @Test
    public void testPickup() {
        initialise();
        player.moveDown();
        assertFalse(dungeon.getEntities().contains(potion1));
        assertTrue(player.getPotion() != null);
    }

    /**
     * Given a player already holds a potion.
     * When the player picks up another potion.
     * Then the effect of the existing potion gets extended for 5 seconds.
     */
    @Test
    public void testExtend() {
        testPickup(); // pick up a potion
        sleep(1000);
        player.moveRight();
        player.moveUp(); // Pick up a second potion
    }

    /**
     * Given a player holds a potion.
     * When the player collides with an enemy.
     * Then the enemy disappears.
     */
    @Test
    public void testKill() {
        testPickup();
        player.moveDown();
        assertFalse(dungeon.getEntities().contains(enemy1));
    }

    /**
     * Given a player has picked up a potion.
     * When 5 seconds of time has passed.
     * Then the potion effect disappears.
     */
    @Test
    public void testEffect() {
        testKill();
        assertEquals(player.getX(), 0);
        assertEquals(player.getY(), 2);
        assertEquals(enemy2.getX(), 1);
        assertEquals(enemy2.getY(), 2);

        // TODO should've worked
        sleep(1050);
        assertEquals(enemy2.getX(), 2);
        assertEquals(enemy2.getY(), 2);
    }
}