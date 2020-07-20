package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import unsw.dungeon.Dungeon;
import unsw.dungeon.GoalBoulders;
import unsw.dungeon.Key;
import unsw.dungeon.Player;
import unsw.dungeon.Switch;
import unsw.dungeon.Boulder;
import unsw.dungeon.Component;
import unsw.dungeon.Door;
import unsw.dungeon.Wall;

public class TestBoulder {
    private Dungeon dungeon = new Dungeon(4, 4);
    private Player player = new Player(dungeon, 0, 1);
    Boulder boulder = new Boulder(1, 1);
    Component goalBoulders = new GoalBoulders();

    private void initialise() {
        dungeon.setGoal(goalBoulders);
        dungeon.setPlayer(player);
        dungeon.addEntity(player);
        dungeon.addEntity(boulder);
    }

    private void assertCoordinates(int playerX, int playerY, int boulderX, int boulderY) {
        assertEquals(player.getX(), playerX);
        assertEquals(player.getY(), playerY);
        assertEquals(boulder.getX(), boulderX);
        assertEquals(boulder.getY(), boulderY);
    }

    /**
     * Given a wall is on a square.
     * When a player tries to push a boulder onto that square
     * The player and boulder stay where they were.
     */
    @Test
    public void testBlockWall() {
        initialise();
        Boulder b2 = new Boulder(2, 2);
        Wall w = new Wall (2, 1);
        Wall w2 = new Wall(1, 2);
        dungeon.addEntity(w);
        dungeon.addEntity(w2);
        player.moveBoulder("right");
        assertCoordinates(0, 1, 1, 1);
        assertEquals(w.getX(), 2);
        assertEquals(w.getY(), 1);

        player.moveUp();
        player.moveRight();
        player.moveBoulder("down");
        assertCoordinates(1, 0, 1, 1);
        assertEquals(w2.getX(), 1);
        assertEquals(w2.getY(), 2);

        player.moveRight();
        player.moveRight();
        player.moveDown();
        player.moveDown();
        player.moveBoulder("left");
        assertEquals(player.getX(), 3);
        assertEquals(player.getY(), 2);
        assertEquals(b2.getX(), 2);
        assertEquals(b2.getY(), 2);
        assertEquals(w2.getX(), 1);
        assertEquals(w2.getY(), 2);

        player.moveDown();
        player.moveLeft();
        player.moveBoulder("up");
        assertEquals(player.getX(), 2);
        assertEquals(player.getY(), 3);
        assertEquals(b2.getX(), 2);
        assertEquals(b2.getY(), 2);
        assertEquals(w.getX(), 2);
        assertEquals(w.getY(), 1);
    }

    /**
     * Given a player tries to move to a square where a boulder is on.
     * Then the player and the boulder stay where they were.
     */
    @Test
    public void testBlockPlayer() {
        initialise();
        player.moveRight();
        assertCoordinates(0, 1, 1, 1);

        player.moveUp();
        player.moveRight();
        player.moveDown();
        assertCoordinates(1, 0, 1, 1);

        player.moveRight();
        player.moveDown();
        player.moveLeft();
        assertCoordinates(2, 1, 1, 1);

        player.moveDown();
        player.moveLeft();
        player.moveUp();
        assertCoordinates(1, 2, 1, 1);
    }


    /**
     * Given a player tires to push a boulder.
     * When there is no entity on the adjacent sqaure in the direction of pushing.
     * Then the boulder moves to that square and the player stays where they were.
     */
    @Test
    public void testMoveBoulder() {
        initialise();
        player.moveBoulder("right");
        assertCoordinates(0, 1, 2, 1);

        player.moveRight();
        player.moveUp();
        player.moveRight();
        player.moveBoulder("down");
        assertCoordinates(2, 0, 2, 2);

        player.moveDown();
        player.moveRight();
        player.moveDown();
        player.moveBoulder("left");
        assertCoordinates(3, 2, 1, 2);

        player.moveLeft();
        player.moveDown();
        player.moveLeft();
        player.moveBoulder("up");
        assertCoordinates(1, 3, 1, 1);
    }

