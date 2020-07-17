package test;

import org.junit.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.Key;
import unsw.dungeon.Player;

public class TestKey {
    private Dungeon dungeon = new Dungeon(4, 4);
    private Player player = new Player(dungeon, 0, 0);
    Key key1 = new Key(1, 0);
    Key key2 = new Key(2, 0);

    public void initilise() {
        dungeon.setPlayer(player);
        dungeon.addEntity(key1);
        dungeon.addEntity(key2);
    }

    /**
     * Given a player does not already hold a key.
     * When the player tries to pick up a key.
     * Then the key disappears.
     */
    @Test
    public void testPickup() {
        initilise();
        player.moveRight();
        assert !dungeon.getEntities().contains(key1);
        player.moveRight();
        assert dungeon.getEntities().contains(key1);
    }

    /**
     * Given a player already holds a key.
     * When the player tires to pick up a key.
     * Then nothing happens.
     */
    @Test
    public void testCannotPickup() {
        initilise();
        player.moveDown();
        player.moveDown();
        assert dungeon.getEntities().contains(key1);
    }
}