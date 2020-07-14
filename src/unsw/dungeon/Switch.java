package unsw.dungeon;

public class Switch extends Entity {
    private boolean triggered = false;

    public Switch(int x, int y) {
        super(x, y);
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(Player player) {
        triggered = !triggered;
        player.complete();
    }
}