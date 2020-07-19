package unsw.dungeon;

public class MoveToward extends Strategy {

    MoveToward(Dungeon dungeon, Enemy enemy) {
        super(dungeon, enemy);
    }

    @Override
    void move() {
        if (enemy.getX() > player.getX()) {// the enemy is to the right of the player
            if (!moveLeft()) {
                if (enemy.getY() > player.getY()) {// the enemy is below the player
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
        } else if (enemy.getX() < player.getX()) {// the enemy is to the left of the player
            if (!moveRight()) {
                if (enemy.getY() > player.getY()) {// the enemy is below the player
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
        } else {// the enemy and the player is on the same column
            if (enemy.getY() > player.getY()) {// the enemy is below the player
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