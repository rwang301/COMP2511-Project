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
    private Dungeon dungeon = new Dungeon(6, 6);
    private Player player = new Player(dungeon, 3, 2);
    private Enemy enemy = new Enemy(dungeon, 2, 1);
    private Potion potion = new Potion(4, 2);
    private Wall wall1 = new Wall(2, 2);
    private Wall wall2 = new Wall(3, 1);
    private Wall wall3 = new Wall(3, 0);
    private Wall wall4 = new Wall(1, 1);
    private Wall wall5 = new Wall(4, 3);

    public void initialise() {
        dungeon.setPlayer(player);
        dungeon.addEntity(player);
        dungeon.addEntity(enemy);
        dungeon.addEntity(potion);
        dungeon.addEntity(wall1);
        dungeon.addEntity(wall2);
        dungeon.addEntity(wall3);
        dungeon.addEntity(wall4);
        dungeon.addEntity(wall5);
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
        player.moveRight(); // Pick up a potion
        assertTrue(enemy.getStrategy() == enemy.getMoveAway());

        sleep(1050);
        assertCoordinates(2, 0);

        sleep(500);
        assertCoordinates(1, 0);

        sleep(500);
        assertCoordinates(0, 0);

        sleep(500);
        assertCoordinates(0, 1);

        sleep(500);
        assertCoordinates(0, 2);

        sleep(500);
        assertCoordinates(0, 3);

        sleep(500);
        assertCoordinates(0, 4);

        sleep(500);
        assertCoordinates(0, 5);

        sleep(500);
        assertCoordinates(1, 5);

        sleep(500);
        assertCoordinates(2, 5);

        assertTrue(enemy.getStrategy() == enemy.getMoveToward());
        sleep(500);
        assertCoordinates(3, 5);

        sleep(500);
        assertCoordinates(4, 5);

        sleep(500);
        assertCoordinates(4, 4);
        player.moveUp();

        sleep(500);
        assertCoordinates(4, 5);
    }
}