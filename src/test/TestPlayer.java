package test;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.Player;

public class TestPlayer {

    /**
     * Given that a user hits the Up-arrow key on the keyboard. Then the player moves up to the adjacent square. 
     * Given that a user hits the Down-arrow key on the keyboard. Then the player moves down to the adjacent square. 
     * Given that a user hits the Left-arrow key on the keyboard. Then the player moves left to the adjacent square. 
     * Given that a user hits the Right-arrow key on the keyboard. Then the player moves right to the adjacent square. 
     */
    @Test
    public void testMove() {
        Dungeon dungeon = new Dungeon(4, 4);
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

    /**
     *  If a player tries to move outside the dimensions of the dungeon, then nothing happens.
     */
    @Test
    public void testOutsideDungeon() {
        Dungeon dungeon = new Dungeon(2, 2);
        Player player = new Player(dungeon, 0, 0);
        player.moveLeft();
        assertEquals(player.getX(), 0);
        assertEquals(player.getY(), 0);
        player.moveUp();
        assertEquals(player.getX(), 0);
        assertEquals(player.getY(), 0);

        player.moveDown();
        player.moveDown();
        player.moveDown();
        assertEquals(player.getX(), 0);
        assertEquals(player.getY(), 1);
        player.moveLeft();
        assertEquals(player.getX(), 0);
        assertEquals(player.getY(), 1);

        player.moveRight();
        player.moveRight();
        player.moveRight();
        assertEquals(player.getX(), 1);
        assertEquals(player.getY(), 1);
    }
}