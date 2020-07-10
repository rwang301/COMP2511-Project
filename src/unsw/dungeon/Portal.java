package unsw.dungeon;

public class Portal extends Entity {
    private Portal portal = null;

    public Portal(int x, int y) {
        super(x, y);
    }

    public Portal getPortal() {
        return portal;
    }

    public void setPortal(Portal portal) {
        this.portal = portal;
    }

}