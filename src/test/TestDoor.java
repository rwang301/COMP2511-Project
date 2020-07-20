package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.Enemy;
import unsw.dungeon.Key;
import unsw.dungeon.Door;
import unsw.dungeon.Player;

public class TestDoor {
    private Dungeon dungeon = new Dungeon(4, 4);
    private Player player = new Player(dungeon, 0, 0);
    private Key key = new Key(1, 0);
    private Key key2 = new Key(3, 2);
    private Key key3 = new Key(3, 3);
    private Door door = new Door(2, 0);
    private Door door2 = new Door(2, 1);
    private Door door3 = new Door(3, 1);
    private Enemy enemy = new Enemy(dungeon, 3, 0);

    public void initialise() {
        dungeon.setPlayer(player);
        dungeon.addEntity(player);
        dungeon.addEntity(key);
        dungeon.addEntity(door);
        dungeon.addEntity(door2);
        dungeon.addEntity(door3);
        dungeon.addEntity(enemy);
        key.setDoor(door);
        key2.setDoor(door2);
        key3.setDoor(door3);
        player.attach(enemy);
        enemy.initialise(player);
    }

    private void assertCoordinates(int x, int y) {
        assertEquals(player.getX(), x);
        assertEquals(player.getY(), y);
    }

    /**
     * Given a player has opened a door.
     * Then the door stays open throughout the game.
     */
    @Test
    public void testDoorStaysOpen() {
        initialise();
        assertFalse(door.isOpen());
        assertEquals(enemy.getX(), 3);
        assertEquals(enemy.getY(), 0);
        sleep(1050);
        assertEquals(enemy.getX(), 3);
        assertEquals(enemy.getY(), 0);

        player.moveRight(); // Pick up key 1
        player.moveRight(); // Open door 1
        assertCoordinates(2, 0);
        assertTrue(player.isOn(door));

        player.moveLeft();
        player.moveRight();
        player.moveLeft();
        assertTrue(door.isOpen());
        assertCoordinates(1, 0);
    }
    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
        }
    }

    /**
     * Given a door has not been opened yet.
     * Then it is closed.
     */
    @Test
    public void testDoorClosed() {
        initialise();
        assertFalse(door2.isOpen());
        player.moveDown();
        player.moveRight();
        player.moveRight();
        assertFalse(door2.isOpen());
        assertCoordinates(1, 1);
    }

    /**
     * There are 3 doors in a dungeon
     */
    @Test
    public void testThreeDoors() {
        initialise();
        int count = player.getEntities(Door.class).size();
        assertEquals(count, 3);
    }
}