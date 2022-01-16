package unsw.dungeon;

public class Key extends Entity implements Pickupable {

    private Door door = null;

    public Key(int x, int y) {
        super(x, y);
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    public Door getDoor() {
        return door;
    }

    @Override
    public void pickup(Player player, Dungeon dungeon) {
        player.setKey(this);
        dungeon.disappear(this);
    }

}
