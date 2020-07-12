package unsw.dungeon;

public class Key extends Entity implements Pickupable {
    private Door door = null;

    public Key(int x, int y) {
        super(x, y);
    }

    public Door getDoor() {
        return door;
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    @Override
    public void pickup(Player player) {
        if (player.getKey() == null) {
            player.setKey(this);
            player.pickup(this);
        }
    }

}