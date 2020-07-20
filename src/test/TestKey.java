package test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    public void initialise() {
        dungeon.setPlayer(player);
        dungeon.addEntity(player);
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
        initialise();
        player.moveRight();
        assertFalse(dungeon.getEntities().contains(key1));
        assertTrue(player.getKey() != null);
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
        assertTrue(dungeon.getEntities().contains(key2));
    }

    /**
     * Given a player holds a key.
     * When the player tries to go through a door which does not correspond to the key.
     * Then the door stays closed.
     */
    @Test
    public void testCannotOpen() {
        testCanPickup();
        assertFalse(door2.isOpen());
        player.moveDown();
        player.moveRight();
        assertFalse(door2.isOpen());
    }

    /**
     * Given a player holds a key.
     * When the player tries to go through a door which corresponds to the key.
     * Then the door opens and the player goes through.
     */
    @Test
    public void testCanOpen() {
        initialise();
        player.moveRight();
        assertFalse(door1.isOpen());
        player.moveDown();
        player.moveDown();
        assertTrue(door1.isOpen());
    }

    /**
     * Given a player used a key to open a door.
     * When the player tries to pick up another key.
     * The key disappears.
     */
    @Test
    public void testOpen() {
        testCanOpen();
        assertTrue(player.getKey() == null);

        player.moveUp();
        player.moveUp();
        player.moveRight();
        assertFalse(dungeon.getEntities().contains(key2));
        assertTrue(player.getKey() != null);
    }
}