package test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import unsw.dungeon.Enemy;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Potion;
import unsw.dungeon.Player;

public class TestMovement {
    private Dungeon dungeon = new Dungeon(5, 5);
    private Player player = new Player(dungeon, 2, 1);
    private Enemy enemy = new Enemy(dungeon, 4, 4);

    public void initialise() {
        dungeon.setPlayer(player);
        dungeon.addEntity(player);
        dungeon.addEntity(enemy);
        player.attach(enemy);
        enemy.initialise(player);
    }

    @Test
    public void testmoveLeft() {
        
    }

}