package unsw.dungeon;

public class MoveToward extends Strategy {

    public MoveToward(Dungeon d, Enemy e) {
        super(d, e);
    }

    @Override
    public void move() {
        System.out.println(enemy);
        if (enemy.getX() > enemy.getPlayer().getX()) {
            // enemy is to the right of player
            if (!moveLeft()) {
                // enemy can't move right
                // therefore check if going up or down is closer
                if (enemy.getY() > enemy.getPlayer().getY()) {
                    if (!moveUp()) {
                        // no moves are optimal
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
        } else if (enemy.getX() < enemy.getPlayer().getX()) {
            if (!moveRight()) {
                if (enemy.getY() > enemy.getPlayer().getY()) {
                    if (!moveUp()) {
                        // no moves are optimal
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
        } else {
            // player is in same column as enemy
            if (!moveUp()) {
                if (!moveDown()) {
                    if (!moveUp()) {
                        moveDown();
                    }
                }
            }
        }
    }
}
