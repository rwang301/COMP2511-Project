package test;

import static org.junit.Assert.assertEquals;
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
    private Dungeon dungeon = new Dungeon(4, 4);
    private Player player = new Player(dungeon, 0, 0);
    private Potion potion1 = new Potion(1, 0);
    private Potion potion2 = new Potion(2, 0);
    private Component goalEnemies = new GoalEnemies();
    private Enemy enemy1 = new Enemy(dungeon, 1, 1);
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

    /**
     * Given a player has picked up a potion.
     * When 5 seconds of time has passed.
     * Then the potion effect disappears.
     */
    @Test
    public void testCanPickup() {
        initialise();
        player.moveRight();
        assertFalse(dungeon.getEntities().contains(potion1));
        assertTrue(player.getPotion() != null);
        System.currentTimeMillis();
    }

    /**
     * Given the player does not hold a potion. Then the enemies move toward them. 
     */

     /**
      * Given the player holds a potion. Then the enemies move away from them. 
      */
        
    /**
     * Given the player does not hold a potion. When the enemies cannot move any closer to the player. Then the enemies stop. 
     */

    /**
     * Given a player holds a potion. When the player collides with an enemy. Then the enemy disappears. 
     */

     
}