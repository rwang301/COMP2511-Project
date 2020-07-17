package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.Key;
import unsw.dungeon.Door;
import unsw.dungeon.Player;

public class TestDoor {
    private Dungeon dungeon = new Dungeon(4, 4);
    private Player player = new Player(dungeon, 0, 0);
    Key key = new Key(1, 0);
    Key key2 = new Key(3, 3);
    Key key3 = new Key(2, 4);
    Door door = new Door(2, 0);
    Door door2 = new Door(4, 4);
    Door door3 = new Door(3, 4);

    public void initilise() {
        dungeon.setPlayer(player);
        dungeon.addEntity(key);
        key.setDoor(door);
        key2.setDoor(door2);
        key3.setDoor(door3);
        dungeon.addEntity(door);
        dungeon.addEntity(door2);
        dungeon.addEntity(door3);
    }

    /**
     * Given a player has opened a door.
     * Then the door stays open throughout the game.
     */
    @Test
    public void testDoorStaysOpen() {
        initilise();
        player.moveRight();
        player.moveRight();
        assertEquals(player.getX(), 2);
        assertEquals(player.getY(), 0);
        assertEquals(door.getX(), 2);
        assertEquals(door.getY(), 0);
        player.moveRight();
        player.moveLeft();
        player.moveLeft();
        assertEquals(player.getX(), 1);
        assertEquals(player.getY(), 0);
        assertEquals(door.getX(), 2);
        assertEquals(door.getY(), 0);
    }


    /**
     * Given a door has not been opened yet. Then it is closed
     */
    @Test
    public void testDoorClosed() {
        initilise();
        player.moveDown();
        player.moveRight();
        player.moveRight();
        player.moveRight();
        player.moveUp();
        player.moveLeft();
        assertEquals(player.getX(), 3);
        assertEquals(player.getY(), 0);
        assertEquals(door.getX(), 2);
        assertEquals(door.getY(), 0);
    }

    /**
     * There are 3 doors in a dungeon
     */
    @Test
    public void testThreeDoors() {
        initilise();
        int count = player.getEntities(Door.class).size();
        assertEquals(count, 3);
    }


}