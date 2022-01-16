package unsw.dungeon;

public class Switch extends Entity {
    public Switch(int x, int y) {
        super(x, y);
    }

    public boolean isTriggered(Player p) {
        return !(p.getEntity(Boulder.class, this.getX(), this.getY()) == null);
    }

}
