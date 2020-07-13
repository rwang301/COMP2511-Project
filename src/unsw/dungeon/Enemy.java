package unsw.dungeon;

public class Enemy extends Entity implements Observer {
    //subject is player, observer is enemy
    public Enemy(int x, int y) {
        super(x, y);
    }

	public void collide(Player player) {
        if (player.getSword() != null || player.getPotion() != null) {
            player.disappear(this);
            player.detach(this);
        } else {
            player.die();
        }
	}

    @Override
    public void update(Subject subject) {
        // TODO Auto-generated method stub
    }
}