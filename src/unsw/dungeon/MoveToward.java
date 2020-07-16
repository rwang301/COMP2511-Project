package unsw.dungeon;

public class MoveToward extends Strategy {

    public MoveToward(Dungeon dungeon, Enemy enemy) {
        super(dungeon, enemy);
    }

    @Override
    public void move() {
        if (enemy.getX() > player.getX()) {
            if (!moveLeft()) {
                if (enemy.getY() > player.getY()) {
                    if (!moveUp()) {
                        if (!moveRight()) {
                            if(!moveDown()) reset();
                        }
                    }
                } else {
                    if (!moveDown()) {
                        if (!moveRight()) {
                            if(!moveUp()) reset();
                        }
                    }
                }
            }
        } else {
            if (!moveRight()) {
                if (enemy.getY() > player.getY()) {
                    if (!moveUp()) {
                        if (!moveRight()) {
                            if(!moveDown()) reset();
                        }
                    }
                } else {
                    if (!moveDown()) {
                        if (!moveRight()) {
                            if(!moveUp()) reset();
                        }
                    }
                }
            }
        }
        // TODO consider the case of equal
    }
}