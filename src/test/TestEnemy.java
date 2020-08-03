package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import unsw.dungeon.Enemy;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Player;
import unsw.dungeon.Portal;
import unsw.dungeon.Potion;
import unsw.dungeon.Switch;
import unsw.dungeon.Wall;

public class TestEnemy {
    private Dungeon dungeon = new Dungeon(3, 3);
    private Player player = new Player(dungeon, 0, 0);
    private Enemy enemy = new Enemy(dungeon, 0, 2);

    public void initialise() {
        dungeon.setPause();
        dungeon.setPlayer(player);
        dungeon.addEntity(player);
        dungeon.addEntity(enemy);
        player.attach(enemy);
        enemy.initialise(player);
    }

    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
        }
    }

    private void assertCoordinates(int x, int y) {
        assertEquals(enemy.getX(), x);
        assertEquals(enemy.getY(), y);
    }

    /**
     * Given a wall is on a square.
     * When an enemy tries to move onto that square.
     * The enemy stays where they were.
     */
    @Test
    public void testBlock() {
        initialise();
        Wall wall = new Wall(0, 1);
        dungeon.addEntity(wall);
        sleep(1050);
        assertCoordinates(1, 2);
    }

    /**
     * Given a floor switch has no entities on it.
     * When an enemy tries to go over the floor switch.
     * Then the enemy goes through.
     */
    @Test
    public void testSwitch() {
        Switch floorSwitch = new Switch(0, 1);
        dungeon.addEntity(floorSwitch);
        testMoveToward();
        assertTrue(enemy.isOn(floorSwitch));
    }

    /**
     * Given an enemy goes through a portal.
     * Then the enemy appears on the corresponding portal.
     */
    @Test
    public void testPortal() {
        initialise();
        Portal portal1 = new Portal(0, 1);
        Portal portal2 = new Portal(1, 0);
        portal1.setPortal(portal2);
        portal2.setPortal(portal1);
        dungeon.addEntity(portal1);
        dungeon.addEntity(portal2);
        sleep(1050);
        assertCoordinates(1, 0);
        assertTrue(enemy.isOn(portal2));
    }

    /**
     * Given the player does not hold a potion.
     * Then the enemies move toward them.
     */
    @Test
    public void testMoveToward() {
        initialise();
        assertTrue(enemy.getStrategy() == enemy.getMoveToward());
        sleep(1050);
        assertCoordinates(0, 1);
    }

    /**
     * Given the player holds a potion.
     * Then the enemies move away from them.
     */
    @Test
    public void testMoveAway() {
        initialise();
        Potion potion = new Potion(0, 1);
        dungeon.addEntity(potion);
        assertTrue(enemy.getStrategy() == enemy.getMoveToward());
        player.moveDown(); // Pick up a potion
        assertTrue(enemy.getStrategy() == enemy.getMoveAway());
        sleep(1050);
        assertCoordinates(1, 2);
        sleep(500);
        assertCoordinates(2, 2);
        sleep(500);
        assertCoordinates(2, 1);
        sleep(500);
        assertCoordinates(1, 1);
        sleep(500);
        assertCoordinates(1, 0);
        sleep(500);
        assertCoordinates(2, 0);
    }

    /**
     * Given the player does not hold a potion.
     * When the enemies cannot move any closer to the player.
     * Then the enemies stop.
     */
    @Test
    public void testStop() {
        initialise();
        Wall wall1 = new Wall(0, 1);
        Wall wall2 = new Wall(1, 0);
        dungeon.addEntity(wall1);
        dungeon.addEntity(wall2);
        sleep(1050);
        assertCoordinates(1, 2);
        sleep(500);
        assertCoordinates(1, 1);
        sleep(500);
        assertCoordinates(2, 1);
        sleep(500);
        assertCoordinates(2, 0);
        sleep(500);
        assertCoordinates(2, 0);
    }
    
    /**
     * Given a player does not hold a sword nor a potion.
     * When an enemy collides with the player.
     * Then the player dies.
     */
    @Test
    public void testDie() {
        testMoveToward();
        sleep(500);
        assertCoordinates(0, 0);
        assertFalse(dungeon.isComplete());
    }

    @Test
    public void testMovement() {
        initialise();
        Wall wall1 = new Wall(1, 1);
        Wall wall2 = new Wall(1, 2);
        dungeon.addEntity(wall1);
        dungeon.addEntity(wall2);
        player.moveRight();
        player.moveRight();
        player.moveDown();
        player.moveDown();

        sleep(1050);
        assertCoordinates(0, 1);

        sleep(500);
        assertCoordinates(0, 0);

        sleep(500);
        assertCoordinates(1, 0);

        sleep(500);
        assertCoordinates(2, 0);

        sleep(500);
        assertCoordinates(2, 1);

        sleep(500);
        assertCoordinates(2, 2);

        assertFalse(dungeon.isComplete());
        assertTrue(player.isOn(player));
    }

    @Test
    public void testLocked() {
        initialise();
        player.x().set(2);
        player.y().set(0);
        Wall wall1 = new Wall(1, 0);
        Wall wall2 = new Wall(2, 1);
        Wall wall3= new Wall(2, 2);
        dungeon.addEntity(wall1);
        dungeon.addEntity(wall2);
        dungeon.addEntity(wall3);

        sleep(1050);
        assertCoordinates(1, 2);

        sleep(500);
        assertCoordinates(1, 1);

        sleep(500);
        assertCoordinates(0, 1);
    }

    @Test
    public void testCanMove() {
        initialise();
        enemy.x().set(2);
        enemy.y().set(1);
        assertCoordinates(2, 1);
        Wall wall1 = new Wall(1, 1);
        Wall wall2 = new Wall(2, 0);
        dungeon.addEntity(wall1);
        dungeon.addEntity(wall2);

        sleep(1050);
        assertCoordinates(2, 2);

        sleep(500);
        assertCoordinates(1, 2);
    }

    @Test
    public void testCannotMoveDown() {
        initialise();
        player.x().set(2);
        player.y().set(2);
        enemy.x().set(1);
        enemy.y().set(1);
        Wall wall1 = new Wall(2, 1);
        Wall wall2 = new Wall(1, 2);
        dungeon.addEntity(wall1);
        dungeon.addEntity(wall2);

        sleep(1050);
        assertCoordinates(0, 1);
    }

    @Test
    public void testCannotMoveLeft() {
        initialise();
        player.x().set(2);
        player.y().set(0);
        enemy.x().set(2);
        enemy.y().set(2);
        Wall wall1 = new Wall(2, 1);
        dungeon.addEntity(wall1);

        sleep(1050);
        assertCoordinates(1, 2);
    }
}