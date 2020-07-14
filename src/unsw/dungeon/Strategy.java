package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Strategy {
    private Dungeon dungeon;
    Enemy enemy;
    Player player;
    private List<Enemy> visited = new ArrayList<>();

    public abstract void move();

    public Strategy(Dungeon dungeon, Enemy enemy) {
        this.dungeon = dungeon;
        this.enemy = enemy;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

	public void setCurrentPosition() {
        visited.add(new Enemy(dungeon, enemy.getX(), enemy.getY()));
	}

	public void reset() {
        visited = new ArrayList<>();
	}

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
        if (isOn(Blockable.class) || visited.contains(enemy)) return false;
        else if (isOn(Player.class)) player.die();
        return true;
    }

    public boolean moveUp() {
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

    public boolean moveDown() {
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

    public boolean moveLeft() {
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

    public boolean moveRight() {
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