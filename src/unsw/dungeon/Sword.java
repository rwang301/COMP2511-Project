package unsw.dungeon;

public class Sword extends Entity implements Pickupable {
    private int hits = 0;
    public Sword(int x, int y) {
        super(x, y);
    }

    public int getHits() {
        return hits;
    }

	public void setHit() {
		hits++;
	}
}