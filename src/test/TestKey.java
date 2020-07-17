package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import unsw.dungeon.Door;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Key;
import unsw.dungeon.Player;

public class TestKey {
    private Dungeon dungeon = new Dungeon(4, 4);
    private Player player = new Player(dungeon, 0, 0);
    private Key key1 = new Key(1, 0);
    private Key key2 = new Key(2, 0);
    private Door door1 = new Door(1, 2);
    private Door door2 = new Door(2, 1);

    public void initilise() {
        dungeon.setPlayer(player);
        dungeon.addEntity(key1);
        dungeon.addEntity(key2);
        dungeon.addEntity(door1);
        dungeon.addEntity(door2);
        key1.setDoor(door1);
        key2.setDoor(door2);
    }

    /**
     * Given a player does not already hold a key.
     * When the player tries to pick up a key.
     * Then the key disappears.
     */
    @Test
    public void testCanPickup() {
        initilise();
        player.moveRight();
        assertEquals(dungeon.getEntities().contains(key1), false);
        assertEquals(player.getKey() != null, true);
    }

    /**
     * Given a player already holds a key.
     * When the player tires to pick up a key.
     * Then nothing happens.
     */
    @Test
    public void testCannotPickup() {
        testCanPickup();
        player.moveRight();
        assertEquals(dungeon.getEntities().contains(key2), true);
    }

    /**
     * Given a player holds a key.
     * When the player tries to go through a door which does not correspond to the key.
     * Then the door stays closed.
     */
    @Test
    public void testCannotOpen() {
        initilise();
        player.moveRight();
        assertEquals(door1.isOpen(), false);
        player.moveRight();
        assertEquals(door1.isOpen(), false);
    }

    /**
     * Given a player holds a key.
     * When the player tries to go through a door which corresponds to the key.
     * Then the door opens and the player goes through.
     */
    @Test
    public void testCanOpen() {
        initilise();
        player.moveRight();
        assertEquals(door1.isOpen(), false);
        player.moveDown();
        player.moveDown();
        assertEquals(door1.isOpen(), true);
    }

    /**
     * Given a player used a key to open a door.
     * When the player tries to pick up another key.
     * The key disappears.
     */
    @Test
    public void testOpen() {
        testCanOpen();
        assertEquals(player.getKey() == null, true);

        player.moveUp();
        player.moveUp();
        player.moveRight();
        assertEquals(dungeon.getEntities().contains(key2), false);
        assertEquals(player.getKey() != null, true);
    }
}