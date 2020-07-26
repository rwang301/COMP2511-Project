package unsw.dungeon;

public class MoveAway extends Strategy {

    MoveAway(Dungeon dungeon, Enemy enemy) {
        super(dungeon, enemy);
    }

    @Override
    void move() {
        if (character.getX() < player.getX()) {// the enemy is to the left of the player
            if (!moveLeft()) {
                if (character.getY() < player.getY()) {// the enemy is above the player
                    if (!moveUp()) {
                        if (!moveRight()) {
                            moveDown();
                        }
                    }
                } else {
                    if (!moveDown()) {
                        if (!moveRight()) {
                            moveUp();
                        }
                    }
                }
            }
        } else if (character.getX() > player.getX()) {// the enemy is to the right of the player
            if (!moveRight()) {
                if (character.getY() < player.getY()) {// the enemy is above the player
                    if (!moveUp()) {
                        if (!moveLeft()) {
                            moveDown();
                        }
                    }
                } else {
                    if (!moveDown()) {
                        if (!moveLeft()) {
                            moveUp();
                        }
                    }
                }
            }
        } else {// the enemy and the player is on the same column
            if (character.getY() < player.getY()) {// the enemy is above the player
                if (!moveUp()) {
                    if (!moveDown()) {
                        if (!moveLeft()) moveRight();
                    }
                }
            } else {
                if (!moveDown()) {
                    if (!moveUp()) {
                        if (!moveLeft()) moveRight();
                    }
                }
            }
        }
    }
}