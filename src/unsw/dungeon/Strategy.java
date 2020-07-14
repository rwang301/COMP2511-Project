package unsw.dungeon;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Strategy {
    private Dungeon dungeon;
    Enemy enemy;
    Player player;
    private Enemy previous = null;

    public Strategy(Dungeon dungeon, Enemy enemy) {
        this.dungeon = dungeon;
        this.enemy = enemy;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public abstract void move();

    public List<Entity> getEntities(Class<?> entityType) {
        return dungeon.getEntities().stream().filter(entity -> entityType.isAssignableFrom(entity.getClass())).collect(Collectors.toList());
    }

    public boolean isOn(Class<?> entityType) {
        for (Entity entity: getEntities(entityType)) {
            if (enemy.isOn(entity)) {
                return true;
            }
        }
        return false;
    }

    private boolean canMove() {
        if (isOn(Blockable.class) || previous != null && enemy.isOn(previous)) {
            return false;
        }
        return true;
    }

    public boolean moveUp() {
        Enemy tmp = new Enemy(dungeon, enemy.getX(), enemy.getY());
        if (enemy.getY() > 0)
            enemy.y().set(enemy.getY() - 1);
        if (!canMove()) {
            enemy.y().set(enemy.getY() + 1);
            return false;
        } else {
            previous = tmp;
            return true;
        }
    }

    public boolean moveDown() {
        Enemy tmp = new Enemy(dungeon, enemy.getX(), enemy.getY());
        if (enemy.getY() < dungeon.getHeight() - 1)
            enemy.y().set(enemy.getY() + 1);
        if (!canMove()) {
            enemy.y().set(enemy.getY() - 1);
            return false;
        } else {
            previous = tmp;
            return true;
        }
    }

    public boolean moveLeft() {
        Enemy tmp = new Enemy(dungeon, enemy.getX(), enemy.getY());
        if (enemy.getX() > 0)
            enemy.x().set(enemy.getX() - 1);
        if (!canMove()){
            enemy.x().set(enemy.getX() + 1);
            return false;
        } else {
            previous = tmp;
            return true;
        }
    }

    public boolean moveRight() {
        Enemy tmp = new Enemy(dungeon, enemy.getX(), enemy.getY());
        if (enemy.getX() < dungeon.getWidth() - 1)
            enemy.x().set(enemy.getX() + 1);
        if (!canMove()) {
            enemy.x().set(enemy.getX() - 1);
            return false;
        } else {
            previous = tmp;
            return true;
        }
    }
}