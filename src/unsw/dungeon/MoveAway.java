package unsw.dungeon;

public class MoveAway extends Strategy {

    MoveAway(Dungeon dungeon, Enemy enemy) {
        super(dungeon, enemy);
    }

    @Override
    void move() {
        if (enemy.getX() < player.getX()) {
            if (!moveLeft()) {
                if (enemy.getY() < player.getY()) {
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
        } else {
            if (!moveRight()) {
                if (enemy.getY() < player.getY()) {
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
        }

    }
}