package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import unsw.dungeon.Enemy;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Player;

public class TestEnemy {
    private Dungeon dungeon = new Dungeon(4, 4);
    private Player player = new Player(dungeon, 0, 0);
    private Enemy enemy = new Enemy(dungeon, 0, 3);

    public void initialise() {
        dungeon.setPlayer(player);
        dungeon.addEntity(enemy);
        enemy.initialise(player);
    }

    /**
     * Given the player does not hold a potion. Then the enemies move toward them.
     */
    @Test
    public void testMoveToward() {
        initialise();
        try {
            Thread.sleep(1050);
            assertEquals(enemy.getX(), 0);
            assertEquals(enemy.getY(), 2);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
        }
    }


     /**
      * Given the player holds a potion. Then the enemies move away from them. 
      */

    /**
     * Given the player does not hold a potion. When the enemies cannot move any closer to the player. Then the enemies stop. 
     */

    /**
     * Given a wall is on a square. When an enemy tries to move onto that square.
     * The enemy stays where they were. 
     */
    
     /**
      * Given a player does not hold a sword nor a potion. When an enemy collides with the player. Then the player dies. 
      */


     /**
      * Given a floor switch has no entities on it. When an enemy tries to go over the floor switch. 
      * Then the enemy goes through. 
      */

      /**
       * Given an enemy goes through a portal. Then the enemy appears on the corresponding portal. 
       */
}