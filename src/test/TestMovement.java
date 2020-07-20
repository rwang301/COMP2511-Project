package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import unsw.dungeon.Enemy;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Potion;
import unsw.dungeon.Wall;
import unsw.dungeon.Player;

public class TestMovement {
    private Dungeon dungeon = new Dungeon(5, 5);
    private Player player = new Player(dungeon, 2, 1);
    private Enemy enemy = new Enemy(dungeon, 1, 0);
    private Potion potion = new Potion(3, 1);
    private Wall wall1 = new Wall(2, 0);
    private Wall wall2 = new Wall(1, 1);

    public void initialise() {
        dungeon.setPlayer(player);
        dungeon.addEntity(player);
        dungeon.addEntity(enemy);
        dungeon.addEntity(potion);
        dungeon.addEntity(wall1);
        dungeon.addEntity(wall2);
        player.attach(enemy);
        enemy.initialise(player);
    }

    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
        }
    }

    private void assertCoordinates(int x, int y) {
        assertEquals(enemy.getX(), x);
        assertEquals(enemy.getY(), y);
    }

    @Test
    public void testmoveLeft() {
        initialise();
        assertTrue(enemy.getStrategy() == enemy.getMoveToward());
        player.moveRight();
        assertTrue(enemy.getStrategy() == enemy.getMoveAway());

        sleep(1050);
        assertCoordinates(0, 0);

        sleep(500);
        assertCoordinates(0, 1);
    }
}