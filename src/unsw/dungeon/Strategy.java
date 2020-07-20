package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Strategy {
    Enemy enemy;
    Player player;
    private Dungeon dungeon;
    private Entity current;
    private List<Enemy> visited = new ArrayList<>();

    /**
     * Implementation of the strategy for how the enemy should move
     */
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
	    setCurrentPosition();
	}

    List<Entity> getEntities(Class<?> entityType) {
        return dungeon.getEntities().stream().filter(entity -> entityType.isAssignableFrom(entity.getClass())).collect(Collectors.toList());
    }

    boolean isOn(Class<?> entityType) {
        for (Entity entity: getEntities(entityType)) {
            if (!enemy.equals(entity) && enemy.isOn(entity)) {
                if (entity.getClass() == Door.class && ((Door)entity).isOpen()) {
                    continue;
                }
                current = entity;
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the enemy is able to move to the current square
     * @return true if the enemy can move to the current square otherwise false
     */
    boolean canMove() {
        if (visited.contains(enemy)) return false;
        if (isOn(Blockable.class) || isOn(Enemy.class)) {
            return false;
        } else if (isOn(Player.class)) {
            if (player.getPotion() != null) return false;
            enemy.collide(player);
        } else if (isOn(Portal.class)) {
            enemy.x().set(((Portal)current).getPortal().getX());
            enemy.y().set(((Portal)current).getPortal().getY());
        }
        return true;
    }

    boolean moveUp() {
        if (enemy.getY() > 0) {
            enemy.y().set(enemy.getY() - 1);
            if (!canMove()) {
                enemy.y().set(enemy.getY() + 1);
                return false;
            } else {
                visited.add(new Enemy(dungeon, enemy.getX(), enemy.getY()));
                return true;
            }
        }
        return false;
    }

    boolean moveDown() {
        if (enemy.getY() < dungeon.getHeight() - 1) {
            enemy.y().set(enemy.getY() + 1);
            if (!canMove()) {
                enemy.y().set(enemy.getY() - 1);
                return false;
            } else {
                visited.add(new Enemy(dungeon, enemy.getX(), enemy.getY()));
                return true;
            }
        }
        return false;
    }

    boolean moveLeft() {
        if (enemy.getX() > 0) {
            enemy.x().set(enemy.getX() - 1);
            if (!canMove()){
                enemy.x().set(enemy.getX() + 1);
                return false;
            } else {
                visited.add(new Enemy(dungeon, enemy.getX(), enemy.getY()));
                return true;
            }
        }
        return false;
    }

    boolean moveRight() {
        if (enemy.getX() < dungeon.getWidth() - 1) {
            enemy.x().set(enemy.getX() + 1);
            if (!canMove()) {
                enemy.x().set(enemy.getX() - 1);
                return false;
            } else {
                visited.add(new Enemy(dungeon, enemy.getX(), enemy.getY()));
                return true;
            }
        }
        return false;
    }
}
