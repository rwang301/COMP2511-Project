package test;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.Player;

public class TestPlayer {
    private Dungeon dungeon = new Dungeon(4, 4);

    @Test
    public void testMove() {
        Player player = new Player(dungeon, 1, 2);
        assertEquals(player.getX(), 1);
        assertEquals(player.getY(), 2);
        player.moveRight();
        assertEquals(player.getX(), 2);
        assertEquals(player.getY(), 2);
        player.moveDown();
        assertEquals(player.getX(), 2);
        assertEquals(player.getY(), 3);
        player.moveLeft();
        assertEquals(player.getX(), 1);
        assertEquals(player.getY(), 3);
        player.moveUp();
        assertEquals(player.getX(), 1);
        assertEquals(player.getY(), 2);
    }

}