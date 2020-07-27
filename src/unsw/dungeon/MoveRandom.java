package unsw.dungeon;

import java.util.Random;

public class MoveRandom extends Strategy {

    MoveRandom(Dungeon dungeon, Gnome gnome) {
        super(dungeon, gnome);
    }

    @Override
    void move() {
        Random move = new Random();
        boolean canMove = false;
        while (!canMove) {
            switch (move.nextInt(4)) {
                case 0:
                    canMove = moveUp();
                    break;
                case 1:
                    canMove = moveDown();
                    break;
                case 2:
                    canMove = moveLeft();
                    break;
                case 3:
                    canMove = moveRight();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    boolean canMove() {
        if (isOn(Blockable.class)) {
            return false;
        } else if (isOn(Player.class)) {
            character.collide(player);
        } else if (isOn(Hound.class)) {
            if (player.getHound() == current) {
                player.sacrifice();
            }
        } else if (isOn(Portal.class)) {
            character.x().set(((Portal) current).getPortal().getX());
            character.y().set(((Portal) current).getPortal().getY());
        }
        return true;
    }
    
}