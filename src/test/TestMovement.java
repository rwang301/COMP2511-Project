package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import unsw.dungeon.Enemy;
import unsw.dungeon.Key;
import unsw.dungeon.Door;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Potion;
import unsw.dungeon.Wall;
import unsw.dungeon.Player;

public class TestMovement {
    private Dungeon dungeon = new Dungeon(6, 6);
    private Player player;
    private Enemy enemy;
    private Potion potion1 = new Potion(4, 2);
    private Potion potion2 = new Potion(4, 0);
    private Potion potion3 = new Potion(0, 4);
    private Door door = new Door(2, 2);
    private Key key = new Key(2, 3);
    private Wall wall1 = new Wall(3, 1);
    private Wall wall2 = new Wall(3, 0);
    private Wall wall3 = new Wall(1, 1);
    private Wall wall4 = new Wall(4, 3);

    public void initialise() {
        dungeon.setPlayer(player);
        dungeon.addEntity(player);
        dungeon.addEntity(enemy);
        dungeon.addEntity(potion1);
        dungeon.addEntity(potion2);
        dungeon.addEntity(potion3);
        dungeon.addEntity(door);
        dungeon.addEntity(key);
        dungeon.addEntity(wall1);
        dungeon.addEntity(wall2);
        dungeon.addEntity(wall3);
        dungeon.addEntity(wall4);
        key.setDoor(door);
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
    public void testMoveToward() {
        player = new Player(dungeon, 3, 2);
        enemy = new Enemy(dungeon, 2, 1);
        initialise();
        player.moveRight();

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
        player.moveUp();

        sleep(500);
        assertCoordinates(4, 4);
    }

    @Test
    public void testMoveAway() {
        player = new Player(dungeon, 0, 5);
        enemy = new Enemy(dungeon, 0, 1);
        initialise();
        assertTrue(enemy.getStrategy() == enemy.getMoveToward());
        player.moveUp(); // Pick up a potion
        assertTrue(enemy.getStrategy() == enemy.getMoveAway());
        sleep(1050);
        assertCoordinates(0, 0);
    }

    @Test
    public void testDoor() {
        player = new Player(dungeon, 3, 2);
        enemy = new Enemy(dungeon, 2, 1);
        initialise();
        player.moveDown();
        player.moveLeft(); // Pick up a key
        player.moveUp(); // Open a door
        player.moveDown();

        sleep(1050);
        assertCoordinates(2, 2);
        assertTrue(enemy.isOn(door));

        sleep(500);
        assertCoordinates(2, 3);
        assertTrue(enemy.isOn(player));
        assertFalse(dungeon.isComplete());
    }

    @Test
    public void testEnemy() {
        player = new Player(dungeon, 3, 2);
        enemy = new Enemy(dungeon, 2, 1);
        Enemy enemy2 = new Enemy(dungeon, 2, 0);
        Wall wall = new Wall(1, 0);
        dungeon.addEntity(wall);
        dungeon.addEntity(enemy2);
        player.attach(enemy2);
        enemy2.initialise(player);
        initialise();

        sleep(1050);
        assertFalse(dungeon.isComplete());
    }
}