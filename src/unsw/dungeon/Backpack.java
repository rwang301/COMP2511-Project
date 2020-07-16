package unsw.dungeon;

public class Backpack {
    private int treasure = 0;
    private Key key = null;
    private Potion potion = null;
    private Sword sword = null;

    public int getTreasure() {
        return treasure;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Potion getPotion() {
        return potion;
    }

    public void setPotion(Potion potion) {
        this.potion = potion;
    }

    public Sword getSword() {
        return sword;
    }

    public void setSword(Sword sword) {
        this.sword = sword;
    }

	public void hit() {
        sword.setHit();
        if (!sword.capable()) sword = null;
	}

	public Door getKeyDoor() {
		return key.getDoor();
    }
    
    /**
     * If the pickupable item is a key or a sword then check if they already exist in the backpack
     * @param pickupable
     * @return true if the pickupable item is a potion or a treasure because they can be picked up again
     */
    public boolean noItem(Pickupable pickupable) {
        if (pickupable.getClass() == Key.class) return key == null;
        else if (pickupable.getClass() == Sword.class) return sword == null;
        else return true;
    }

    public void setItem(Pickupable pickupable, Player player) {
        if (pickupable.getClass() == Key.class) key = (Key)pickupable;
        else if (pickupable.getClass() == Sword.class) sword = (Sword)pickupable;
        else if (pickupable.getClass() == Potion.class) potion = ((Potion)pickupable).pickup(potion, player);
        else treasure++;
    }
}