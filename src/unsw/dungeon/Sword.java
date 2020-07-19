package unsw.dungeon;

public class Sword extends Entity implements Pickupable {
    private int hits = 0;

    public Sword(int x, int y) {
        super(x, y);
    }

	public void setHit() {
		hits++;
	}

    /**
     * Check if the sword is still capable of hitting enemies
     * @return true if the sword has not hit 5 enemies yet otherwise false
     */
	public boolean capable() {
		return hits < 5;
	}
}
