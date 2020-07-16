package test;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.Player;

public class PlayerTest {
    private Dungeon dungeon = new Dungeon(1, 1);

    @Test
    public void testMove() {
        Player player = new Player(dungeon, 1, 2);
        assertEquals(player.getX(), 1);
    }
}