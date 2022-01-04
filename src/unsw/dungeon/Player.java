package unsw.dungeon;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public class Player extends Entity {

    private Dungeon dungeon;

    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Player(Dungeon dungeon, int x, int y) {
        super(x, y);
        this.dungeon = dungeon;
    }

    public void moveUp() {
        if (getY() > 0)
            y().set(getY() - 1);
        if (isOn(Blockable.class)) {
            y().set(getY() + 1);
        }
        if (isOn(Portal.class)) {
            portalTeleport();
        }
    }

    public void moveDown() {
        if (getY() < dungeon.getHeight() - 1)
            y().set(getY() + 1);
        if (isOn(Blockable.class)) {
            y().set(getY() - 1);
        }
        if (isOn(Portal.class)) {
            portalTeleport();
        }
    }

    public void moveLeft() {
        if (getX() > 0)
            x().set(getX() - 1);
        if (isOn(Blockable.class)) {
            x().set(getX() + 1);
        }
        if (isOn(Portal.class)) {
            portalTeleport();
        }
    }

    public void moveRight() {
        if (getX() < dungeon.getWidth() - 1)
            x().set(getX() + 1);
        if (isOn(Blockable.class)) {
            x().set(getX() - 1);
        }
        if (isOn(Portal.class)) {
            portalTeleport();
        }
    }

    private boolean isOn(Class<?> entityType) {
        if (getEntityType(entityType).contains(this)) return true;
        return false;
    }

    private void portalTeleport() {
        Portal matchingPortal = getCurrPortal().getMatchingPortal();
        x().set(matchingPortal.getX());
        y().set(matchingPortal.getY());
    }

    private Portal getCurrPortal() {
        return (Portal)getEntityType(Portal.class).stream().filter(e -> e.equals(this)).findFirst().get();
    }

    private List<Entity> getEntityType(Class<?> entityType) {
        return dungeon.getEntities().stream().filter(e -> entityType.isAssignableFrom(e.getClass())).collect(Collectors.toList());
    }
}
