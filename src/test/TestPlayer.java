package test;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.Player;

public class TestPlayer {
    private Dungeon dungeon = new Dungeon(2, 2);
    private Player player = new Player(dungeon, 0, 0);

    /**
     * Given that a user hits the Up-arrow key on the keyboard. Then the player moves up to the adjacent square. 
     * Given that a user hits the Down-arrow key on the keyboard. Then the player moves down to the adjacent square. 
     * Given that a user hits the Left-arrow key on the keyboard. Then the player moves left to the adjacent square. 
     * Given that a user hits the Right-arrow key on the keyboard. Then the player moves right to the adjacent square. 
     */
    @Test
    public void testMove() {
        assertEquals(player.getX(), 0);
        assertEquals(player.getY(), 0);

        player.moveRight();
        assertEquals(player.getX(), 1);
        assertEquals(player.getY(), 0);
        player.moveDown();
        assertEquals(player.getX(), 1);
        assertEquals(player.getY(), 1);
        player.moveLeft();
        assertEquals(player.getX(), 0);
        assertEquals(player.getY(), 1);
        player.moveUp();
        assertEquals(player.getX(), 0);
        assertEquals(player.getY(), 0);
    }

    /**
     * Given a player tries to move outside the dimensions of the dungeon.
     * Then nothing happens.
     */
    @Test
    public void testOutsideDungeon() {
        player.moveLeft();
        assertEquals(player.getX(), 0);
        assertEquals(player.getY(), 0);
        player.moveUp();
        assertEquals(player.getX(), 0);
        assertEquals(player.getY(), 0);

        player.moveDown();
        player.moveDown();
        assertEquals(player.getX(), 0);
        assertEquals(player.getY(), 1);
        player.moveLeft();
        assertEquals(player.getX(), 0);
        assertEquals(player.getY(), 1);

        player.moveRight();
        player.moveRight();
        assertEquals(player.getX(), 1);
        assertEquals(player.getY(), 1);
    }
}