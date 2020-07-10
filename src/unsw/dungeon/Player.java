package unsw.dungeon;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The player entity
 * 
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

    private List<Entity> getEntities(Class<?> entityType) {
        return dungeon.getEntities().stream().filter(entity -> entity.getClass() == entityType).collect(Collectors.toList());
    }

    private boolean isOn(Class<?> entityType) {
        if (getEntities(entityType).contains(this)) return true;
        return false;
    }

    /**
     * can only call this method if the player is on a portal
     * otherwise it will throw an exception
     * @return the portal that the player is on right now
     */
    private Portal getPortal() {
        return (Portal)getEntities(Portal.class).stream().filter(portal -> portal.equals(this)).findFirst().get();
    }

    /**
     * given a portal set the player's position to its corresponding portal
     * @param portal
     */
    private void teleport(Portal portal) {
        x().set(portal.getPortal().getX());
        y().set(portal.getPortal().getY());
    }

    public void moveUp() {
        if (getY() > 0)
            y().set(getY() - 1);
        if (isOn(Wall.class))
            y().set(getY() + 1);
        if (isOn(Portal.class))
            teleport(getPortal());
    }

    public void moveDown() {
        if (getY() < dungeon.getHeight() - 1)
            y().set(getY() + 1);
        if (isOn(Wall.class))
            y().set(getY() - 1);
        if (isOn(Portal.class))
            teleport(getPortal());
    }

    public void moveLeft() {
        if (getX() > 0)
            x().set(getX() - 1);
        if (isOn(Wall.class))
            x().set(getX() + 1);
        if (isOn(Portal.class))
            teleport(getPortal());
    }

    public void moveRight() {
        if (getX() < dungeon.getWidth() - 1)
            x().set(getX() + 1);
        if (isOn(Wall.class))
            x().set(getX() - 1);
        if (isOn(Portal.class))
            teleport(getPortal());
    }
}
