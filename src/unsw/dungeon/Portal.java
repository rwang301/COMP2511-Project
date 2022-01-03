package unsw.dungeon;

public class Portal extends Entity {

    private Portal matchingPortal = null;

    public Portal(int x, int y) {
        super(x, y);
    }

    public Portal getMatchingPortal() {
        return matchingPortal;
    }

    public void setMatchingPortal(Portal matchingPortal) {
        this.matchingPortal = matchingPortal;
    }
}
