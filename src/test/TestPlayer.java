package test;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.Player;
import unsw.dungeon.Wall;

public class TestPlayer {
    private Dungeon dungeon = new Dungeon(2, 2);
    private Player player = new Player(dungeon, 0, 0);

    private void assertCoordinates(int x, int y) {
        assertEquals(player.getX(), x);
        assertEquals(player.getY(), y);
    }

    /**
     * Given that a user hits the Up-arrow key on the keyboard. Then the player moves up to the adjacent square. 
     * Given that a user hits the Down-arrow key on the keyboard. Then the player moves down to the adjacent square. 
     * Given that a user hits the Left-arrow key on the keyboard. Then the player moves left to the adjacent square. 
     * Given that a user hits the Right-arrow key on the keyboard. Then the player moves right to the adjacent square. 
     */
    @Test
    public void testMove() {
        assertCoordinates(0, 0);

        player.moveRight();
        assertCoordinates(1, 0);

        player.moveDown();
        assertCoordinates(1, 1);

        player.moveLeft();
        assertCoordinates(0, 1);

        player.moveUp();
        assertCoordinates(0, 0);
    }

    /**
     * Given a player tries to move outside the dimensions of the dungeon.
     * Then nothing happens.
     */
    @Test
    public void testOutsideDungeon() {
        player.moveLeft();
        assertCoordinates(0, 0);

        player.moveUp();
        assertCoordinates(0, 0);

        player.moveDown();
        player.moveDown();
        assertCoordinates(0, 1);

        player.moveRight();
        player.moveRight();
        assertCoordinates(1, 1);
    }

    /**
     * Given a wall is on a square.
     * When a player tries to move onto that square.
     * The player stays where they were.
     */
    @Test
    public void testWall() {
        Wall w1 = new Wall(1, 0);
        Wall w2 = new Wall(0, 1);
        dungeon.addEntity(w1);
        dungeon.addEntity(w2);

        player.moveDown();
        assertCoordinates(0, 0);

        player.moveRight();
        assertCoordinates(0, 0);
    }
}