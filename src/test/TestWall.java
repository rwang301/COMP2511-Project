package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.Player;
import unsw.dungeon.Wall;

public class TestWall {
    private Dungeon dungeon = new Dungeon(4, 4);

    @Test
    public void testWall() {
        Player player = new Player(dungeon, 1, 1);
        Wall w1 = new Wall(1, 0);
        Wall w2 = new Wall(2, 1);
        Wall w3 = new Wall(1, 2);
        Wall w4 = new Wall(0, 1);
        dungeon.addEntity(w1);
        dungeon.addEntity(w2);
        dungeon.addEntity(w3);
        dungeon.addEntity(w4);
        player.moveRight();
        assertEquals(player.getX(), 1);
        assertEquals(player.getY(), 1);
        player.moveLeft();
        assertEquals(player.getX(), 1);
        assertEquals(player.getY(), 1);
        player.moveDown();
        assertEquals(player.getX(), 1);
        assertEquals(player.getY(), 1);
        player.moveUp();
        assertEquals(player.getX(), 1);
        assertEquals(player.getY(), 1);
    }
}