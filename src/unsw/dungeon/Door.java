package unsw.dungeon;

public class Door extends Entity {
    private Key key = null;
    private boolean open = false;

    public Door(int x, int y) {
        super(x, y);
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public boolean isOpen() {
        return open;
    }

    public void open(Player player) {
        open = !open;
        player.setKey(null);
    }

    @Override
    public String toString() {
        return super.toString() + " [key=" + key + ", open=" + open + "]";
    }

}