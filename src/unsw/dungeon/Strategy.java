package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Strategy {
    private Dungeon dungeon;
    Enemy enemy;
    Player player;
    Entity current;
    private List<Enemy> visited = new ArrayList<>();

    abstract void move();

    Strategy(Dungeon dungeon, Enemy enemy) {
        this.dungeon = dungeon;
        this.enemy = enemy;
    }

    void setPlayer(Player player) {
        this.player = player;
    }

	void setCurrentPosition() {
        visited.add(new Enemy(dungeon, enemy.getX(), enemy.getY()));
	}

	void reset() {
        visited = new ArrayList<>();
	}

    List<Entity> getEntities(Class<?> entityType) {
        return dungeon.getEntities().stream().filter(entity -> entityType.isAssignableFrom(entity.getClass())).collect(Collectors.toList());
    }

    boolean isOn(Class<?> entityType) {
        for (Entity entity: getEntities(entityType)) {
            if (enemy.isOn(entity)) {
                current = entity;
                return true;
            }
        }
        return false;
    }

    boolean canMove() {
        if (visited.contains(enemy)) return false;
        if (isOn(Blockable.class)) {
            if (current.getClass() == Door.class) return ((Door)current).isOpen();
            else return false;
        } else if (isOn(Player.class)) {
            enemy.collide(player);
        } else if (isOn(Portal.class)) {
            enemy.x().set(((Portal)current).getPortal().getX());
            enemy.y().set(((Portal)current).getPortal().getY());
        }
        return true;
    }

    boolean moveUp() {
        if (enemy.getY() > 0)
            enemy.y().set(enemy.getY() - 1);
        if (!canMove()) {
            enemy.y().set(enemy.getY() + 1);
            return false;
        } else {
            visited.add(new Enemy(dungeon, enemy.getX(), enemy.getY()));
            return true;
        }
    }

    boolean moveDown() {
        if (enemy.getY() < dungeon.getHeight() - 1)
            enemy.y().set(enemy.getY() + 1);
        if (!canMove()) {
            enemy.y().set(enemy.getY() - 1);
            return false;
        } else {
            visited.add(new Enemy(dungeon, enemy.getX(), enemy.getY()));
            return true;
        }
    }

    boolean moveLeft() {
        if (enemy.getX() > 0)
            enemy.x().set(enemy.getX() - 1);
        if (!canMove()){
            enemy.x().set(enemy.getX() + 1);
            return false;
        } else {
            visited.add(new Enemy(dungeon, enemy.getX(), enemy.getY()));
            return true;
        }
    }

    boolean moveRight() {
        if (enemy.getX() < dungeon.getWidth() - 1)
            enemy.x().set(enemy.getX() + 1);
        if (!canMove()) {
            enemy.x().set(enemy.getX() - 1);
            return false;
        } else {
            visited.add(new Enemy(dungeon, enemy.getX(), enemy.getY()));
            return true;
        }
    }
}