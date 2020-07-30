package unsw.dungeon;

public class Backpack {
    private int treasure = 0;
    private Key key = null;
    private Potion potion = null;
    private Sword sword = null;

    int getTreasure() {
        return treasure;
    }

    Key getKey() {
        return key;
    }

    void setKey(Key key) {
        this.key = key;
    }

    Potion getPotion() {
        return potion;
    }

    void setPotion(Potion potion) {
        this.potion = potion;
    }

    Sword getSword() {
        return sword;
    }

	void hit(Player player) {
        sword.setHit();
        if (!sword.capable()) {
            player.use(sword);
            sword = null;
        }
	}

	Door getKeyDoor() {
		return key.getDoor();
    }
    
    /**
     * If the pickupable item is a key or a sword then check if they already exist in the backpack
     * @param pickupable
     * @return true if the pickupable item is a potion or a treasure because they can be picked up again
     */
    boolean noItem(Pickupable pickupable, Player player) {
        if (pickupable.getClass() == Key.class) return key == null;
        else if (pickupable.getClass() == Sword.class) return sword == null;
        else if (pickupable.getClass() == Medicine.class) return player.canAddLive();
        else return true;
    }

    void setItem(Pickupable pickupable, Player player) {
        if (pickupable.getClass() == Key.class) key = (Key) pickupable;
        else if (pickupable.getClass() == Sword.class) sword = (Sword) pickupable;
        else if (pickupable.getClass() == Potion.class) potion = ((Potion) pickupable).pickup(potion, player);
        else if (pickupable.getClass() == Medicine.class) player.setHealth();
        else if (pickupable.getClass() == Treasure.class) {
            treasure++;
            player.complete();
        }
    }
}