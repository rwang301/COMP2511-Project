package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Strategy {
    Entity character;
    Player player;
    private Dungeon dungeon;
    private Entity current;
    private List<Enemy> visited = new ArrayList<>();

    /**
     * Implementation of the strategy for how the enemy should move
     */
    abstract void move();

    Strategy(Dungeon dungeon, Entity character) {
        this.dungeon = dungeon;
        this.character = character;
    }

    void setPlayer(Player player) {
        this.player = player;
    }

	void setCurrentPosition() {
        visited.add(new Enemy(dungeon, character.getX(), character.getY()));
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
            if (!character.equals(entity) && character.isOn(entity)) {
                if (entity.getClass() == Door.class && ((Door) entity).isOpen()) {
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
        if (visited.contains(character)) return false;
        if (isOn(Blockable.class)) {
            return false;
        } else if (isOn(Player.class)) {// Assume hound will never run into player
            if (player.getPotion() != null) return false;
            ((Enemy) character).collide(player);
        } else if (isOn(Hound.class)) {
            if (player.getHound() == current) {
                player.kill((Enemy) character);
                player.sacrifice();
            }
        } else if (isOn(Portal.class)) {
            character.x().set(((Portal) current).getPortal().getX());
            character.y().set(((Portal) current).getPortal().getY());
        }
        return true;
    }

    boolean moveUp() {
        if (character.getY() > 0) {
            character.y().set(character.getY() - 1);
            if (!canMove()) {
                character.y().set(character.getY() + 1);
                return false;
            } else {
                visited.add(new Enemy(dungeon, character.getX(), character.getY()));
                return true;
            }
        }
        return false;
    }

    boolean moveDown() {
        if (character.getY() < dungeon.getHeight() - 1) {
            character.y().set(character.getY() + 1);
            if (!canMove()) {
                character.y().set(character.getY() - 1);
                return false;
            } else {
                visited.add(new Enemy(dungeon, character.getX(), character.getY()));
                return true;
            }
        }
        return false;
    }

    boolean moveLeft() {
        if (character.getX() > 0) {
            character.x().set(character.getX() - 1);
            if (!canMove()){
                character.x().set(character.getX() + 1);
                return false;
            } else {
                visited.add(new Enemy(dungeon, character.getX(), character.getY()));
                return true;
            }
        }
        return false;
    }

    boolean moveRight() {
        if (character.getX() < dungeon.getWidth() - 1) {
            character.x().set(character.getX() + 1);
            if (!canMove()) {
                character.x().set(character.getX() - 1);
                return false;
            } else {
                visited.add(new Enemy(dungeon, character.getX(), character.getY()));
                return true;
            }
        }
        return false;
    }
}