    /**
     * Given a player tries to push a boulder.
     * When there is another boulder on the adjacent square in the direction of pushing.
     * Then the boulder and the player stay where they were.
     */
    @Test
    public void testBlockableBoulder(){
        initialise();
        Boulder b2 = new Boulder(2, 1);
        dungeon.addEntity(b2);
        player.moveBoulder("right");
        assertCoordinates(0, 1, 1, 1);
        assertEquals(b2.getX(), 2);
        assertEquals(b2.getY(), 1);

        player.moveDown();
        player.moveRight();
        player.moveRight();
        player.moveRight();
        player.moveUp();
        player.moveBoulder("left");
        assertCoordinates(3, 1, 1, 1);
        assertEquals(b2.getX(), 2);
        assertEquals(b2.getY(), 1);

        player.moveUp();
        player.moveLeft();
        player.moveBoulder("down");
        player.moveDown();
        player.moveRight();
        player.moveDown();
        player.moveBoulder("left");
        player.moveLeft();
        player.moveDown();
        player.moveLeft();

        player.moveBoulder("up");
        assertCoordinates(1, 3, 1, 1);
        assertEquals(b2.getX(), 1);
        assertEquals(b2.getY(), 2);

        player.moveLeft();
        player.moveUp();
        player.moveUp();
        player.moveUp();
        player.moveRight();
        player.moveBoulder("down");
        assertCoordinates(1, 0, 1, 1);
        assertEquals(b2.getX(), 1);
        assertEquals(b2.getY(), 2);
    }

    /**
     * Given a player tries to push a boulder.
     * When there is a door on the adjacent sqaure in the direction of pushing.
     * Given the door is closed.
     * Then the boulder and the player stay where they were.
     */
    @Test
    public void testclosedDoor() {
        initialise();
        Key key = new Key(0, 0);
        dungeon.addEntity(key);
        Door door = new Door(1, 0);
        dungeon.addEntity(door);
        key.setDoor(door);
        player.moveDown();
        player.moveRight();
        player.moveBoulder("up");
        assertCoordinates(1, 2, 1, 1);
    }

    /**
     * Given a player tries to push a boulder.
     * When there is a door on the adjacent sqaure in the direction of pushing.
     * Given the door is open.
     * Then the boulder moves to that square sharing with the door and the player stays where they were.
     */
    @Test
    public void testOpenDoor() {
        testclosedDoor();
        player.moveLeft();
        player.moveUp();
        player.moveUp(); // Pick up a key
        player.moveRight(); // Open a door
        player.moveLeft();
        player.moveDown();
        player.moveDown();
        player.moveRight();
        player.moveBoulder("up");
        assertCoordinates(1, 2, 1, 0);

        player.moveUp();
        player.moveLeft();
        player.moveUp();
        player.moveBoulder("right");
        assertCoordinates(0, 0, 2, 0);

        player.moveRight();
        player.moveDown();
        player.moveRight();
        player.moveRight();
        player.moveUp();
        player.moveBoulder("left");
        assertCoordinates(3, 0, 1, 0);
    }

    /**
     * Given a player tries to push a boulder.
     * When there is a floor switch on the adjacent square in the direction of pushing.
     * Then the and the player stay where they were. 
     */
    @Test
    public void testSwitch() {
        initialise();
        Switch floorSwitch = new Switch(2, 1);
        dungeon.addEntity(floorSwitch);
        Key key = new Key(2, 1);
        dungeon.addEntity(key);
        player.moveBoulder("right");
        assertCoordinates(0, 1, 1, 1);
    }

    /**
     * Given a player tries to push a boulder.
     * When there is an entity on a floor switch on the adjacent square in the direction of pushing.
     * Then the and the player stay where they were.
     */
    @Test
    public void testSwitchBlock() {
        initialise();
        Switch floorSwitch = new Switch(2, 1);
        dungeon.addEntity(floorSwitch);
        player.moveBoulder("right");
        assertCoordinates(0, 1, 2, 1);
        assertTrue(dungeon.isComplete());
    }

    @Test
    public void testInvalidPush() {
        initialise();
        player.moveBoulder("left");
        assertCoordinates(0, 1, 1, 1);

        player.moveUp();
        player.moveRight();
        player.moveBoulder("up");
        assertCoordinates(1, 0, 1, 1);

        player.moveRight();
        player.moveDown();
        player.moveBoulder("right");
        assertCoordinates(2, 1, 1, 1);

        player.moveDown();
        player.moveLeft();
        player.moveBoulder("down");
        assertCoordinates(1, 2, 1, 1);
    }
}